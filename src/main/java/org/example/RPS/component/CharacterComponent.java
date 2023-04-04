package org.example.RPS.component;

public class CharacterComponent {
    // Character Abilities:
    // Attack (based on damage)
    // Defense (reduce damage taken by half)
    // Skill (Can be a Heal Skill, Damage Skill, Shield Skill, Gain Mana Skill, etc)
    // Mana is required to use skill | The total mana will be reduced (by 1 or 2 or 3) everytime any character use skill | Mana can only regenerate (maybe 3 points) after every level (and maybe 5 points after every 10 level)


    // Character Stats
    private String name, skillName;
    private int level, hp, shield , damage, evasion;
    // Level is the same as Stage Level
    // Hp = Health Point (When hp is 0, the character die)
    // Shield protect health point | The enemy will damage shield first | Shield will also generate back after each InitRPS | Pierce damage ignore shield
    // Damage is the amount of damage the character will do to the enemy. Do instance, enemy hp = 9, character damage 3. After the character attack the enemy once, the enemy hp = 9-3 = 6.
    // Evasion may not be implements. It is the chance of dodging the attack.

    public CharacterComponent(String NAME, String SKILLNAME, int LEVEL, int HP, int SHIELD , int DAMAGE, int EVASION){
        this.name = NAME;
        this.skillName = SKILLNAME;
        this.level = LEVEL;
        this.hp = HP;
        this.shield = SHIELD;
        this.damage = DAMAGE;
        this.evasion = EVASION;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getShield() {
        return shield;
    }

    public void setShield(int shield) {
        this.shield = shield;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getEvasion() {
        return evasion;
    }

    public void setEvasion(int evasion) {
        this.evasion = evasion;
    }
}
