package fr.shykaruu.pog;

import java.util.HashMap;

public class Config {
    public static int MAX_LEVEL = 20;

    /// HashMap <Level, Xp to next level>
    public static HashMap<Integer, Float> XP_MAP = new HashMap<>();

    /// HashMap <registry_name, the amount of Xp given by this block >
    public static HashMap<String, Float> BLOCK_XP = new HashMap<>();

    ///HashMap <Level, diging_speed>
    public static HashMap<Integer, Float> SPEED_MAP = new HashMap<>();
}
