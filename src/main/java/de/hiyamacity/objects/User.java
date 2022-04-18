package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import de.hiyamacity.lang.LanguageHandler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

@Getter
@Setter
public class User {

    @Expose
    private long purse;
    @Expose
    private long bank;
    @Expose
    private long playedMinutes;
    @Expose
    private long playedHours;
    @Expose
    private long kills;
    @Expose
    private long deaths;
    @Expose
    private boolean isConfirmedTeamspeak;
    @Expose
    private UUID uuid;
    @Expose
    private String tsIdentifier;
    @Expose
    private String forename;
    @Expose
    private String surname;
    @Expose
    private List<Ban> bans;
    @Expose
    private List<Skill> skills;

    public User(UUID uuid) {
        this.purse = 4000;
        this.bank = 2000;
        this.playedMinutes = 0;
        this.playedHours = 0;
        this.uuid = uuid;
    }

    /**
     * Adds Experience to a specific SkillType and sends a message to the Player (if online) that they leveled up.
     *
     * @param skillType SkillType of the Skill to add Experience to.
     * @param amount    Amount of Experience to add.
     */
    public void addXP(Skill.SkillType skillType, int amount) {
        Skill skill = this.getSkills().stream().filter(filteredSkill -> filteredSkill.getSkillType().equals(skillType)).toList().get(0);
        if (skill.addXP(amount)) {
            UUID uuid = this.uuid;
            ResourceBundle rs = LanguageHandler.getResourceBundle(uuid);
            Player p = Bukkit.getPlayer(uuid);
            if (p == null) return;
            p.sendMessage(rs.getString("levelUp").replace("%skill%", skill.toString()).replace("%level%", String.valueOf(skill.getLevel())));
        }
    }

    @Override
    public String toString() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create().toJson(this);
    }

    public static User fromJson(String string) {
        return new GsonBuilder().create().fromJson(string, User.class);
    }

}
