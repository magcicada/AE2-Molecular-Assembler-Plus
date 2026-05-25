package com.gasai.ccapplied.integration.jei;

import com.brandon3055.draconicevolution.api.crafting.IFusionRecipe;
import com.brandon3055.draconicevolution.api.crafting.IngredientStack;
import com.gasai.ccapplied.menus.DraconicPatternEncodingTermMenu;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class DraconicJeiRecipeTransferHandler implements IRecipeTransferHandler<DraconicPatternEncodingTermMenu, IFusionRecipe> {
    private final RecipeType<IFusionRecipe> type;

    public DraconicJeiRecipeTransferHandler(RecipeType<IFusionRecipe> type) {
        this.type = type;
    }

    @Override
    public Class<DraconicPatternEncodingTermMenu> getContainerClass() {
        return DraconicPatternEncodingTermMenu.class;
    }

    @Override
    public java.util.Optional<net.minecraft.world.inventory.MenuType<DraconicPatternEncodingTermMenu>> getMenuType() {
        return java.util.Optional.of(DraconicPatternEncodingTermMenu.TYPE);
    }

    @Override
    public RecipeType<IFusionRecipe> getRecipeType() {
        return type;
    }

    @Override
    public IRecipeTransferError transferRecipe(DraconicPatternEncodingTermMenu menu, IFusionRecipe recipe,
            IRecipeSlotsView slots, Player player, boolean maxTransfer, boolean doTransfer) {
        if (!doTransfer) {
            return null;
        }

        List<ItemStack> outer = new ArrayList<>(DraconicPatternEncodingTermMenu.OUTER_SLOTS);
        for (var fusionIngredient : recipe.fusionIngredients()) {
            if (outer.size() >= DraconicPatternEncodingTermMenu.OUTER_SLOTS) {
                break;
            }
            outer.add(firstStack(fusionIngredient.get()));
        }

        menu.requestApplyJeiFusion(firstStack(recipe.getCatalyst()), outer);
        return null;
    }

    private static ItemStack firstStack(Ingredient ingredient) {
        if (ingredient == null || ingredient.isEmpty()) {
            return ItemStack.EMPTY;
        }

        var stacks = ingredient.getItems();
        if (stacks.length == 0) {
            return ItemStack.EMPTY;
        }

        var stack = stacks[0].copy();
        if (!stack.isEmpty() && ingredient instanceof IngredientStack ingredientStack) {
            stack.setCount(Math.max(1, ingredientStack.getCount()));
        }
        return stack;
    }
}
