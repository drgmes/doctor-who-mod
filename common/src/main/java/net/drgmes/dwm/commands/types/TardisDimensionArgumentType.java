package net.drgmes.dwm.commands.types;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.drgmes.dwm.DWM;
import net.minecraft.command.CommandSource;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class TardisDimensionArgumentType implements ArgumentType<Identifier> {
    private static final DynamicCommandExceptionType INVALID_DIMENSION_EXCEPTION = new DynamicCommandExceptionType((id) -> Text.translatable("argument.dimension.invalid", id));

    @Override
    public Identifier parse(StringReader reader) throws CommandSyntaxException {
        return Identifier.fromCommandInput(reader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSource) {
            Stream<Identifier> worldIds = ((CommandSource) context.getSource()).getWorldKeys().stream().map(RegistryKey::getValue);
            return CommandSource.suggestIdentifiers(worldIds.filter((value) -> value.getNamespace().equals(DWM.MODID)), builder);
        }

        return Suggestions.empty();
    }

    public static ServerWorld getDimensionArgument(CommandContext<ServerCommandSource> context, String name) throws CommandSyntaxException {
        Identifier id = context.getArgument(name, Identifier.class);
        ServerWorld world = context.getSource().getServer().getWorld(RegistryKey.of(RegistryKeys.WORLD, id));
        if (world == null) throw INVALID_DIMENSION_EXCEPTION.create(id);
        return world;
    }
}
