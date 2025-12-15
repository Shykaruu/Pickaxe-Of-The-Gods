package fr.shykaruu.pog;

import net.minecraft.world.item.ItemStack;

public class Utils {
    public static final String XP_TAG = "pog_xp";
    public static final String LEVEL_TAG = "pog_level";

    /**
     * Get lvl
     */
    public static int getLevel(ItemStack stack) {
        return stack.getOrCreateTag().getInt(LEVEL_TAG);
    }

    /**
     * Get Xp
     */
    public static float getXp(ItemStack stack) {
        return stack.getOrCreateTag().getFloat(XP_TAG);
    }
}
