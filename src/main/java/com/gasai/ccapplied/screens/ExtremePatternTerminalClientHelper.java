package com.gasai.ccapplied.screens;

import com.gasai.ccapplied.core.registry.CCMenuTypes;
import com.gasai.ccapplied.core.registry.CCOptionalMods;
import com.gasai.ccapplied.menus.DraconicPatternEncodingTermMenu;
import com.gasai.ccapplied.menus.ExtremePatternEncodingTermMenu;
import com.gasai.ccapplied.menus.ExtremeMolecularAssemblerMenu;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.minecraft.client.gui.screens.MenuScreens;

/**
 * Client helper that registers CCApplied screens
 */
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ExtremePatternTerminalClientHelper {

    private ExtremePatternTerminalClientHelper() {}

    @SubscribeEvent
    public static void onRegisterScreens(RegisterMenuScreensEvent event) {
        event.register(CCMenuTypes.EXTREME_PATTERN_TERM.get(),
                (MenuScreens.ScreenConstructor<ExtremePatternEncodingTermMenu, ExtremePatternEncodingTermScreen>) (menu, playerInv, title) -> {
            try {
                var style = appeng.client.gui.style.StyleManager
                        .loadStyleDoc("/screens/ccterminal/extreme_pattern_encoding_terminal.json");
                return new ExtremePatternEncodingTermScreen(menu, playerInv, title, style);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create screen", e);
            }
        });

        if (CCOptionalMods.isDraconicEvolutionLoaded() && CCMenuTypes.DRACONIC_PATTERN_TERM != null) {
            event.register(CCMenuTypes.DRACONIC_PATTERN_TERM.get(),
                    (MenuScreens.ScreenConstructor<DraconicPatternEncodingTermMenu, DraconicPatternEncodingTermScreen>) (menu, playerInv, title) -> {
                try {
                    var style = appeng.client.gui.style.StyleManager
                            .loadStyleDoc("/screens/ccterminal/draconic_pattern_encoding_terminal.json");
                    return new DraconicPatternEncodingTermScreen(menu, playerInv, title, style);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to create screen", e);
                }
            });
        }

        event.register(CCMenuTypes.EXTREME_MOLECULAR_ASSEMBLER.get(),
                (net.minecraft.client.gui.screens.MenuScreens.ScreenConstructor<ExtremeMolecularAssemblerMenu, ExtremeMolecularAssemblerScreen>) (menu, playerInv, title) -> {
            try {
                var style = appeng.client.gui.style.StyleManager
                        .loadStyleDoc("/screens/extreme_molecular_assembler.json");
                return new ExtremeMolecularAssemblerScreen(menu, playerInv, title, style);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create screen", e);
            }
        });
    }
}
