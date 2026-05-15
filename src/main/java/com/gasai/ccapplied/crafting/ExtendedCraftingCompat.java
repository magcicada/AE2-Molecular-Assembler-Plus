package com.gasai.ccapplied.crafting;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

final class ExtendedCraftingCompat {

    private ExtendedCraftingCompat() {
    }

    static @Nullable ExtremeRecipeMatch findRecipe(ItemStack[] craftingGrid, Level level) {
        if (level == null || craftingGrid == null || craftingGrid.length != 81 || isEmptyGrid(craftingGrid)) {
            return null;
        }

        try {
            for (var recipe : level.getRecipeManager().getRecipes()) {
                if (isTableRecipe(recipe) && matches(recipe, craftingGrid)) {
                    return new ExtremeRecipeMatch(
                            recipe.getResultItem(level.registryAccess()).copy(),
                            recipe.getId(),
                            "extendedcrafting");
                }
            }
        } catch (Exception e) {
            com.gasai.ccapplied.CCApplied.LOG.warn("Error matching ExtendedCrafting recipe", e);
        }

        return null;
    }

    private static boolean isTableRecipe(Recipe<?> recipe) {
        for (Class<?> iface : recipe.getClass().getInterfaces()) {
            if (iface.getName().equals("com.blakebr0.extendedcrafting.api.crafting.ITableRecipe")) {
                return true;
            }
        }
        return false;
    }

    private static boolean matches(Recipe<?> recipe, ItemStack[] craftingGrid) {
        try {
            Integer width = invokeInt(recipe, "getWidth");
            Integer height = invokeInt(recipe, "getHeight");

            if (width != null && height != null) {
                return matchesShaped(recipe, craftingGrid, width, height);
            }

            return matchesShapeless(recipe, craftingGrid);
        } catch (Exception e) {
            com.gasai.ccapplied.CCApplied.LOG.warn("Error matching ExtendedCrafting recipe", e);
            return false;
        }
    }

    private static boolean matchesShaped(Recipe<?> recipe, ItemStack[] craftingGrid, int width, int height) {
        if (width > 9 || height > 9) {
            return false;
        }

        var ingredients = recipe.getIngredients();
        for (int startY = 0; startY <= 9 - height; startY++) {
            for (int startX = 0; startX <= 9 - width; startX++) {
                if (matchesRecipeAtPosition(ingredients, craftingGrid, startX, startY, width, height)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean matchesRecipeAtPosition(
            List<Ingredient> ingredients,
            ItemStack[] craftingGrid,
            int startX,
            int startY,
            int width,
            int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int recipeIndex = x + y * width;
                int gridIndex = (startX + x) + (startY + y) * 9;

                var ingredient = recipeIndex < ingredients.size()
                        ? ingredients.get(recipeIndex)
                        : Ingredient.EMPTY;
                var gridStack = craftingGrid[gridIndex];

                if (!ingredient.isEmpty()) {
                    if (gridStack.isEmpty() || !ingredient.test(gridStack)) {
                        return false;
                    }
                } else if (!gridStack.isEmpty()) {
                    return false;
                }
            }
        }

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                int gridIndex = x + y * 9;
                if ((x < startX || x >= startX + width || y < startY || y >= startY + height)
                        && !craftingGrid[gridIndex].isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean matchesShapeless(Recipe<?> recipe, ItemStack[] craftingGrid) {
        var availableItems = new ArrayList<ItemStack>();
        for (ItemStack stack : craftingGrid) {
            if (stack != null && !stack.isEmpty()) {
                availableItems.add(stack.copy());
            }
        }

        for (var ingredient : recipe.getIngredients()) {
            if (ingredient.isEmpty()) {
                continue;
            }

            boolean found = false;
            for (int i = 0; i < availableItems.size(); i++) {
                if (ingredient.test(availableItems.get(i))) {
                    availableItems.remove(i);
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false;
            }
        }

        return availableItems.isEmpty();
    }

    private static @Nullable Integer invokeInt(Object target, String methodName) {
        try {
            Method method = target.getClass().getMethod(methodName);
            Object value = method.invoke(target);
            return value instanceof Number number ? number.intValue() : null;
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    private static boolean isEmptyGrid(ItemStack[] craftingGrid) {
        for (ItemStack stack : craftingGrid) {
            if (stack != null && !stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
