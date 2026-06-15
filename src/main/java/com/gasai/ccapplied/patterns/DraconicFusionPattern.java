package com.gasai.ccapplied.patterns;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import com.gasai.ccapplied.core.registry.CCItems;
import com.gasai.ccapplied.core.registry.CCOptionalMods;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import java.util.Arrays;
import java.util.List;

public class DraconicFusionPattern implements IMolecularAssemblerSupportedPattern {
    public static final int OUTER_SLOTS = 12;
    public static final int TOTAL_INPUT_SLOTS = 13; // 12 outer + 1 catalyst(center)

    public enum FusionTier {
        WYVERN,
        DRACONIC,
        CHAOTIC
    }

    private final AEItemKey definition;
    private final IInput[] inputs;
    private final GenericStack[] outputs;
    private final ItemStack[] inputStacks;
    private final ItemStack outputStack;
    private final FusionTier tier;
    private final long totalEnergy;
    @Nullable
    private final ResourceLocation recipeId;

    public DraconicFusionPattern(
            GenericStack[] sparseInputs,
            GenericStack[] sparseOutputs,
            ItemStack[] inputs,
            ItemStack output,
            FusionTier tier,
            long totalEnergy,
            @Nullable ResourceLocation recipeId) {
        this.definition = CCOptionalMods.isDraconicEvolutionLoaded() && CCItems.DRACONIC_FUSION_PATTERN != null
                ? AEItemKey.of(CCItems.DRACONIC_FUSION_PATTERN.get())
                : null;
        this.inputs = createInputs(sparseInputs);
        this.outputs = sparseOutputs;
        this.inputStacks = inputs;
        this.outputStack = output;
        this.tier = tier;
        this.totalEnergy = totalEnergy;
        this.recipeId = recipeId;
    }

    @Override
    public AEItemKey getDefinition() {
        return definition;
    }

    @Override
    public IInput[] getInputs() {
        return inputs;
    }

    @Override
    public List<GenericStack> getOutputs() {
        return Arrays.asList(outputs);
    }

    public ItemStack[] getInputStacks() {
        return inputStacks;
    }

    public ItemStack getOutputStack() {
        return outputStack;
    }

    public FusionTier getTier() {
        return tier;
    }

    public long getTotalEnergy() {
        return totalEnergy;
    }

    @Nullable
    public ResourceLocation getRecipeId() {
        return recipeId;
    }

    private IInput[] createInputs(GenericStack[] sparseInputs) {
        java.util.List<IInput> inputList = new java.util.ArrayList<>();
        for (GenericStack stack : sparseInputs) {
            if (stack == null || !(stack.what() instanceof AEItemKey)) {
                continue;
            }
            inputList.add(new IInput() {
                @Override
                public GenericStack[] getPossibleInputs() {
                    return new GenericStack[] { stack };
                }

                @Override
                public long getMultiplier() {
                    return 1;
                }

                @Override
                public boolean isValid(AEKey input, Level level) {
                    return input instanceof AEItemKey && input.equals(stack.what());
                }

                @Override
                public @Nullable AEKey getRemainingKey(AEKey template) {
                    return null;
                }
            });
        }
        return inputList.toArray(new IInput[0]);
    }

    @Override
    public ItemStack assemble(Container container, Level level) {
        for (int i = 0; i < Math.min(TOTAL_INPUT_SLOTS, inputStacks.length); i++) {
            ItemStack expected = inputStacks[i];
            if (expected.isEmpty()) {
                continue;
            }
            ItemStack actual = container.getItem(i);
            if (actual.isEmpty() || !ItemStack.isSameItemSameComponents(expected, actual) || actual.getCount() < expected.getCount()) {
                return ItemStack.EMPTY;
            }
        }
        return outputStack.copy();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack in = container.getItem(i);
            if (in.isEmpty()) {
                continue;
            }
            ItemStack expected = i < inputStacks.length ? inputStacks[i] : ItemStack.EMPTY;
            if (expected.isEmpty()) {
                continue;
            }
            int remain = in.getCount() - expected.getCount();
            if (remain > 0) {
                ItemStack out = in.copy();
                out.setCount(remain);
                remaining.set(i, out);
            }
        }
        return remaining;
    }

    @Override
    public boolean isItemValid(int slot, AEItemKey key, Level level) {
        if (slot < 0 || slot >= TOTAL_INPUT_SLOTS) {
            return false;
        }
        if (slot >= inputStacks.length || inputStacks[slot].isEmpty()) {
            return key == null;
        }
        AEItemKey expected = AEItemKey.of(inputStacks[slot]);
        return expected != null && expected.equals(key);
    }

    @Override
    public boolean isSlotEnabled(int slot) {
        return slot >= 0 && slot < TOTAL_INPUT_SLOTS && slot < inputStacks.length && !inputStacks[slot].isEmpty();
    }

    @Override
    public void fillCraftingGrid(KeyCounter[] table, CraftingGridAccessor gridAccessor) {
        for (int i = 0; i < TOTAL_INPUT_SLOTS; i++) {
            gridAccessor.set(i, ItemStack.EMPTY);
        }

        for (int i = 0; i < Math.min(TOTAL_INPUT_SLOTS, inputStacks.length); i++) {
            ItemStack expected = inputStacks[i];
            if (expected.isEmpty()) {
                continue;
            }
            AEItemKey expectedKey = AEItemKey.of(expected);
            if (expectedKey == null) {
                continue;
            }
            boolean filled = false;
            for (KeyCounter counter : table) {
                if (counter == null || counter.isEmpty()) {
                    continue;
                }
                for (var entry : counter) {
                    if (entry.getKey() instanceof AEItemKey key
                            && key.equals(expectedKey)
                            && entry.getLongValue() >= expected.getCount()) {
                        gridAccessor.set(i, key.toStack(expected.getCount()));
                        counter.remove(key, expected.getCount());
                        filled = true;
                        break;
                    }
                }
                if (filled) {
                    break;
                }
            }
        }
    }
}
