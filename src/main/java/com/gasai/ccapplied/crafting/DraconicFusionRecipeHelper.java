package com.gasai.ccapplied.crafting;

import com.gasai.ccapplied.patterns.DraconicFusionPattern;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class DraconicFusionRecipeHelper {
    private static final ResourceLocation FUSION_RECIPE_TYPE =
            ResourceLocation.fromNamespaceAndPath("draconicevolution", "fusion_crafting");

    private DraconicFusionRecipeHelper() {
    }

    public static @Nullable DraconicFusionRecipeMatch findRecipe(List<ItemStack> outerInputs, ItemStack catalyst, Level level) {
        if (level == null || catalyst.isEmpty() || outerInputs == null || outerInputs.isEmpty() || outerInputs.size() > 12) {
            return null;
        }
        if (!ModList.get().isLoaded("draconicevolution")) {
            return null;
        }

        try {
            for (var recipe : level.getRecipeManager().getRecipes()) {
                var typeId = net.minecraft.core.registries.BuiltInRegistries.RECIPE_TYPE.getKey(recipe.getType());
                if (!FUSION_RECIPE_TYPE.equals(typeId)) {
                    continue;
                }
                var match = tryMatchFusionRecipe(recipe, outerInputs, catalyst, level);
                if (match != null) {
                    return match;
                }
            }
        } catch (Exception e) {
            com.gasai.ccapplied.CCApplied.LOG.warn("Error matching Draconic Evolution fusion recipe", e);
        }

        return null;
    }

    private static @Nullable DraconicFusionRecipeMatch tryMatchFusionRecipe(
            Recipe<?> recipe,
            List<ItemStack> outerInputs,
            ItemStack catalyst,
            Level level) {
        try {
            Ingredient catalystIngredient = extractCatalystIngredient(recipe);
            if (catalystIngredient == null || !catalystIngredient.test(catalyst)) {
                return null;
            }

            List<Ingredient> ingredientDefs = extractIngredients(recipe);
            if (ingredientDefs.isEmpty() || ingredientDefs.size() != outerInputs.size()) {
                return null;
            }

            var remaining = new ArrayList<>(outerInputs.stream().filter(s -> !s.isEmpty()).map(ItemStack::copy).toList());
            if (remaining.size() != ingredientDefs.size()) {
                return null;
            }

            for (Ingredient ingredient : ingredientDefs) {
                boolean found = false;
                for (int i = 0; i < remaining.size(); i++) {
                    if (ingredient.test(remaining.get(i))) {
                        remaining.remove(i);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return null;
                }
            }

            ItemStack result = recipe.getResultItem(level.registryAccess()).copy();
            if (result.isEmpty()) {
                return null;
            }

            var tier = extractTier(recipe);
            long totalEnergy = extractTotalEnergy(recipe);
            return new DraconicFusionRecipeMatch(result, catalyst.copy(), outerInputs, tier, totalEnergy, recipe.getId());
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static List<Ingredient> extractIngredients(Recipe<?> recipe) throws ReflectiveOperationException {
        Method method = findMethod(recipe.getClass(), "fusionIngredients", "getIngredients");
        Object value = method.invoke(recipe);
        if (value instanceof List<?> list) {
            List<Ingredient> out = new ArrayList<>();
            for (Object o : list) {
                if (o instanceof Ingredient ing && !ing.isEmpty()) {
                    out.add(ing);
                } else if (o != null) {
                    Ingredient viaReflection = ingredientFromUnknown(o);
                    if (viaReflection != null && !viaReflection.isEmpty()) {
                        out.add(viaReflection);
                    }
                }
            }
            return out;
        }
        return List.of();
    }

    private static @Nullable Ingredient extractCatalystIngredient(Recipe<?> recipe) throws ReflectiveOperationException {
        Method method = findMethod(recipe.getClass(), "catalyst", "getCatalyst");
        Object value = method.invoke(recipe);
        if (value instanceof Ingredient ing) {
            return ing;
        }
        return ingredientFromUnknown(value);
    }

    private static DraconicFusionPattern.FusionTier extractTier(Recipe<?> recipe) {
        try {
            Method method = findMethod(recipe.getClass(), "getRecipeTier", "recipeTier", "tier", "getTier");
            Object value = method.invoke(recipe);
            if (value != null) {
                String tierName = value.toString().toUpperCase(java.util.Locale.ROOT);
                if (tierName.contains("CHAOTIC")) return DraconicFusionPattern.FusionTier.CHAOTIC;
                if (tierName.contains("DRACONIC")) return DraconicFusionPattern.FusionTier.DRACONIC;
            }
        } catch (Exception ignored) {
        }
        return DraconicFusionPattern.FusionTier.WYVERN;
    }

    private static long extractTotalEnergy(Recipe<?> recipe) {
        try {
            Method method = findMethod(recipe.getClass(), "getEnergyCost", "getTotalEnergy", "totalEnergy", "energy", "total_energy");
            Object value = method.invoke(recipe);
            if (value instanceof Number n) {
                return Math.max(0L, n.longValue());
            }
        } catch (Exception ignored) {
        }
        return 0L;
    }

    private static @Nullable Ingredient ingredientFromUnknown(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Ingredient ing) {
            return ing;
        }
        try {
            Method method = findMethod(value.getClass(), "get", "ingredient");
            Object nested = method.invoke(value);
            if (nested instanceof Ingredient ing) {
                return ing;
            }
        } catch (Exception ignored) {
        }
        return null;
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
