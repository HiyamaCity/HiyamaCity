package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class Skill {

    private HashMap<Integer, Integer> levelHashMap = new HashMap<>() {{
        put(1, 500);
        put(2, get(2 - 1) * 2);
        put(3, get(3 - 1) * 2);
        put(4, get(4 - 1) * 2);
        put(5, get(5 - 1) * 2);
        put(6, get(6 - 1) * 2);
        put(7, get(7 - 1) * 2);
        put(8, get(8 - 1) * 2);
        put(9, get(9 - 1) * 2);
        put(10, get(10 - 1) * 2);
        put(11, get(11 - 1) * 2);
        put(12, get(12 - 1) * 2);
        put(13, get(13 - 1) * 2);
        put(14, get(14 - 1) * 2);
        put(15, get(15 - 1) * 2);
        put(16, get(16 - 1) * 2);
        put(17, get(17 - 1) * 2);
        put(18, get(18 - 1) * 2);
        put(19, get(19 - 1) * 2);
        put(20, get(20 - 1) * 2);
    }};

    @Getter
    public enum SkillType {

        SOCIAL, MINING, BUILDING

    }

    @Expose
    private int level = 1;
    @Expose
    private int xp = 0;
    @Expose
    private double cumulativeXP = 0;
    @Expose
    private SkillType skillType;

    public Skill(SkillType skillType) {
        this.skillType = skillType;
    }

    /**
     * @return Returns the XP needed for the next level.
     */
    public int calculateXpForNextLevel() {
        return levelHashMap.get(this.level) - this.xp;
    }

    /**
     * @param amount Amount of xp to add
     * @return Returns if the added xp resulted in a level up.
     */
    public boolean addXP(int amount) {
        this.xp = this.xp + amount;
        return update();
    }

    /**
     * @return Returns if this update ended in a level up.
     */
    private boolean update() {
        this.cumulativeXP = this.cumulativeXP + this.xp;
        if (this.xp >= levelHashMap.get(this.level)) {
            this.level = this.level + 1;
            this.xp = 0;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create().toJson(this);
    }

}
