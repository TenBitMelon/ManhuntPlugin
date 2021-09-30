package me.melonboy10.manhuntplugin.game;

import org.bukkit.Difficulty;
import org.bukkit.WorldType;

import java.util.Random;

/**
 * Contains the setting for the ManhuntGame class
 */
public class ManhuntGameSettings {

    public enum Privacy {PRIVATE, PUBLIC, SPECTATOR_ONLY}

    private long seed = new Random().nextLong();
    private WorldType worldType = WorldType.NORMAL;
    private Difficulty difficulty = Difficulty.NORMAL;
    private int hunterCooldown = 10;
    private Privacy privacy = Privacy.PRIVATE;

    public ManhuntGameSettings() {}

    public ManhuntGameSettings(String seed) {
        setSeed(seed);
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        try {
            setSeed(Long.parseLong(seed));
        } catch (Exception e) {
            setSeed(seed.hashCode());
        }
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public WorldType getWorldType() {
        return worldType;
    }

    public void setWorldType(WorldType worldType) {
        this.worldType = worldType;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getHunterCooldown() {
        return hunterCooldown;
    }

    public void setHunterCooldown(int hunterCooldown) {
        this.hunterCooldown = hunterCooldown;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

}
