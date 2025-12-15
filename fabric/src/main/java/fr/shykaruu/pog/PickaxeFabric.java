package fr.shykaruu.pog;

import fr.shykaruu.pog.items.PickaxeOfTheGods;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static fr.shykaruu.pog.PickaxeCommon.makePogStack;

public class PickaxeFabric implements ModInitializer {
    public static final Item PICKAXE_OF_THE_GODS = Registry.register(
            BuiltInRegistries.ITEM,
            new ResourceLocation(Constants.MOD_ID, "pickaxe_of_the_gods"),
            new PickaxeOfTheGods(new FabricItemSettings().stacksTo(1))
    );

    public static final CreativeModeTab POG_GROUP = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            new ResourceLocation(Constants.MOD_ID, "pog_group"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(PICKAXE_OF_THE_GODS))
                    .title(Component.translatable("itemgroup.pog.pog_group"))
                    .displayItems((context, entries) -> {
                        for(int i = 0; i < Config.MAX_LEVEL + 1; i++) {
                            entries.accept(makePogStack(PickaxeFabric.PICKAXE_OF_THE_GODS, i));
                        }
                    })
                    .build()
    );


    @Override
    public void onInitialize() {
        PickaxeCommon.configPath = FabricLoader.getInstance().getConfigDir().resolve("pog.json");
        Constants.LOG.info("Initializing pickaxe of the god for Fabric !");
        PickaxeCommon.init();
    }
}
