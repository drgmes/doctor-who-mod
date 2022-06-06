package net.drgmes.dwm.blocks.tardis.others.tardisroomcreator;

import net.drgmes.dwm.blocks.tardis.others.tardisroomcreator.screens.TardisRoomCreatorScreen;
import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class TardisRoomCreatorBlock extends BaseRotatableWaterloggedBlock {
    public TardisRoomCreatorBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (player.isSpectator()) return InteractionResult.PASS;
        if (!level.isClientSide) return InteractionResult.SUCCESS;

        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(new TardisRoomCreatorScreen(blockPos));

        return InteractionResult.SUCCESS;
    }
}
