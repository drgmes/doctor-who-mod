package net.drgmes.dwm.common.screwdriver.modes.scan;

import java.util.List;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.items.screwdriver.ScrewdriverItem;
import net.minecraft.client.Minecraft;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.GuiUtils;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DWM.MODID)
public class ScrewdriverScanModeOverlay {
    private static final int PADDING = 10;
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
        final int maxTextLength = Math.round((screenWidth - 192) / 2 - PADDING * 1.5F);

        List<FormattedCharSequence> titleLines = Language.getInstance().getVisualOrder(mc.font.getSplitter().splitLines(title, maxTextLength, Style.EMPTY));

        float titleLineHeight = mc.font.lineHeight + LINE_MARGIN;
        float lineHeight = mc.font.lineHeight * LINE_SCALE + LINE_MARGIN;
        float height = titleLineHeight * titleLines.size() + lineHeight * lines.size() - LINE_MARGIN * 2;

        Vec2 pos = new Vec2(screenWidth - maxTextLength - PADDING, screenHeight - height - PADDING / 2 - 1);
        float y = pos.y;

        int color = 0x05000000;
        int bgPadding = PADDING / 2;
        Vec2 bgPos1 = new Vec2(pos.x - bgPadding, pos.y - bgPadding);
        Vec2 bgPos2 = new Vec2(pos.x + maxTextLength + bgPadding, pos.y + height + bgPadding);
        GuiUtils.drawGradientRect(poseStack.last().pose(), 0, (int) bgPos1.x, (int) bgPos1.y, (int) bgPos2.x, (int) bgPos2.y, color, color);

        for (FormattedCharSequence titleLine : titleLines) {
            mc.font.drawShadow(poseStack, titleLine, pos.x, y, 0xFFFFFF);
            y += titleLineHeight;
        }

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
