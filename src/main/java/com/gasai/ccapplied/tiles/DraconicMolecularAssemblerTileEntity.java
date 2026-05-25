package com.gasai.ccapplied.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DraconicMolecularAssemblerTileEntity extends ExtremeMolecularAssemblerTileEntity {
    public DraconicMolecularAssemblerTileEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState, AssemblerTier.DRACONIC);
    }
}
