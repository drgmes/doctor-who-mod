package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.blocks.tardis.consoleunits.screens.TardisConsoleUnitMonitorConsoleMainScreen;
import net.drgmes.dwm.blocks.tardis.consoleunits.screens.TardisConsoleUnitTelepathicInterfaceLocationsScreen;
import net.drgmes.dwm.blocks.tardis.consoleunits.screens.TardisConsoleUnitTelepathicInterfaceMapBannersScreen;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisarscreator.TardisArsCreatorBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisarscreator.screens.TardisArsCreatorScreen;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.screens.TardisArsDestroyerScreen;
import net.drgmes.dwm.blocks.tardis.misc.tardisroundel.TardisRoundelBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisteleporter.TardisTeleporterBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisteleporter.screens.TardisTeleporterScreen;
import net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner.TardisToyotaSpinnerBlockEntity;
import net.drgmes.dwm.common.sonicdevice.SonicDevice;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRoomEntry;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRooms;
import net.drgmes.dwm.enums.TardisTelepathicInterfaceDataType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Client-only S2C packet handlers. Kept in a separate class so packet payload
 * classes can be loaded on dedicated servers without pulling in client-only types
 * (e.g. {@link net.minecraft.client.gui.screen.Screen}).
 */
@Environment(EnvType.CLIENT)
public final class ClientPackets {
    private ClientPackets() {
    }

    public static void handleArsCreatorOpen(ArsCreatorOpenPacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            ClientPlayerEntity player = (ClientPlayerEntity) context.getPlayer();

            if (player.getWorld().getBlockEntity(payload.blockPos()) instanceof TardisArsCreatorBlockEntity) {
                MinecraftClient.getInstance().setScreen(new TardisArsCreatorScreen(payload.blockPos(), payload.arsCategories(), payload.arsStructures()));
            }
        });
    }

    public static void handleArsDestroyerOpen(ArsDestroyerOpenPacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            ClientPlayerEntity player = (ClientPlayerEntity) context.getPlayer();

            if (player.getWorld().getBlockEntity(payload.blockPos()) instanceof TardisArsDestroyerBlockEntity) {
                MinecraftClient.getInstance().setScreen(new TardisArsDestroyerScreen(payload.blockPos(), payload.arsStructure()));
            }
        });
    }

    public static void handleDimensionAdd(DimensionAddPacket payload, NetworkManager.PacketContext context) {
        ClientPlayerEntity player = (ClientPlayerEntity) context.getPlayer();

        Set<RegistryKey<World>> worlds = player.networkHandler.getWorldKeys();
        if (worlds == null || worlds.contains(payload.worldKey())) return;

        worlds.add(payload.worldKey());
    }

    public static void handleDimensionRemove(DimensionRemovePacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            ClientPlayerEntity player = (ClientPlayerEntity) context.getPlayer();

            Set<RegistryKey<World>> worlds = player.networkHandler.getWorldKeys();
            if (worlds == null || !worlds.contains(payload.worldKey())) return;

            worlds.remove(payload.worldKey());
        });
    }

    public static void handleSonicDeviceUpdate(SonicDeviceUpdatePacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            ClientPlayerEntity player = (ClientPlayerEntity) context.getPlayer();

            EquipmentSlot slot = EquipmentSlot.byName(payload.slotName());
            ItemStack equippedStack = player.getEquippedStack(slot);

            if (SonicDevice.checkItemStackIsSonicDevice(equippedStack) || equippedStack.isEmpty()) {
                context.getPlayer().equipStack(slot, payload.itemStack());
            }
        });
    }

    public static void handleTardisConsoleUnitControlsStatesUpdate(TardisConsoleUnitControlsStatesUpdatePacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            ClientPlayerEntity player = (ClientPlayerEntity) context.getPlayer();

            if (player.getWorld().getBlockEntity(payload.blockPos()) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
                tardisConsoleUnitBlockEntity.controlsStorage.readNbt(payload.tag());
            }
        });
    }

    public static void handleTardisConsoleUnitMonitorOpen(TardisConsoleUnitMonitorOpenPacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            ClientPlayerEntity player = (ClientPlayerEntity) context.getPlayer();

            if (player.getWorld().getBlockEntity(payload.blockPos()) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
                NbtCompound tag = new NbtCompound();
                tag.put("tardisTag", payload.tardisTag());
                tag.put("roomsTag", payload.roomsTag());

                TardisConsoleRooms.CONSOLE_ROOMS.clear();

                List<String> keys = new ArrayList<>(payload.roomsTag().getKeys());
                keys.sort(Comparator.comparing((key) -> key));

                keys.forEach((key) -> {
                    TardisConsoleRoomEntry entry = TardisConsoleRoomEntry.fromNbt(payload.roomsTag().getCompound(key));
                    TardisConsoleRooms.CONSOLE_ROOMS.put(entry.name, entry);
                });

                tardisConsoleUnitBlockEntity.tardisStateManager.readNbt(payload.tardisTag(), player.getRegistryManager());
                MinecraftClient.getInstance().setScreen(new TardisConsoleUnitMonitorConsoleMainScreen(tardisConsoleUnitBlockEntity, payload.tardisId(), payload.owner(), tag));
            }
        });
    }

    public static void handleTardisConsoleUnitMonitorPageUpdate(TardisConsoleUnitMonitorPageUpdatePacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            ClientPlayerEntity player = (ClientPlayerEntity) context.getPlayer();

            if (player.getWorld().getBlockEntity(payload.blockPos()) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
                tardisConsoleUnitBlockEntity.monitorPage = payload.monitorPage();
            }
        });
    }

    public static void handleTardisConsoleUnitSonicScrewdriverSlotUpdate(TardisConsoleUnitSonicScrewdriverSlotUpdatePacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            ClientPlayerEntity player = (ClientPlayerEntity) context.getPlayer();

            if (player.getWorld().getBlockEntity(payload.blockPos()) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
                tardisConsoleUnitBlockEntity.sonicScrewdriverItemStack = payload.itemStack();
            }
        });
    }

    public static void handleTardisConsoleUnitTelepathicInterfaceLocationsOpen(TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            ClientPlayerEntity player = (ClientPlayerEntity) context.getPlayer();

            if (player.getWorld().getBlockEntity(payload.blockPos()) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
                List<Map.Entry<Identifier, TardisTelepathicInterfaceDataType>> locations = new ArrayList<>();
                List<String> keys = new ArrayList<>(payload.tag().getKeys());

                keys.sort(Comparator.comparing((key) -> key));
                keys.forEach((key) -> {
                    locations.add(Map.entry(
                        Identifier.of(payload.tag().getCompound(key).getString("id")),
                        TardisTelepathicInterfaceDataType.valueOf(payload.tag().getCompound(key).getString("type"))
                    ));
                });

                MinecraftClient.getInstance().setScreen(new TardisConsoleUnitTelepathicInterfaceLocationsScreen(tardisConsoleUnitBlockEntity, locations));
            }
        });
    }

    public static void handleTardisConsoleUnitTelepathicInterfaceMapBannersOpen(TardisConsoleUnitTelepathicInterfaceMapBannersOpenPacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            ClientPlayerEntity player = (ClientPlayerEntity) context.getPlayer();

            if (player.getWorld().getBlockEntity(payload.blockPos()) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
                MinecraftClient.getInstance().setScreen(new TardisConsoleUnitTelepathicInterfaceMapBannersScreen(tardisConsoleUnitBlockEntity, MapState.fromNbt(payload.tag(), player.getRegistryManager())));
            }
        });
    }

    public static void handleTardisConsoleUnitUpdate(TardisConsoleUnitUpdatePacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            ClientPlayerEntity player = (ClientPlayerEntity) context.getPlayer();

            if (player.getWorld().getBlockEntity(payload.blockPos()) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
                tardisConsoleUnitBlockEntity.readNbt(payload.tag(), player.getRegistryManager());
            }
        });
    }

    public static void handleTardisExteriorUpdate(TardisExteriorUpdatePacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            ClientPlayerEntity player = (ClientPlayerEntity) context.getPlayer();

            if (player.getWorld().getBlockEntity(payload.blockPos()) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
                switch (payload.exteriorAction()) {
                    case NORMALIZE -> tardisExteriorBlockEntity.normalize();
                    case DEMAT -> tardisExteriorBlockEntity.demat();
                    case REMAT -> tardisExteriorBlockEntity.remat();
                    case PULSE -> tardisExteriorBlockEntity.pulse();
                }
            }
        });
    }

    public static void handleTardisRoundelBlockTemplateClear(TardisRoundelBlockTemplateClearPacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            ClientPlayerEntity player = (ClientPlayerEntity) context.getPlayer();

            if (player.getWorld().getBlockEntity(payload.blockPos()) instanceof TardisRoundelBlockEntity tardisRoundelBlockEntity) {
                tardisRoundelBlockEntity.blockTemplate = null;
            }
        });
    }

    public static void handleTardisRoundelBlockTemplateUpdate(TardisRoundelBlockTemplateUpdatePacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            ClientPlayerEntity player = (ClientPlayerEntity) context.getPlayer();

            if (player.getWorld().getBlockEntity(payload.blockPos()) instanceof TardisRoundelBlockEntity tardisRoundelBlockEntity) {
                tardisRoundelBlockEntity.blockTemplate = Identifier.of(payload.blockTemplateId());
            }
        });
    }

    public static void handleTardisRoundelUpdate(TardisRoundelUpdatePacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            ClientPlayerEntity player = (ClientPlayerEntity) context.getPlayer();

            if (player.getWorld().getBlockEntity(payload.blockPos()) instanceof TardisRoundelBlockEntity tardisRoundelBlockEntity) {
                tardisRoundelBlockEntity.uncovered = payload.uncovered();
                tardisRoundelBlockEntity.lightMode = payload.lightMode();
            }
        });
    }

    public static void handleTardisToyotaSpinnerUpdate(TardisToyotaSpinnerUpdatePacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            ClientPlayerEntity player = (ClientPlayerEntity) context.getPlayer();

            if (player.getWorld().getBlockEntity(payload.blockPos()) instanceof TardisToyotaSpinnerBlockEntity tardisToyotaSpinnerBlockEntity) {
                tardisToyotaSpinnerBlockEntity.inProgress = payload.inProgress();
            }
        });
    }

    public static void handleTardisTeleporterOpen(TardisTeleporterOpenPacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            ClientPlayerEntity player = (ClientPlayerEntity) context.getPlayer();

            if (player.getWorld().getBlockEntity(payload.blockPos()) instanceof TardisTeleporterBlockEntity) {
                MinecraftClient.getInstance().setScreen(new TardisTeleporterScreen(payload.blockPos(), payload.destinationBlockPos(), payload.isLocked(), payload.allowedEntityTypes()));
            }
        });
    }
}
