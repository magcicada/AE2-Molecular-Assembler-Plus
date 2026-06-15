package com.gasai.ccapplied.core.registry;

import appeng.api.AECapabilities;
import appeng.api.implementations.blockentities.ICraftingMachine;
import com.gasai.ccapplied.tiles.ExtremeMolecularAssemblerTileEntity;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public final class CCCapabilities {

    private CCCapabilities() {
    }

    public static void register(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, CCBlocks.EXTREME_MOLECULAR_ASSEMBLER_TILE.get(),
                (machine, context) -> machine);
        event.registerBlockEntity(AECapabilities.CRAFTING_MACHINE, CCBlocks.EXTREME_MOLECULAR_ASSEMBLER_TILE.get(),
                (machine, side) -> machine);

        if (CCBlocks.WYVERN_MOLECULAR_ASSEMBLER_TILE != null) {
            event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, CCBlocks.WYVERN_MOLECULAR_ASSEMBLER_TILE.get(),
                    (machine, context) -> machine);
            event.registerBlockEntity(AECapabilities.CRAFTING_MACHINE, CCBlocks.WYVERN_MOLECULAR_ASSEMBLER_TILE.get(),
                    (machine, side) -> machine);
        }
        if (CCBlocks.DRACONIC_MOLECULAR_ASSEMBLER_TILE != null) {
            event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST,
                    CCBlocks.DRACONIC_MOLECULAR_ASSEMBLER_TILE.get(), (machine, context) -> machine);
            event.registerBlockEntity(AECapabilities.CRAFTING_MACHINE,
                    CCBlocks.DRACONIC_MOLECULAR_ASSEMBLER_TILE.get(), (machine, side) -> machine);
        }
        if (CCBlocks.CHAOTIC_MOLECULAR_ASSEMBLER_TILE != null) {
            event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, CCBlocks.CHAOTIC_MOLECULAR_ASSEMBLER_TILE.get(),
                    (machine, context) -> machine);
            event.registerBlockEntity(AECapabilities.CRAFTING_MACHINE, CCBlocks.CHAOTIC_MOLECULAR_ASSEMBLER_TILE.get(),
                    (machine, side) -> machine);
        }
    }
}
