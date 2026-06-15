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
import net.minecraft.world.item.crafting.RecipeHolder;

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
            Object actualRecipe = unwrapRecipe(recipe);
            Integer width = invokeInt(actualRecipe, "getWidth", "width", "recipeWidth");
            Integer height = invokeInt(actualRecipe, "getHeight", "height", "recipeHeight");
            var ingredients = getIngredients(actualRecipe);

            if (width != null && height != null) {
                w = Math.min(9, width);
                h = Math.min(9, height);
                inputs = new ArrayList<>(w * h);
                for (int i = 0; i < w * h; i++) {
                    var ing = i < ingredients.size() ? ingredients.get(i) : Ingredient.EMPTY;
                    inputs.add(firstStack(ing));
                }
            } else {
                int square = (int) Math.round(Math.sqrt(ingredients.size()));
                if (square > 1 && square * square == ingredients.size() && square <= 9) {
                    w = square;
                    h = square;
                    inputs = new ArrayList<>(w * h);
                    for (int i = 0; i < w * h; i++) {
                        var ing = i < ingredients.size() ? ingredients.get(i) : Ingredient.EMPTY;
                        inputs.add(firstStack(ing));
                    }
                    menu.requestApplyJeiGrid(w, h, inputs);
                    return null;
                }
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

    private static Object unwrapRecipe(Object recipe) {
        if (recipe instanceof RecipeHolder<?> holder) {
            return holder.value();
        }
        return recipe;
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
            Method method = findMethod(recipe.getClass(), "getIngredients", "ingredients");
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

    private static Integer invokeInt(Object target, String... methodNames) {
        for (String methodName : methodNames) {
            try {
                Method method = findMethod(target.getClass(), methodName);
                Object value = method.invoke(target);
                return value instanceof Number number ? number.intValue() : null;
            } catch (ReflectiveOperationException ignored) {
            }
        }
        return null;
    }

    private static Method findMethod(Class<?> owner, String... names) throws NoSuchMethodException {
        Class<?> current = owner;
        while (current != null) {
            for (String name : names) {
                try {
                    Method method = current.getMethod(name);
                    method.setAccessible(true);
                    return method;
                } catch (NoSuchMethodException ignored) {
                }
                try {
                    Method method = current.getDeclaredMethod(name);
                    method.setAccessible(true);
                    return method;
                } catch (NoSuchMethodException ignored) {
                }
            }
            current = current.getSuperclass();
        }
        throw new NoSuchMethodException("No recipe method found on " + owner.getName());
    }
}
