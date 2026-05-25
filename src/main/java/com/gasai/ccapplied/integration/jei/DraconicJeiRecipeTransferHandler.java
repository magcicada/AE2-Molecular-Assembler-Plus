package com.gasai.ccapplied.integration.jei;

import com.gasai.ccapplied.menus.DraconicPatternEncodingTermMenu;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DraconicJeiRecipeTransferHandler implements IRecipeTransferHandler<DraconicPatternEncodingTermMenu, Object> {
    private final RecipeType<Object> type;

    @SuppressWarnings("unchecked")
    public DraconicJeiRecipeTransferHandler(RecipeType<?> type) {
        this.type = (RecipeType<Object>) type;
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
    public RecipeType<Object> getRecipeType() {
        return type;
    }

    @Override
    public IRecipeTransferError transferRecipe(DraconicPatternEncodingTermMenu menu, Object recipe,
            IRecipeSlotsView slots, Player player, boolean maxTransfer, boolean doTransfer) {
        if (!doTransfer) {
            return null;
        }

        List<ItemStack> outer = new ArrayList<>(DraconicPatternEncodingTermMenu.OUTER_SLOTS);
        for (var fusionIngredient : fusionIngredients(recipe)) {
            if (outer.size() >= DraconicPatternEncodingTermMenu.OUTER_SLOTS) {
                break;
            }
            outer.add(firstStack(ingredientFromUnknown(fusionIngredient)));
        }

        menu.requestApplyJeiFusion(firstStack(catalyst(recipe)), outer);
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

    private static List<?> fusionIngredients(Object recipe) {
        try {
            Method method = findMethod(recipe.getClass(), "fusionIngredients");
            Object value = method.invoke(recipe);
            if (value instanceof List<?> list) {
                return list;
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return List.of();
    }

    private static Ingredient catalyst(Object recipe) {
        try {
            Method method = findMethod(recipe.getClass(), "getCatalyst", "catalyst");
            Object value = method.invoke(recipe);
            return ingredientFromUnknown(value);
        } catch (ReflectiveOperationException ignored) {
        }
        return Ingredient.EMPTY;
    }

    private static Ingredient ingredientFromUnknown(Object value) {
        if (value instanceof Ingredient ingredient) {
            return ingredient;
        }
        if (value == null) {
            return Ingredient.EMPTY;
        }
        try {
            Method method = findMethod(value.getClass(), "get", "ingredient");
            Object nested = method.invoke(value);
            if (nested instanceof Ingredient ingredient) {
                return ingredient;
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return Ingredient.EMPTY;
    }

    private static int ingredientCount(Ingredient ingredient) {
        try {
            Method method = findMethod(ingredient.getClass(), "getCount");
            Object value = method.invoke(ingredient);
            if (value instanceof Number number) {
                return Math.max(1, number.intValue());
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return 1;
    }

    private static Method findMethod(Class<?> owner, String... names) throws NoSuchMethodException {
        for (String name : names) {
            try {
                Method method = owner.getMethod(name);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException ignored) {
            }
            try {
                Method method = owner.getDeclaredMethod(name);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException ignored) {
            }
        }
        throw new NoSuchMethodException("No method found on " + owner.getName());
    }
}
