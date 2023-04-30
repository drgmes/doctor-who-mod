package net.drgmes.dwm.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.commands.types.TardisDimensionArgumentType;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Optional;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TardisDebugCommand {
    private static final DynamicCommandExceptionType INVALID_TARDIS_EXCEPTION = new DynamicCommandExceptionType((id) -> Text.translatable("argument.dwm.tardis.invalid"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            literal(DWM.MODID).then(
                literal("tardis").then(
                    literal("debug").then(
                        argument("tardisId", new TardisDimensionArgumentType())
                            .requires((source) -> source.hasPermissionLevel(3))
                            .executes(TardisDebugCommand::executeDebug)
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

        MutableText text = Text.empty();
        TardisStateManager tardis = tardisHolder.get();

        text.append(Text.literal("< === " + tardis.getId() + " === >")).append("\n");
        text.append(Text.literal("\n"));
        text.append(Text.literal("Owner: ").append(Text.literal(tardis.getOwner() != null ? tardis.getOwner().toString() : "None").formatted(Formatting.AQUA))).append("\n");
        text.append(Text.literal("Room: ").append(Text.literal(tardis.getConsoleRoom().name).formatted(Formatting.AQUA))).append("\n");
        text.append(Text.literal("\n"));
        text.append(Text.literal("Current Dim: ").append(Text.literal(tardis.getCurrentExteriorDimension().getValue().toString()).formatted(Formatting.AQUA))).append("\n");
        text.append(Text.literal("Next Dim: ").append(Text.literal(tardis.getDestinationExteriorDimension().getValue().toString()).formatted(Formatting.AQUA))).append("\n");
        text.append(Text.literal("Prev Dim: ").append(Text.literal(tardis.getPreviousExteriorDimension().getValue().toString()).formatted(Formatting.AQUA))).append("\n");
        text.append(Text.literal("\n"));
        text.append(Text.literal("Current Pos: ").append(Text.literal(tardis.getCurrentExteriorPosition().toShortString()).formatted(Formatting.AQUA))).append("\n");
        text.append(Text.literal("Next Pos: ").append(Text.literal(tardis.getDestinationExteriorPosition().toShortString()).formatted(Formatting.AQUA))).append("\n");
        text.append(Text.literal("Prev Pos: ").append(Text.literal(tardis.getPreviousExteriorPosition().toShortString()).formatted(Formatting.AQUA))).append("\n");
        text.append(Text.literal("\n"));
        text.append(Text.literal("Current Facing: ").append(Text.literal(tardis.getCurrentExteriorFacing().getName()).formatted(Formatting.AQUA))).append("\n");
        text.append(Text.literal("Next Facing: ").append(Text.literal(tardis.getDestinationExteriorFacing().getName()).formatted(Formatting.AQUA))).append("\n");
        text.append(Text.literal("Prev Facing: ").append(Text.literal(tardis.getPreviousExteriorFacing().getName()).formatted(Formatting.AQUA))).append("\n");
        text.append(Text.literal("\n"));
        text.append(Text.literal("Broken: ").append(Text.literal(String.valueOf(tardis.isBroken())).formatted(Formatting.BLUE))).append("\n");
        text.append(Text.literal("Door Locked: ").append(Text.literal(String.valueOf(tardis.isDoorsLocked())).formatted(Formatting.BLUE))).append("\n");
        text.append(Text.literal("Door Opened: ").append(Text.literal(String.valueOf(tardis.isDoorsOpened())).formatted(Formatting.BLUE))).append("\n");
        text.append(Text.literal("Light Enabled: ").append(Text.literal(String.valueOf(tardis.isLightEnabled())).formatted(Formatting.BLUE))).append("\n");
        text.append(Text.literal("Shields Enabled: ").append(Text.literal(String.valueOf(tardis.isShieldsEnabled())).formatted(Formatting.BLUE))).append("\n");
        text.append(Text.literal("Fuel Harvesting: ").append(Text.literal(String.valueOf(tardis.isFuelHarvesting())).formatted(Formatting.BLUE))).append("\n");
        text.append(Text.literal("Energy Harvesting: ").append(Text.literal(String.valueOf(tardis.isEnergyHarvesting())).formatted(Formatting.BLUE))).append("\n");
        text.append(Text.literal("\n"));
        text.append(Text.literal("Fuel Amount: ").append(Text.literal(tardis.getFuelAmount() + " / " + tardis.getFuelCapacity()).formatted(Formatting.GOLD))).append("\n");
        text.append(Text.literal("Energy Amount: ").append(Text.literal(tardis.getEnergyAmount() + " / " + tardis.getEnergyCapacity()).formatted(Formatting.GOLD))).append("\n");
        text.append(Text.literal("\n"));
        text.append(Text.literal("< ========================================== >"));

        if (player != null) player.sendMessage(text, false);
        else DWM.LOGGER.info(text.getString());

        return Command.SINGLE_SUCCESS;
    }
}
