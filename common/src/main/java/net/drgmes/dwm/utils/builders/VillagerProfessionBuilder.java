package net.drgmes.dwm.utils.builders;

import com.google.common.collect.ImmutableSet;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModVillagerProfessions;
import net.drgmes.dwm.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class VillagerProfessionBuilder {
    public final Supplier<PointOfInterestType> poiType;
    public final Supplier<VillagerProfession> villagerProfession;

    private final String name;
    private final RegistryKey<PointOfInterestType> poiKey;

    public VillagerProfessionBuilder(String name, int ticketCount, int searchDistance, SoundEvent workSound, Supplier<ImmutableSet<Block>> blocksSupplier, Consumer<VillagerProfession> villagerProfessionConsumer) {
        this.name = name;
        this.poiKey = RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, DWM.getIdentifier(name));
        this.poiType = registerPoiType(name, ticketCount, searchDistance, () -> getStatesOfBlocks(blocksSupplier.get()));
        this.villagerProfession = Registration.registerVillagerProfession(name, () -> {
            VillagerProfession profession = new VillagerProfession(name, (entry) -> entry.value().equals(this.getPoiType()), (entry) -> entry.value().equals(this.getPoiType()), ImmutableSet.of(), ImmutableSet.of(), workSound);
            villagerProfessionConsumer.accept(profession);
            return profession;
        });

        ModVillagerProfessions.VILLAGER_PROFESSION_BUILDERS.add(this);
    }

    public VillagerProfessionBuilder(String name, int ticketCount, int searchDistance, SoundEvent workSound, Supplier<ImmutableSet<Block>> blocksSupplier) {
        this(name, ticketCount, searchDistance, workSound, blocksSupplier, (profession) -> {});
    }

    @ExpectPlatform
    public static Supplier<PointOfInterestType> registerPoiType(String name, int ticketCount, int searchDistance, Supplier<ImmutableSet<BlockState>> blockStatesSupplier) {
        throw new AssertionError();
    }

    public String getName() {
        return this.name;
    }

    public RegistryKey<PointOfInterestType> getPoiKey() {
        return this.poiKey;
    }

    public PointOfInterestType getPoiType() {
        return this.poiType.get();
    }

    public VillagerProfession getVillagerProfession() {
        return this.villagerProfession.get();
    }

    private static ImmutableSet<BlockState> getStatesOfBlocks(Set<Block> blocks) {
        ImmutableSet.Builder<BlockState> builder = ImmutableSet.builder();
        blocks.forEach((bs) -> builder.addAll(bs.getStateManager().getStates()));
        return builder.build();
    }
}
