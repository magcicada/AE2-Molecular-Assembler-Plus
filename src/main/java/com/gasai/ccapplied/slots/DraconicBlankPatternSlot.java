package com.gasai.ccapplied.slots;

import appeng.api.inventories.InternalInventory;
import appeng.menu.slot.RestrictedInputSlot;
import com.gasai.ccapplied.core.registry.CCItems;
import com.gasai.ccapplied.core.registry.CCOptionalMods;
import net.minecraft.world.item.ItemStack;

public class DraconicBlankPatternSlot extends RestrictedInputSlot {
    public DraconicBlankPatternSlot(InternalInventory inv, int slotIndex) {
        super(PlacableItemType.BLANK_PATTERN, inv, slotIndex);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return CCOptionalMods.isDraconicEvolutionLoaded()
                && CCItems.DRACONIC_BLANK_PATTERN != null
                && !stack.isEmpty()
                && stack.getItem() == CCItems.DRACONIC_BLANK_PATTERN.get();
    }
}
