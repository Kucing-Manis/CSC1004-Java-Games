package org.example.RPS.component;

import com.almasb.fxgl.dsl.components.HealthIntComponent;
import javafx.scene.paint.Color;
import org.example.RPS.animation.SpriteData;

public class CharacterComponent {
    // Character Abilities:
    // Attack (based on damage)
    // Skill (Can be a Heal Skill, Damage Skill, Shield Skill, Gain Mana Skill, etc)
    // Mana is required to use skill | The total mana will be reduced (by 1 or 2 or 3) everytime any character use skill | Mana can only regenerate (maybe 3 points) after every level (and maybe 5 points after every 10 level)


    // Character Stats
    private String name, skillName, description;
    // Description is currently not used
    private int level, currentHp, maxHp, currentShield, maxShield, damage;
    // CurrentHp cannot be bigger that MaxHp
    // CurrentShield cannot be bigger than MaxShield
    private HealthIntComponent hp = new HealthIntComponent(10);
    private HealthIntComponent shield = new HealthIntComponent(7);
    // Level is the same as Stage Level (Will be used to get stats boost)
    // Hp = Health Point (When hp is 0, the character or enemy die)
    // Shield protect health point | The enemy will damage shield first | Shield will also generate back after each Stage | Pierce damage ignore shield
    // Damage is the amount of damage the character will do to the enemy. For instance, enemy hp = 9, character damage 3. After the character attack the enemy once, the enemy hp = 9-3 = 6.

    private SpriteData spriteData; // To set the character animation

    public CharacterComponent(String NAME, String SKILLNAME, String DESCRIPTION, int LEVEL, int HP, int SHIELD , int DAMAGE, SpriteData SPRITEDATA){
        this.name = NAME;
        this.skillName = SKILLNAME;
        this.description = DESCRIPTION;
        this.level = LEVEL;
        this.currentHp = HP;
        this.maxHp = HP;
        this.currentShield = SHIELD;
        this.maxShield = SHIELD;
        this.damage = DAMAGE;
        this.spriteData = SPRITEDATA;
        this.hp.setMaxValue(HP);
        this.hp.setValue(HP);
        this.shield.setValue(SHIELD);
        this.shield.setMaxValue(SHIELD);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getCurrentShield() {
        return currentShield;
    }

    public void setCurrentShield(int currentShield) {
        this.currentShield = currentShield;
    }

    public int getMaxShield() {
        return maxShield;
    }

    public void setMaxShield(int maxShield) {
        this.maxShield = maxShield;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
    public SpriteData getSpriteData() {
        return spriteData;
    }

    public void setSpriteData(SpriteData spriteData) {
        this.spriteData = spriteData;
    }

    public HealthIntComponent getHp() {
        return hp;
    }
    public HealthIntComponent getShield() {
        return shield;
    }
}
