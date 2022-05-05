package net.drgmes.dwm.common.screwdriver.modes;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public interface IScrewdriverMode {
  public boolean interactWithBlock(Level level, Player player, InteractionHand hand, BlockHitResult hitResult);

  public boolean interactWithEntity(Level level, Player player, InteractionHand hand, EntityHitResult hitResult);
}
