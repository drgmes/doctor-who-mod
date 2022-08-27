package net.drgmes.dwm.utils.builders;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModVillagerProfessions;
import net.drgmes.dwm.setup.Registration;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

public class VillagerProfessionBuilder {
    private final String name;
    private final PointOfInterestType poiType;
    private final RegistryKey<PointOfInterestType> poiKey;
    private final VillagerProfession villagerProfession;

    public VillagerProfessionBuilder(String name, int ticketCount, int searchDistance, SoundEvent workSound, Block... blocks) {
        Identifier id = DWM.getIdentifier(name);

        this.name = name;
        this.poiKey = RegistryKey.of(Registry.POINT_OF_INTEREST_TYPE_KEY, id);
        this.poiType = PointOfInterestHelper.register(id, ticketCount, searchDistance, blocks);
        this.villagerProfession = Registration.registerVillagerProfession(name, this.poiType, workSound);

        ModVillagerProfessions.VILLAGER_PROFESSION_BUILDERS.add(this);
    }

    public String getName() {
        return this.name;
    }

    public VillagerProfession getVillagerProfession() {
        return this.villagerProfession;
    }

    public RegistryKey<PointOfInterestType> getPoiKey() {
        return this.poiKey;
    }

    public PointOfInterestType getPoiType() {
        return this.poiType;
    }

    public void setTradeOffers(ImmutableMap<Integer, TradeOffers.Factory[]> tradeOffers) {
        TradeOffers.PROFESSION_TO_LEVELED_TRADE.put(this.getVillagerProfession(), new Int2ObjectOpenHashMap<>(tradeOffers));
    }
}
