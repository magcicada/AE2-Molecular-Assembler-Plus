package com.gasai.ccapplied.core.registry;

import com.gasai.ccapplied.CCApplied;
import com.gasai.ccapplied.items.DraconicBlankPatternItem;
import com.gasai.ccapplied.items.DraconicEncodedPatternItem;
import com.gasai.ccapplied.items.ExtremeBlankPatternItem;
import com.gasai.ccapplied.items.ExtremeEncodedPatternItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class CCItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(BuiltInRegistries.ITEM, CCApplied.MODID);

    public static final Supplier<Item> EXTREME_BLANK_PATTERN = ITEMS.register(
            "extreme_blank_pattern",
            () -> new ExtremeBlankPatternItem(new Item.Properties().stacksTo(64))
    );

    public static final Supplier<Item> EXTREME_CRAFTING_PATTERN = ITEMS.register(
            "extreme_crafting_pattern",
            () -> new ExtremeEncodedPatternItem(new Item.Properties().stacksTo(1))
    );

    public static final @Nullable Supplier<Item> DRACONIC_BLANK_PATTERN = CCOptionalMods.isDraconicEvolutionLoaded() ? ITEMS.register(
            "draconic_blank_pattern",
            () -> new DraconicBlankPatternItem(new Item.Properties().stacksTo(64))
    ) : null;

    public static final @Nullable Supplier<Item> DRACONIC_FUSION_PATTERN = CCOptionalMods.isDraconicEvolutionLoaded() ? ITEMS.register(
            "draconic_fusion_pattern",
            () -> new DraconicEncodedPatternItem(new Item.Properties().stacksTo(1))
    ) : null;

    public static final Supplier<Item> EXTREME_PATTERN_TERMINAL = ITEMS.register(
        "extreme_pattern_terminal",
        () -> new appeng.items.parts.PartItem<>(
                new Item.Properties(),
                com.gasai.ccapplied.parts.ExtremePatternEncodingTerminalPart.class,
                com.gasai.ccapplied.parts.ExtremePatternEncodingTerminalPart::new
        )
);

    public static final @Nullable Supplier<Item> DRACONIC_PATTERN_TERMINAL = CCOptionalMods.isDraconicEvolutionLoaded() ? ITEMS.register(
            "draconic_pattern_terminal",
            () -> new appeng.items.parts.PartItem<>(
                    new Item.Properties(),
                    com.gasai.ccapplied.parts.DraconicPatternEncodingTerminalPart.class,
                    com.gasai.ccapplied.parts.DraconicPatternEncodingTerminalPart::new
            )
    ) : null;


    private CCItems() {}
}
