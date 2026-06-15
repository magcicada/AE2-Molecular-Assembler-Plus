package com.gasai.ccapplied.core.registry;

import com.gasai.ccapplied.CCApplied;
import com.gasai.ccapplied.blocks.ExtremeMolecularAssemblerBlock;
import appeng.blockentity.AEBaseBlockEntity;
import com.gasai.ccapplied.tiles.ExtremeMolecularAssemblerTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.concurrent.atomic.AtomicReference;

public class CCBlocks {
    
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, CCApplied.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, CCApplied.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, CCApplied.MODID);
    
    public static final Supplier<Block> EXTREME_MOLECULAR_ASSEMBLER = BLOCKS.register(
        "extreme_molecular_assembler",
        () -> new ExtremeMolecularAssemblerBlock(BlockBehaviour.Properties.of()
                .strength(3.5f)
                .requiresCorrectToolForDrops()
                .noOcclusion())
    );

    public static final @Nullable Supplier<Block> WYVERN_MOLECULAR_ASSEMBLER = CCOptionalMods.isDraconicEvolutionLoaded() ? BLOCKS.register(
            "wyvern_molecular_assembler",
            () -> new ExtremeMolecularAssemblerBlock(BlockBehaviour.Properties.of()
                    .strength(3.5f)
                    .requiresCorrectToolForDrops()
                    .noOcclusion())) : null;

    public static final @Nullable Supplier<Block> DRACONIC_MOLECULAR_ASSEMBLER = CCOptionalMods.isDraconicEvolutionLoaded() ? BLOCKS.register(
            "draconic_molecular_assembler",
            () -> new ExtremeMolecularAssemblerBlock(BlockBehaviour.Properties.of()
                    .strength(3.5f)
                    .requiresCorrectToolForDrops()
                    .noOcclusion())) : null;

    public static final @Nullable Supplier<Block> CHAOTIC_MOLECULAR_ASSEMBLER = CCOptionalMods.isDraconicEvolutionLoaded() ? BLOCKS.register(
            "chaotic_molecular_assembler",
            () -> new ExtremeMolecularAssemblerBlock(BlockBehaviour.Properties.of()
                    .strength(3.5f)
                    .requiresCorrectToolForDrops()
                    .noOcclusion())) : null;
    
    public static final Supplier<Item> EXTREME_MOLECULAR_ASSEMBLER_ITEM = ITEMS.register(
        "extreme_molecular_assembler",
        () -> new BlockItem(EXTREME_MOLECULAR_ASSEMBLER.get(), new Item.Properties())
    );

    public static final @Nullable Supplier<Item> WYVERN_MOLECULAR_ASSEMBLER_ITEM = CCOptionalMods.isDraconicEvolutionLoaded() ? ITEMS.register(
            "wyvern_molecular_assembler",
            () -> new BlockItem(WYVERN_MOLECULAR_ASSEMBLER.get(), new Item.Properties())) : null;
    public static final @Nullable Supplier<Item> DRACONIC_MOLECULAR_ASSEMBLER_ITEM = CCOptionalMods.isDraconicEvolutionLoaded() ? ITEMS.register(
            "draconic_molecular_assembler",
            () -> new BlockItem(DRACONIC_MOLECULAR_ASSEMBLER.get(), new Item.Properties())) : null;
    public static final @Nullable Supplier<Item> CHAOTIC_MOLECULAR_ASSEMBLER_ITEM = CCOptionalMods.isDraconicEvolutionLoaded() ? ITEMS.register(
            "chaotic_molecular_assembler",
            () -> new BlockItem(CHAOTIC_MOLECULAR_ASSEMBLER.get(), new Item.Properties())) : null;
    
    public static final Supplier<BlockEntityType<ExtremeMolecularAssemblerTileEntity>> EXTREME_MOLECULAR_ASSEMBLER_TILE = BLOCK_ENTITIES.register(
        "extreme_molecular_assembler",
        () -> {
            var typeHolder = new AtomicReference<BlockEntityType<ExtremeMolecularAssemblerTileEntity>>();
            BlockEntityType.BlockEntitySupplier<ExtremeMolecularAssemblerTileEntity> supplier = (BlockPos pos, BlockState state) -> new ExtremeMolecularAssemblerTileEntity(typeHolder.get(), pos, state);
            var type = BlockEntityType.Builder.of(supplier, EXTREME_MOLECULAR_ASSEMBLER.get()).build(null);
            typeHolder.set(type);
            AEBaseBlockEntity.registerBlockEntityItem(type, EXTREME_MOLECULAR_ASSEMBLER_ITEM.get());
            
            ((ExtremeMolecularAssemblerBlock) EXTREME_MOLECULAR_ASSEMBLER.get()).setBlockEntity(ExtremeMolecularAssemblerTileEntity.class, type, null, null);
            
            return type;
        }
    );

    public static final @Nullable Supplier<BlockEntityType<ExtremeMolecularAssemblerTileEntity>> WYVERN_MOLECULAR_ASSEMBLER_TILE = CCOptionalMods.isDraconicEvolutionLoaded() ? BLOCK_ENTITIES.register(
            "wyvern_molecular_assembler",
            () -> {
                var typeHolder = new AtomicReference<BlockEntityType<ExtremeMolecularAssemblerTileEntity>>();
                BlockEntityType.BlockEntitySupplier<ExtremeMolecularAssemblerTileEntity> supplier = (BlockPos pos, BlockState state) -> new ExtremeMolecularAssemblerTileEntity(typeHolder.get(), pos, state);
                var type = BlockEntityType.Builder.of(supplier, WYVERN_MOLECULAR_ASSEMBLER.get()).build(null);
                typeHolder.set(type);
                AEBaseBlockEntity.registerBlockEntityItem(type, WYVERN_MOLECULAR_ASSEMBLER_ITEM.get());
                ((ExtremeMolecularAssemblerBlock) WYVERN_MOLECULAR_ASSEMBLER.get()).setBlockEntity(ExtremeMolecularAssemblerTileEntity.class, type, null, null);
                return type;
            }) : null;

    public static final @Nullable Supplier<BlockEntityType<ExtremeMolecularAssemblerTileEntity>> DRACONIC_MOLECULAR_ASSEMBLER_TILE = CCOptionalMods.isDraconicEvolutionLoaded() ? BLOCK_ENTITIES.register(
            "draconic_molecular_assembler",
            () -> {
                var typeHolder = new AtomicReference<BlockEntityType<ExtremeMolecularAssemblerTileEntity>>();
                BlockEntityType.BlockEntitySupplier<ExtremeMolecularAssemblerTileEntity> supplier = (BlockPos pos, BlockState state) -> new ExtremeMolecularAssemblerTileEntity(typeHolder.get(), pos, state);
                var type = BlockEntityType.Builder.of(supplier, DRACONIC_MOLECULAR_ASSEMBLER.get()).build(null);
                typeHolder.set(type);
                AEBaseBlockEntity.registerBlockEntityItem(type, DRACONIC_MOLECULAR_ASSEMBLER_ITEM.get());
                ((ExtremeMolecularAssemblerBlock) DRACONIC_MOLECULAR_ASSEMBLER.get()).setBlockEntity(ExtremeMolecularAssemblerTileEntity.class, type, null, null);
                return type;
            }) : null;

    public static final @Nullable Supplier<BlockEntityType<ExtremeMolecularAssemblerTileEntity>> CHAOTIC_MOLECULAR_ASSEMBLER_TILE = CCOptionalMods.isDraconicEvolutionLoaded() ? BLOCK_ENTITIES.register(
            "chaotic_molecular_assembler",
            () -> {
                var typeHolder = new AtomicReference<BlockEntityType<ExtremeMolecularAssemblerTileEntity>>();
                BlockEntityType.BlockEntitySupplier<ExtremeMolecularAssemblerTileEntity> supplier = (BlockPos pos, BlockState state) -> new ExtremeMolecularAssemblerTileEntity(typeHolder.get(), pos, state);
                var type = BlockEntityType.Builder.of(supplier, CHAOTIC_MOLECULAR_ASSEMBLER.get()).build(null);
                typeHolder.set(type);
                AEBaseBlockEntity.registerBlockEntityItem(type, CHAOTIC_MOLECULAR_ASSEMBLER_ITEM.get());
                ((ExtremeMolecularAssemblerBlock) CHAOTIC_MOLECULAR_ASSEMBLER.get()).setBlockEntity(ExtremeMolecularAssemblerTileEntity.class, type, null, null);
                return type;
            }) : null;
}
