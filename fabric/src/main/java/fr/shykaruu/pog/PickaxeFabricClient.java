package fr.shykaruu.pog;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class PickaxeFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ItemProperties.register(
                PickaxeFabric.PICKAXE_OF_THE_GODS,
                new ResourceLocation(Constants.MOD_ID, "pog_level"),
                (stack, world, entity, seed) ->
                        ((float) (Utils.getLevel(stack)) / 100)
        );
    }
}

