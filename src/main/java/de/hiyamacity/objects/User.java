package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import de.hiyamacity.database.ConnectionPool;
import de.hiyamacity.lang.LanguageHandler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private UUID uuid;
    @Expose
    private IdentityCard identityCard;
    @Expose
    private List<Ban> bans;
    @Expose
    private List<Skill> skills;

    /**
     * Instantiates a new User object and registers it in the Database paired with its corresponding UUID.
     *
     * @param uuid Identifier by which is queried in the Database and which allows the object to be associated with a Player on the Server.
     */
    public User(UUID uuid) {
        this.purse = 4000;
        this.bank = 2000;
        this.playedMinutes = 0;
        this.playedHours = 0;
        this.uuid = uuid;
        registerUser();
    }

    /**
     * @param uuid Unique user ID of the Player.
     * @return Return if the User is present in the Database or not.
     */
    public static boolean isUserExist(UUID uuid) {
        try (Connection con = ConnectionPool.getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM PLAYERS WHERE UUID = ?")) {
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Registers a new User Object and its corresponding UUID in the Database.
     */
    private void registerUser() {
        if (isUserExist(this.uuid)) return;
        try (Connection con = ConnectionPool.getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO PLAYERS (UUID, PLAYER) VALUES (?,?)")) {
                ps.setString(1, uuid.toString());
                ps.setString(2, this.toString());
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates a known user to a new User Object. Used for Object manipulation.
     *
     * @param uuid Unique user ID of the Player.
     * @param user Updates the User Object in the Database.
     */
    public static void updateUser(UUID uuid, User user) {
        try (Connection con = ConnectionPool.getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("UPDATE PLAYERS SET PLAYER = ? WHERE UUID = ?")) {
                ps.setString(1, user.toString());
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param uuid Unique user ID of the Player.
     * @return Returns a User Object from the Database by its corresponding UUID.
     */
    public static User getUser(UUID uuid) {
        try (Connection con = ConnectionPool.getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT PLAYER FROM PLAYERS WHERE UUID = ?")) {
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) return fromJson(rs.getString("PLAYER"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
