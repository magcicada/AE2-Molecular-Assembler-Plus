package com.gasai.ccapplied.core.registry;

import com.gasai.ccapplied.CCApplied;
import com.gasai.ccapplied.menus.DraconicPatternEncodingTermMenu;
import com.gasai.ccapplied.menus.ExtremePatternEncodingTermMenu;
import com.gasai.ccapplied.menus.ExtremeMolecularAssemblerMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class CCMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(BuiltInRegistries.MENU, CCApplied.MODID);

    public static final Supplier<MenuType<ExtremePatternEncodingTermMenu>> EXTREME_PATTERN_TERM =
            MENUS.register("extreme_patternterm", () -> ExtremePatternEncodingTermMenu.TYPE);
    public static final @Nullable Supplier<MenuType<DraconicPatternEncodingTermMenu>> DRACONIC_PATTERN_TERM =
            CCOptionalMods.isDraconicEvolutionLoaded()
                    ? MENUS.register("draconic_patternterm", () -> DraconicPatternEncodingTermMenu.TYPE)
                    : null;
    
    public static final Supplier<MenuType<ExtremeMolecularAssemblerMenu>> EXTREME_MOLECULAR_ASSEMBLER =
            MENUS.register("extreme_molecular_assembler", () -> ExtremeMolecularAssemblerMenu.TYPE);

    private CCMenuTypes() {}
}
