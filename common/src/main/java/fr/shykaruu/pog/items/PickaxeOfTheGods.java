package fr.shykaruu.pog.items;

import fr.shykaruu.pog.Config;
import fr.shykaruu.pog.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static fr.shykaruu.pog.Utils.*;

public class PickaxeOfTheGods extends PickaxeItem {

    public PickaxeOfTheGods(Properties properties) {
        super(Tiers.DIAMOND, 1, -2.8f,
                properties.durability(-1)); // Unbreakable
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack stack, @NotNull BlockState state) {
        return Config.SPEED_MAP.getOrDefault(Utils.getLevel(stack), 1.0f);
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack stack, @NotNull Level world, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull LivingEntity entity) {
        if (entity instanceof Player player) {
            String key = BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
            if (Config.BLOCK_XP.containsKey(key)) {
                float gainedXp = Config.BLOCK_XP.get(key);
                addXp(stack, gainedXp, player);
            }
        }
        return super.mineBlock(stack, world, state, pos, entity);
    }

    public static void addXp(ItemStack stack, float xp, Player player) {
        CompoundTag tag = stack.getOrCreateTag();
        int currentLevel = tag.getInt(LEVEL_TAG);
        float currentXp = tag.getFloat(XP_TAG);

        // Stop XP gain if max level reached
        if (currentLevel >= Config.MAX_LEVEL) {
            currentXp = 0;
            tag.putFloat(XP_TAG, currentXp);
            stack.setTag(tag);
            updateItemName(stack); // update name to max level
            return;
        }

        float xpPerLevel = Config.XP_MAP.get(currentLevel);
        float newXp = currentXp + xp;
        int newLevel = currentLevel;


        // Level-up check
        while (newXp >= xpPerLevel && newLevel < Config.MAX_LEVEL) {
            newXp -= xpPerLevel;
            newLevel++;

            if (!player.level().isClientSide) {
                // Level-up message
                player.displayClientMessage(
                        Component.literal("§6Pickaxe Of The Gods: §fLevel ")
                                .append(Component.literal(String.valueOf(newLevel)).withStyle(ChatFormatting.GOLD)),
                        true
                );

                // Play level-up sound
                player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, 1.0f);
            }
        }

        tag.putFloat(XP_TAG, newXp);
        tag.putInt(LEVEL_TAG, newLevel);
        stack.setTag(tag);

        updateItemName(stack);

        // Client-side XP message
        if (player.level().isClientSide && newLevel < Config.MAX_LEVEL) {
            player.sendSystemMessage(
                    Component.literal("§a+" + Math.round(xp) + " XP"));
        }
    }

    private static void updateItemName(ItemStack stack) {
        stack.setHoverName(Component.literal("§6Pickaxe Of The Gods")
                .append(Component.literal(" §7[").withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal("Lvl " + getLevel(stack)).withStyle(ChatFormatting.GOLD))
                .append(Component.literal("]").withStyle(ChatFormatting.DARK_GRAY)));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);

        int level = getLevel(stack);
        if (level >= Config.MAX_LEVEL) return; // Mask xp if max level

        float xp = getXp(stack);
        float xpForLevel = Config.XP_MAP.getOrDefault(level, 1.0f);
        int percent = Math.min(Math.round((xp / xpForLevel) * 100), 100);

        tooltip.add(Component.literal("§eXP: " + Math.round(xp) + " / " + Math.round(xpForLevel) + " (" + percent + "%)").withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return getLevel(stack) >= Config.MAX_LEVEL;
    }
}
