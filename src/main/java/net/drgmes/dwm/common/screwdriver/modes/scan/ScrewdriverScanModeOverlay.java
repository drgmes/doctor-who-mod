package net.drgmes.dwm.common.screwdriver.modes.scan;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.items.screwdriver.ScrewdriverItem;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DWM.MODID)
public class ScrewdriverScanModeOverlay {
    private static final int PADDING = 5;
    private static final int LINE_MARGIN = 1;
    private static final float LINE_SCALE = 0.5F;

    private static Player player;
    private static int liveTime;
    private static Component title;
    private static List<Component> lines;

    public static void setup(Player player, Component title, List<Component> lines) {
        ScrewdriverScanModeOverlay.liveTime = 5000;
        ScrewdriverScanModeOverlay.player = player;
        ScrewdriverScanModeOverlay.title = title;
        ScrewdriverScanModeOverlay.lines = lines;
    }

    public static void clear() {
        ScrewdriverScanModeOverlay.liveTime = 0;
        ScrewdriverScanModeOverlay.player = null;
        ScrewdriverScanModeOverlay.title = null;
        ScrewdriverScanModeOverlay.lines = null;
    }

    private static void render(PoseStack poseStack) {
        if (liveTime <= 0) return;
        if (title == null) return;
        if (lines == null) return;

        if (--liveTime <= 0) {
            ScrewdriverScanModeOverlay.clear();
            return;
        }

        final Minecraft mc = Minecraft.getInstance();
        final int screenWidth = mc.getWindow().getGuiScaledWidth();
        final int screenHeight = mc.getWindow().getGuiScaledHeight();

        float titleLineHeight = mc.font.lineHeight + LINE_MARGIN;
        float lineHeight = mc.font.lineHeight * LINE_SCALE + LINE_MARGIN;
        float height = titleLineHeight + lineHeight * lines.size() - LINE_MARGIN;

        int width = mc.font.width(title);
        for (Component line : lines) {
            int lineWidth = (int) (mc.font.width(line) * LINE_SCALE);
            if (lineWidth > width) width = lineWidth;
        }

        Vec2 pos = new Vec2(screenWidth - width - PADDING, screenHeight - height - PADDING - 20);
        float y = pos.y + titleLineHeight;

        mc.font.drawShadow(poseStack, title, pos.x, pos.y, 0xFFFFFF);

        poseStack.pushPose();
        poseStack.scale(LINE_SCALE, LINE_SCALE, LINE_SCALE);
        for (Component line : lines) {
            mc.font.drawShadow(poseStack, line, pos.x / LINE_SCALE, y / LINE_SCALE, 0xFFFFFF);
            y += lineHeight;
        }
        poseStack.popPose();
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (player == null) return;

        Item mainHandItem = player.getMainHandItem().getItem();
        Item offHandItem = player.getOffhandItem().getItem();
        if (!(mainHandItem instanceof ScrewdriverItem || offHandItem instanceof ScrewdriverItem)) return;

        render(event.getMatrixStack());
    }
}
