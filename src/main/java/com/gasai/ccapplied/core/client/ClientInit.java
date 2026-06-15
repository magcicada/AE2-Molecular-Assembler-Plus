package com.gasai.ccapplied.core.client;

import appeng.api.util.AEColor;
import com.gasai.ccapplied.core.registry.CCItems;
import com.gasai.ccapplied.core.registry.CCOptionalMods;
import net.minecraft.util.FastColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientInit {
    private ClientInit() {}

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent e) {
        e.enqueueWork(() -> {
            InitRenderTypes.init();
            InitBlockEntityRenderers.init();
        });
        
    }
    
    @SubscribeEvent
    public static void onModelRegistry(ModelEvent.RegisterAdditional event) {
        InitAdditionalModels.init(event);
    }

    @SubscribeEvent
    public static void onItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(
                (stack, tintIndex) -> FastColor.ARGB32.opaque(AEColor.TRANSPARENT.getVariantByTintIndex(tintIndex)),
                CCItems.EXTREME_PATTERN_TERMINAL.get());
        if (CCOptionalMods.isDraconicEvolutionLoaded()) {
            event.register(
                    (stack, tintIndex) -> FastColor.ARGB32.opaque(AEColor.TRANSPARENT.getVariantByTintIndex(tintIndex)),
                    CCItems.DRACONIC_PATTERN_TERMINAL.get());
        }
    }
}

