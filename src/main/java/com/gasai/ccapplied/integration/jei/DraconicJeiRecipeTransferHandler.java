package com.gasai.ccapplied.integration.jei;

import com.brandon3055.draconicevolution.api.crafting.IFusionRecipe;
import com.gasai.ccapplied.menus.DraconicPatternEncodingTermMenu;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;

public class DraconicJeiRecipeTransferHandler implements IRecipeTransferHandler<DraconicPatternEncodingTermMenu, RecipeHolder<IFusionRecipe>> {
    private final RecipeType<RecipeHolder<IFusionRecipe>> type;

    public DraconicJeiRecipeTransferHandler(RecipeType<RecipeHolder<IFusionRecipe>> type) {
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
    public RecipeType<RecipeHolder<IFusionRecipe>> getRecipeType() {
        return type;
    }

    @Override
    public IRecipeTransferError transferRecipe(DraconicPatternEncodingTermMenu menu, RecipeHolder<IFusionRecipe> recipeHolder,
            IRecipeSlotsView slots, Player player, boolean maxTransfer, boolean doTransfer) {
        if (!doTransfer) {
            return null;
        }

        List<ItemStack> outer = new ArrayList<>(DraconicPatternEncodingTermMenu.OUTER_SLOTS);
        for (var fusionIngredient : recipeHolder.value().fusionIngredients()) {
            if (outer.size() >= DraconicPatternEncodingTermMenu.OUTER_SLOTS) {
                break;
            }
            outer.add(firstStack(fusionIngredient.get()));
        }

        menu.requestApplyJeiFusion(firstStack(recipeHolder.value().getCatalyst()), outer);
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
        int count = ingredientCount(ingredient);
        if (!stack.isEmpty() && count > 1) {
            stack.setCount(count);
        }
        return stack;
    }

    private static int ingredientCount(Ingredient ingredient) {
        try {
            var method = ingredient.getClass().getMethod("getCount");
            Object value = method.invoke(ingredient);
            if (value instanceof Number number) {
                return Math.max(1, number.intValue());
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return 1;
    }
}
