package net.drgmes.dwm.fabric.datagen.common;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.fabric.datagen.common.ars.ArsEntry;
import net.drgmes.dwm.fabric.datagen.common.ars.IEStructures;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryWrapper;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class ModArsProvider implements DataProvider {
    private final FabricDataOutput output;
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture;

    private final HashSet<ArsEntry> entries = new HashSet<>();

    public ModArsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        this.output = output;
        this.registryLookupFuture = registriesFuture;

        this.entries.add(new ArsEntry("imperial", ArsEntry.ETypes.HALLWAYS));
        this.entries.add(new ArsEntry("imperial", ArsEntry.ETypes.STAIRWELLS));
        this.entries.add(new ArsEntry("imperial", ArsEntry.ETypes.ROOMS));

        this.entries.add(new ArsEntry("aquatic", ArsEntry.ETypes.HALLWAYS));
        this.entries.add(new ArsEntry("aquatic", ArsEntry.ETypes.STAIRWELLS));
        this.entries.add(new ArsEntry("aquatic", ArsEntry.ETypes.ROOMS));

        this.entries.add(new ArsEntry("tech", ArsEntry.ETypes.HALLWAYS));
        this.entries.add(new ArsEntry("tech", ArsEntry.ETypes.STAIRWELLS));
        this.entries.add(new ArsEntry("tech", ArsEntry.ETypes.ROOMS));

        CommonHelper.WOODS.forEach((wood) -> {
            this.entries.add(new ArsEntry("wooden/" + wood, ArsEntry.ETypes.HALLWAYS));
            this.entries.add(new ArsEntry("wooden/" + wood, ArsEntry.ETypes.STAIRWELLS));
            this.entries.add(new ArsEntry("wooden/" + wood, ArsEntry.ETypes.ROOMS));
        });

        CommonHelper.ROCKS.forEach((rock) -> {
            this.entries.add(new ArsEntry("rock/" + rock, ArsEntry.ETypes.HALLWAYS));
            this.entries.add(new ArsEntry("rock/" + rock, ArsEntry.ETypes.STAIRWELLS));
            this.entries.add(new ArsEntry("rock/" + rock, ArsEntry.ETypes.ROOMS));
        });

        CommonHelper.COPPERS.forEach((copper) -> {
            this.entries.add(new ArsEntry("copper/" + copper, ArsEntry.ETypes.HALLWAYS));
            this.entries.add(new ArsEntry("copper/" + copper, ArsEntry.ETypes.STAIRWELLS));
            this.entries.add(new ArsEntry("copper/" + copper, ArsEntry.ETypes.ROOMS));
        });

        CommonHelper.COLORS.forEach((color) -> {
            this.entries.add(new ArsEntry("titanium/" + color, ArsEntry.ETypes.HALLWAYS));
            this.entries.add(new ArsEntry("titanium/" + color, ArsEntry.ETypes.STAIRWELLS));
            this.entries.add(new ArsEntry("titanium/" + color, ArsEntry.ETypes.ROOMS));
        });
    }

    @Override
    public String getName() {
        return "ARS Structures";
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return this.registryLookupFuture.thenCompose((lookup) -> {
            final List<CompletableFuture<?>> futures = new ArrayList<>();
            final String rootPath = this.output.resolvePath(DataOutput.OutputType.DATA_PACK).resolve(DWM.MODID).toString().replace("generated", "resources");

            this.entries.forEach((entry) -> {
                AtomicInteger i = new AtomicInteger();
                String prevSubstitutesFolder = null;

                for (IEStructures structureEntry : entry.type().structures) {
                    JsonObject data = new JsonObject();
                    JsonObject substitutes = new JsonObject();

                    String[] substitutesFolderParts = structureEntry.getPath().split("/");
                    String substitutesFolder = String.join("_", Arrays.copyOfRange(substitutesFolderParts, 0, substitutesFolderParts.length - 1));
                    String substitutesPath = String.format("%s/tardis/ars/%s/%s/%s/_substitutes.json", rootPath, entry.type().path, entry.name(), substitutesFolder).replace("//", "/");

                    if (!Objects.equals(prevSubstitutesFolder, substitutesFolder)) i.set(0);
                    prevSubstitutesFolder = substitutesFolder;

                    try (FileInputStream fileInputStream = new FileInputStream(substitutesPath)) {
                        String content = IOUtils.toString(fileInputStream, Charset.defaultCharset());
                        substitutes = JsonParser.parseString(content).getAsJsonObject();
                    } catch (Exception e) {
                        DWM.LOGGER.warn("Error in loading substitutes ({})", substitutesPath);
                    }

                    String structureName = structureEntry.getPath().replace("/", "_");
                    String title = String.format("title.dwm.ars.structure.%s_%s_%s", entry.type().prefix, entry.name().replace("/", "_"), structureName);
                    String structure = String.format("dwm:tardis/ars/%s/_/%s", entry.type().path, structureEntry.getPath());

                    data.addProperty("title", title);
                    data.addProperty("structure", structure);
                    if (!substitutes.isEmpty()) data.add("substitutes", substitutes);

                    String[] pathParts = String.format("tardis/ars/%s/%s/%s", entry.type().path, entry.name(), structureEntry.getPath()).split("/");
                    String pathFolder = String.join("/", Arrays.copyOfRange(pathParts, 0, pathParts.length - 1));
                    String path = String.format("%s/%s-%s", pathFolder, CommonHelper.formatIndexString(i.incrementAndGet()), pathParts[pathParts.length - 1]);

                    futures.add(DataProvider.writeToPath(writer, data, this.output.getResolver(DataOutput.OutputType.DATA_PACK, "").resolveJson(DWM.getIdentifier(path))));
                }
            });

            return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        });
    }
}
