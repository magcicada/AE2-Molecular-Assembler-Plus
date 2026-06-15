package com.gasai.ccapplied.patterns;

import com.gasai.ccapplied.CCApplied;
import appeng.api.crafting.PatternDetailsHelper;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Registration of decoder for extreme patterns
 */
@EventBusSubscriber(modid = CCApplied.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ExtremePatternRegistration {

    private ExtremePatternRegistration() {}

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        
        event.enqueueWork(() -> {
            try {
                PatternDetailsHelper.registerDecoder(ExtremePatternDecoder.INSTANCE);
            } catch (Exception e) {
            }
        });
    }
}

