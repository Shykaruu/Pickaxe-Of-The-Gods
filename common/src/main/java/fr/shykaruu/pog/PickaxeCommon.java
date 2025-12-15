package fr.shykaruu.pog;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class PickaxeCommon {
    static Path configPath;
    public static void init() {
        /// Config phases
        if(Files.exists(configPath)) {
            try (FileReader reader = new FileReader(String.valueOf(configPath))) {
                JsonObject object = JsonParser.parseReader(reader).getAsJsonObject();
                if(object == null) {
                    initDefaultConfig();
                } else {
                    /// Modular properties system
                    if(object.has("maxLevel")) {
                        Config.MAX_LEVEL = object.get("maxLevel").getAsInt();
                    } else {
                        object.addProperty("maxLevel", 20);
                        saveConfig(object);
                    }
                    if(object.has("maxXp")) {
                        Config.XP_MAP.clear();

                        for (Map.Entry<String, JsonElement> entry : object.getAsJsonObject(
                                "maxXp").entrySet()) {
                            Config.XP_MAP.put(Integer.parseInt(entry.getKey()),
                                    entry.getValue().getAsFloat());
                        }
                    } else {
                        for (Map.Entry<Integer, Float> entry : Config.XP_MAP.entrySet()) {
                            object.getAsJsonObject("maxXp").addProperty(
                                    String.valueOf(entry.getKey()), // key
                                    entry.getValue()                 // value
                            );
                        }
                        setDefaultValues(false, true);
                        saveConfig(object);
                    }
                    if(object.has("speed_per_lvl")) {
                        Config.SPEED_MAP.clear();

                        for (Map.Entry<String, JsonElement> entry : object.getAsJsonObject(
                                "speed_per_lvl").entrySet()) {
                            Config.SPEED_MAP.put(Integer.parseInt(entry.getKey()),
                                    entry.getValue().getAsFloat());
                        }
                    } else {
                        for (Map.Entry<Integer, Float> entry : Config.SPEED_MAP.entrySet()) {
                            object.getAsJsonObject("speed_per_lvl").addProperty(
                                    String.valueOf(entry.getKey()), // key
                                    entry.getValue()                 // value
                            );
                        }
                        generateDefaultSpeedMap();
                        saveConfig(object);
                    }
                    if(object.has("xp_blocks")) {
                        Config.BLOCK_XP.clear();
                        for (Map.Entry<String, JsonElement> entry : object.getAsJsonObject(
                                "xp_blocks").entrySet()) {
                            Config.BLOCK_XP.put(entry.getKey(),
                                    entry.getValue().getAsFloat());
                        }
                    } else {
                        putDefaultBlocks();
                        for (Map.Entry<String, Float> entry : Config.BLOCK_XP.entrySet()) {
                            object.getAsJsonObject("xp_blocks").addProperty(
                                    entry.getKey(),                  // key
                                    entry.getValue()                 // value
                            );
                        }
                        saveConfig(object);
                    }
                }

                Constants.LOG.info("Config successfully processed !");


            } catch (IOException exception) {
                Constants.LOG.error("Failed to configure POG !", exception);
            }
        } else {
            try {
                Files.createFile(configPath);
                initDefaultConfig();
            } catch (IOException exception) {
                Constants.LOG.error("Failed to create POG config file !", exception);
            }
        }
    }
    private static void initDefaultConfig() {
        /// Default config values set
        setDefaultValues(true, true);
        putDefaultBlocks(); //Set default XP blocks values
        generateDefaultSpeedMap();
        /// Default config gen
        JsonObject object = new JsonObject();
        object.addProperty("maxLevel", Config.MAX_LEVEL);

        JsonObject xpJson = new JsonObject();
        JsonObject speedJson = new JsonObject();
        JsonObject blockXpJson = new JsonObject();

        //XP_MAP
        for (Map.Entry<Integer, Float> entry : Config.XP_MAP.entrySet()) {
            xpJson.addProperty(String.valueOf(entry.getKey()), entry.getValue());
        }

        //SPEED_MAP
        for (Map.Entry<Integer, Float> entry : Config.SPEED_MAP.entrySet()) {
            speedJson.addProperty(String.valueOf(entry.getKey()), entry.getValue());
        }

        //BLOCK_XP
        for (Map.Entry<String, Float> entry : Config.BLOCK_XP.entrySet()) {
            blockXpJson.addProperty(entry.getKey(), entry.getValue());
        }

        object.add("maxXp", xpJson);
        object.add("speed_per_lvl", speedJson);
        object.add("xp_blocks", blockXpJson);

        saveConfig(object);
    }
    private static void saveConfig(JsonObject object) {
        try(FileWriter writer = new FileWriter(String.valueOf(configPath))) {
            new GsonBuilder()
                    .setPrettyPrinting()
                    .create().toJson(object, writer);
        } catch (Exception exception) {
            Constants.LOG.error("Error while saving POG config", exception);
        }
    }
    private static void setDefaultValues(boolean max_level, boolean max_xp) {
        if(max_level) {
            Config.MAX_LEVEL = 20;
        }
        if(max_xp) {
            Config.XP_MAP = new HashMap<>();
            for (int level = 0; level <= Config.MAX_LEVEL; level++) {
                Config.XP_MAP.put(level,
                        (100.0F + ((level - 1) * (level - 1) * 12.0F))); //XP
            }
        }
    }
    /**
     * This method is used to put a default set of block that add xp to the pickaxe
     */
    public static void putDefaultBlocks() {
        // --- BASICS ---
        Config.BLOCK_XP.put("minecraft:stone", 1.0f);
        Config.BLOCK_XP.put("minecraft:cobblestone", 0.5f);
        Config.BLOCK_XP.put("minecraft:dirt", 0.2f);
        Config.BLOCK_XP.put("minecraft:grass_block", 0.3f);
        Config.BLOCK_XP.put("minecraft:sand", 0.3f);
        Config.BLOCK_XP.put("minecraft:gravel", 0.4f);

        // --- WOOD ---
        Config.BLOCK_XP.put("minecraft:oak_log", 1.5f);
        Config.BLOCK_XP.put("minecraft:spruce_log", 1.5f);
        Config.BLOCK_XP.put("minecraft:birch_log", 1.5f);
        Config.BLOCK_XP.put("minecraft:jungle_log", 1.8f);
        Config.BLOCK_XP.put("minecraft:dark_oak_log", 2.0f);
        Config.BLOCK_XP.put("minecraft:acacia_log", 1.7f);

        // --- ORE ---
        Config.BLOCK_XP.put("minecraft:coal_ore", 3.0f);
        Config.BLOCK_XP.put("minecraft:copper_ore", 3.5f);
        Config.BLOCK_XP.put("minecraft:iron_ore", 5.0f);
        Config.BLOCK_XP.put("minecraft:gold_ore", 8.0f);
        Config.BLOCK_XP.put("minecraft:redstone_ore", 6.0f);
        Config.BLOCK_XP.put("minecraft:lapis_ore", 7.0f);
        Config.BLOCK_XP.put("minecraft:emerald_ore", 15.0f);
        Config.BLOCK_XP.put("minecraft:diamond_ore", 20.0f);
        Config.BLOCK_XP.put("minecraft:deepslate_diamond_ore", 25.0f);

        // --- NETHER ---
        Config.BLOCK_XP.put("minecraft:netherrack", 1.0f);
        Config.BLOCK_XP.put("minecraft:nether_quartz_ore", 6.0f);
        Config.BLOCK_XP.put("minecraft:nether_gold_ore", 8.0f);
        Config.BLOCK_XP.put("minecraft:ancient_debris", 40.0f);

        // --- MISCELLANEOUS ---
        Config.BLOCK_XP.put("minecraft:obsidian", 10.0f);
        Config.BLOCK_XP.put("minecraft:crying_obsidian", 15.0f);
        Config.BLOCK_XP.put("minecraft:spawner", 100.0f);

        // --- END ---
        Config.BLOCK_XP.put("minecraft:end_stone", 2.0f);

    }
    public static ItemStack makePogStack(Item instance, int level) {
        ItemStack stack = new ItemStack(instance);
        stack.getOrCreateTag().putInt("pog_level", level);

        stack.setHoverName(Component.literal("§6Pickaxe Of The Gods")
                .append(Component.literal(" §7[").withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal("Lvl " + level).withStyle(ChatFormatting.GOLD))
                .append(Component.literal("]").withStyle(ChatFormatting.DARK_GRAY)));
        return stack;
    }
    public static void generateDefaultSpeedMap() {
        Config.SPEED_MAP.clear();

        float minSpeed = 6.0f;   // Like iron tools
        float maxSpeed = 55.0f;  // ~ netherite eff 5

        for (int level = 0; level <= 20; level++) {
            float t = (float) level / 20.0f; // 0 → 1
            float speed = minSpeed + (t * t) * (maxSpeed - minSpeed);

            Config.SPEED_MAP.put(level, speed);
        }
    }
}