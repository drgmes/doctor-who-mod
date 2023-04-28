package net.drgmes.dwm.utils.builders;

import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class BlockEntityBuilder<C extends BlockEntity> {
    public final Supplier<BlockEntityType<C>> blockEntityType;
    private final String name;

    public BlockEntityBuilder(String name, Supplier<BlockEntityType<C>> blockEntityTypeFactory) {
        this.name = name;
        this.blockEntityType = Registration.registerBlockEntity(name, blockEntityTypeFactory);

        ModBlockEntities.BLOCK_ENTITY_BUILDERS.add(this);
    }

    public BlockEntityBuilder(String name, BlockEntityType.BlockEntityFactory<C> blockEntityFactory, Supplier<Block> blockSupplier) {
        this(name, () -> {
            BlockEntityType.Builder<C> builder = BlockEntityType.Builder.create(blockEntityFactory, blockSupplier.get());
            return builder.build(null);
        });
    }

    public String getName() {
        return this.name;
    }

    public BlockEntityType<C> getBlockEntityType() {
        return this.blockEntityType.get();
    }
}
