package fr.shykaruu.pog;

import fr.shykaruu.pog.items.PickaxeOfTheGods;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static fr.shykaruu.pog.PickaxeCommon.makePogStack;

@Mod(Constants.MOD_ID)
public class PickaxeForge {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);

    public static final RegistryObject<Item> PICKAXE_OF_THE_GODS = ITEMS.register(
            "pickaxe_of_the_gods",
            () -> new PickaxeOfTheGods(new Item.Properties().stacksTo(1)));

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);
    public static final RegistryObject<CreativeModeTab> POG_GROUP =
            CREATIVE_MODE_TABS.register("pog_group", () ->
                    CreativeModeTab.builder()
                            .icon(() -> new ItemStack(PICKAXE_OF_THE_GODS.get()))
                            .title(Component.translatable("itemgroup.pog.pog_group"))
                            .displayItems((parameters, output) -> {
                                for (int i = 0; i <= Config.MAX_LEVEL; i++) {
                                    output.accept(makePogStack(PICKAXE_OF_THE_GODS.get(), i));
                                }
                            })
                            .build()
            );

    public PickaxeForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        CREATIVE_MODE_TABS.register(bus);
        ITEMS.register(bus);
        PickaxeCommon.configPath = FMLPaths.CONFIGDIR.get().resolve("pog.json");
        Constants.LOG.info("Initializing pickaxe of the god for Forge !");
        PickaxeCommon.init();
    }
}