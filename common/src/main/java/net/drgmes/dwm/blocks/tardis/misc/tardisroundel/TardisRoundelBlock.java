package net.drgmes.dwm.blocks.tardis.misc.tardisroundel;

import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedBlockWithEntity;
import net.drgmes.dwm.utils.helpers.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.lwjgl.system.NonnullDefault;

public class TardisRoundelBlock extends BaseRotatableWaterloggedBlockWithEntity {
    public static final EnumProperty<Direction.Axis> AXIS = Properties.AXIS;
    public static final BooleanProperty LIT = Properties.LIT;

    public TardisRoundelBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return ModBlockEntities.TARDIS_ROUNDEL.getBlockEntityType().instantiate(blockPos, blockState);
    }

    @NonnullDefault
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return super.getPlacementState(context).with(AXIS, context.getSide().getAxis());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(AXIS);
        builder.add(LIT);
    }

    @Override
    protected BlockState getDefaultBlockState() {
        return super.getDefaultBlockState()
            .with(AXIS, Direction.Axis.Y)
            .with(LIT, false);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return switch (rotation) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (state.get(AXIS)) {
                case X -> state.with(AXIS, Direction.Axis.Z);
                case Z -> state.with(AXIS, Direction.Axis.X);
                default -> state;
            };
            default -> state;
        };
    }

    @Override
    public BlockState onBreak(World world, BlockPos blockPos, BlockState blockState, PlayerEntity player) {
        if (!world.isClient && !player.isCreative()) {
            if (world.getBlockEntity(blockPos) instanceof TardisRoundelBlockEntity tardisRoundelBlockEntity) {
                if (tardisRoundelBlockEntity.blockTemplate != null) {
                    Block block = Registries.BLOCK.get(tardisRoundelBlockEntity.blockTemplate);
                    dropStack(world, blockPos, new ItemStack(block));
                }
            }
        }

        return super.onBreak(world, blockPos, blockState, player);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (hand != Hand.MAIN_HAND) return ActionResult.PASS;
        ItemStack itemStack = player.getStackInHand(Hand.MAIN_HAND);

        if (itemStack.isEmpty()) {
            if (player.isSneaking()) {
                if (!world.isClient && world.getBlockEntity(blockPos) instanceof TardisRoundelBlockEntity tardisRoundelBlockEntity) {
                    if (tardisRoundelBlockEntity.blockTemplate != null) {
                        Block block = Registries.BLOCK.get(tardisRoundelBlockEntity.blockTemplate);
                        if (!player.isCreative()) dropStack(world, blockPos.offset(hit.getSide()), new ItemStack(block));

                        tardisRoundelBlockEntity.blockTemplate = null;
                        tardisRoundelBlockEntity.markDirty();
                        tardisRoundelBlockEntity.sendUpdatePacket();
                    }
                }

                if (blockState.get(LIT)) {
                    world.setBlockState(blockPos, blockState.with(LIT, false), Block.NOTIFY_ALL);
                    if (!player.isCreative()) dropStack(world, blockPos.offset(hit.getSide()), new ItemStack(Items.REDSTONE));
                }
            }

            return ActionResult.SUCCESS;
        }

        if (itemStack.getItem() == Items.WHITE_DYE || itemStack.getItem() == Items.BLACK_DYE) {
            if (!world.isClient && world.getBlockEntity(blockPos) instanceof TardisRoundelBlockEntity tardisRoundelBlockEntity) {
                if (itemStack.getItem() == Items.WHITE_DYE && tardisRoundelBlockEntity.lightMode) return ActionResult.PASS;
                if (itemStack.getItem() == Items.BLACK_DYE && !tardisRoundelBlockEntity.lightMode) return ActionResult.PASS;

                if (!player.isCreative()) {
                    itemStack.decrement(1);
                    player.setStackInHand(Hand.MAIN_HAND, itemStack);
                }

                tardisRoundelBlockEntity.lightMode = itemStack.getItem() == Items.WHITE_DYE;
                tardisRoundelBlockEntity.markDirty();
                tardisRoundelBlockEntity.sendUpdatePacket();
            }

            return ActionResult.SUCCESS;
        }

        if (itemStack.getItem() == Items.REDSTONE) {
            if (blockState.get(LIT)) return ActionResult.PASS;

            if (!world.isClient) {
                if (!player.isCreative()) {
                    itemStack.decrement(1);
                    player.setStackInHand(Hand.MAIN_HAND, itemStack);
                }

                world.setBlockState(blockPos, blockState.with(LIT, true), Block.NOTIFY_ALL);
            }

            return ActionResult.SUCCESS;
        }

        if (itemStack.getItem() instanceof BlockItem blockItem) {
            if (blockItem.getBlock() == this) return ActionResult.PASS;
            if (blockItem.getBlock().getDefaultState().hasBlockEntity()) return ActionResult.PASS;
            if (WorldHelper.checkBlockIsTransparent(blockItem.getBlock().getDefaultState())) return ActionResult.PASS;

            if (!world.isClient && world.getBlockEntity(blockPos) instanceof TardisRoundelBlockEntity tardisRoundelBlockEntity) {
                if (tardisRoundelBlockEntity.blockTemplate != null) {
                    Block block = Registries.BLOCK.get(tardisRoundelBlockEntity.blockTemplate);
                    if (block == blockItem.getBlock()) return ActionResult.PASS;
                    if (!player.isCreative()) dropStack(world, blockPos.offset(hit.getSide()), new ItemStack(block));
                }

                if (!player.isCreative()) {
                    itemStack.decrement(1);
                    player.setStackInHand(Hand.MAIN_HAND, itemStack);
                }

                tardisRoundelBlockEntity.blockTemplate = Registries.BLOCK.getId(blockItem.getBlock());
                tardisRoundelBlockEntity.markDirty();
                tardisRoundelBlockEntity.sendUpdatePacket();
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}
