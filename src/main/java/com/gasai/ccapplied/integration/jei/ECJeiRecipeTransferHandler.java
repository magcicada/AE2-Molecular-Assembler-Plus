package com.gasai.ccapplied.integration.jei;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.gasai.ccapplied.menus.ExtremePatternEncodingTermMenu;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class ECJeiRecipeTransferHandler implements IRecipeTransferHandler<ExtremePatternEncodingTermMenu, Object> {
    private final RecipeType<Object> type;
    private final IRecipeTransferHandlerHelper helper;

    @SuppressWarnings("unchecked")
    public ECJeiRecipeTransferHandler(RecipeType<?> type, IRecipeTransferHandlerHelper helper) {
        this.type = (RecipeType<Object>) type;
        this.helper = helper;
    }

    @Override
    public Class<ExtremePatternEncodingTermMenu> getContainerClass() {
        return ExtremePatternEncodingTermMenu.class;
    }

    @Override
    public java.util.Optional<net.minecraft.world.inventory.MenuType<ExtremePatternEncodingTermMenu>> getMenuType() {
        return java.util.Optional.of(ExtremePatternEncodingTermMenu.TYPE);
    }

    @Override
    public RecipeType<Object> getRecipeType() {
        return type;
    }

    @Override
    public IRecipeTransferError transferRecipe(ExtremePatternEncodingTermMenu menu, Object recipe, IRecipeSlotsView slots,
            Player player, boolean maxTransfer, boolean doTransfer) {
        if (!doTransfer) return null;

        List<ItemStack> inputs = new ArrayList<>();
        int w;
        int h;

        try {
            Integer width = invokeInt(recipe, "getWidth");
            Integer height = invokeInt(recipe, "getHeight");
            var ingredients = getIngredients(recipe);

            if (width != null && height != null) {
                w = Math.min(9, width);
                h = Math.min(9, height);
                inputs = new ArrayList<>(w * h);
                for (int i = 0; i < w * h; i++) {
                    var ing = i < ingredients.size() ? ingredients.get(i) : Ingredient.EMPTY;
                    inputs.add(firstStack(ing));
                }
            } else {
                for (var ing : ingredients) {
                    if (!ing.isEmpty()) {
                        inputs.add(firstStack(ing));
                    }
                }
                w = 1;
                h = inputs.size();
            }
        } catch (Throwable t) {
            w = 1;
            h = 0;
        }

        menu.requestApplyJeiGrid(w, h, inputs);
        return null;
    }

    private static ItemStack firstStack(Ingredient ingredient) {
        if (ingredient.isEmpty()) {
            return ItemStack.EMPTY;
        }

        var stacks = ingredient.getItems();
        return stacks.length > 0 ? stacks[0].copy() : ItemStack.EMPTY;
    }

    private static List<Ingredient> getIngredients(Object recipe) {
        try {
            Method method = recipe.getClass().getMethod("getIngredients");
            Object value = method.invoke(recipe);
            if (value instanceof List<?> rawList) {
                var ingredients = new ArrayList<Ingredient>(rawList.size());
                for (Object item : rawList) {
                    if (item instanceof Ingredient ingredient) {
                        ingredients.add(ingredient);
                    }
                }
                return ingredients;
            }
        } catch (ReflectiveOperationException ignored) {
        }

        return List.of();
    }

    private static Integer invokeInt(Object target, String methodName) {
        try {
            Method method = target.getClass().getMethod(methodName);
            Object value = method.invoke(target);
            return value instanceof Number number ? number.intValue() : null;
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }
}
