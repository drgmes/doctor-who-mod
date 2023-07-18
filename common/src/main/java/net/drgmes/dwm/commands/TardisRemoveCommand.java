package net.drgmes.dwm.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.commands.types.TardisDimensionArgumentType;
import net.drgmes.dwm.common.tardis.TardisEnergyManager;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.compat.ImmersivePortals;
import net.drgmes.dwm.setup.ModCompats;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import java.util.Optional;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TardisRemoveCommand {
    private static final DynamicCommandExceptionType INVALID_TARDIS_EXCEPTION = new DynamicCommandExceptionType((id) -> Text.translatable("argument." + DWM.MODID + ".tardis.invalid"));
    private static final DynamicCommandExceptionType PLAYER_PRESENT_EXCEPTION = new DynamicCommandExceptionType((id) -> Text.translatable("argument." + DWM.MODID + ".tardis.player_present"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            literal(DWM.MODID).then(
                literal("tardis").then(
                    literal("remove").then(
                        argument("tardisId", new TardisDimensionArgumentType())
                            .requires((source) -> source.hasPermissionLevel(3))
                            .executes(TardisRemoveCommand::executeDebug)
                    )
                )
            )
        );
    }

    private static int executeDebug(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();

        ServerWorld tardisWorld = TardisDimensionArgumentType.getDimensionArgument(context, "tardisId");
        Optional<TardisStateManager> tardisHolder = TardisStateManager.get(tardisWorld);
        if (tardisHolder.isEmpty()) throw INVALID_TARDIS_EXCEPTION.create(null);
        if (player != null && player.getWorld() == tardisWorld) throw PLAYER_PRESENT_EXCEPTION.create(null);

        TardisStateManager tardis = tardisHolder.get();
        String tardisId = tardis.getId();

        tardis.setOwner(null);
        tardis.setDoorsLockState(true, null);

        TardisEnergyManager.remove(tardisId);
        DimensionHelper.removeWorld(tardisId, context.getSource().getServer());
        if (ModCompats.immersivePortals()) ImmersivePortals.removeTardisPortalsState(tardisId);

        if (player != null) player.sendMessage(DWM.TEXTS.TARDIS_REMOVED.apply(tardisId), false);
        else DWM.LOGGER.info(DWM.TEXTS.TARDIS_REMOVED.apply(tardisId).getString());

        return Command.SINGLE_SUCCESS;
    }
}
