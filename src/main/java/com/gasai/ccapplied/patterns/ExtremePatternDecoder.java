package com.gasai.ccapplied.patterns;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.IPatternDetailsDecoder;
import appeng.api.stacks.AEItemKey;
import com.gasai.ccapplied.core.registry.CCItems;
import com.gasai.ccapplied.core.registry.CCOptionalMods;
import com.gasai.ccapplied.items.DraconicEncodedPatternItem;
import com.gasai.ccapplied.items.ExtremeEncodedPatternItem;

/**
 * Decoder for extreme 9x9 patterns
 */
public class ExtremePatternDecoder implements IPatternDetailsDecoder {

    public static final ExtremePatternDecoder INSTANCE = new ExtremePatternDecoder();

    private ExtremePatternDecoder() {}

    @Override
    public boolean isEncodedPattern(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (stack.getItem() == CCItems.EXTREME_CRAFTING_PATTERN.get()) {
            return true;
        }
        return CCOptionalMods.isDraconicEvolutionLoaded()
                && CCItems.DRACONIC_FUSION_PATTERN != null
                && stack.getItem() == CCItems.DRACONIC_FUSION_PATTERN.get();
    }

    @Override
    @Nullable
    public IPatternDetails decodePattern(AEItemKey what, Level level) {
        return decodePattern(what.toStack(), level);
    }

    @Override
    @Nullable
    public IPatternDetails decodePattern(ItemStack stack, Level level) {
        if (!isEncodedPattern(stack)) {
            return null;
        }

        try {
            if (stack.getItem() == CCItems.EXTREME_CRAFTING_PATTERN.get()) {
                return ExtremeEncodedPatternItem.decodeStack(stack, level);
            }
            if (CCOptionalMods.isDraconicEvolutionLoaded()
                    && CCItems.DRACONIC_FUSION_PATTERN != null
                    && stack.getItem() == CCItems.DRACONIC_FUSION_PATTERN.get()) {
                return DraconicEncodedPatternItem.decodeStack(stack, level);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
