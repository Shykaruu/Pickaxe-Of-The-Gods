package fr.shykaruu.pog;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PickaxeForgeClient {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(
                PickaxeForge.PICKAXE_OF_THE_GODS.get(),
                new ResourceLocation(Constants.MOD_ID, "pog_level"),
                (stack, world, entity, seed) ->
                        ((float) (Utils.getLevel(stack)) / 100)
        ));
    }
}

