package net.drgmes.dwm.common.sonicdevice.modes.scan;

import net.drgmes.dwm.common.sonicdevice.SonicDevice;
import net.drgmes.dwm.utils.helpers.RenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Language;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Environment(EnvType.CLIENT)
public class SonicDeviceScanModeOverlay {
    public static final SonicDeviceScanModeOverlay INSTANCE = new SonicDeviceScanModeOverlay();

    private static final int PADDING = 10;
    private static final int LINE_MARGIN = 1;
    private static final float LINE_SCALE = 0.5F;

    public void render(DrawContext context) {
        MinecraftClient mc = MinecraftClient.getInstance();
        MatrixStack matrixStack = context.getMatrices();
        ClientPlayerEntity player = mc.player;
        if (player == null || mc.world == null) return;

        ItemStack sonicDeviceItemStack = null;
        ItemStack mainItemStack = player.getMainHandStack();
        ItemStack offItemStack = player.getOffHandStack();
        ItemStack helmetItemStack = player.getEquippedStack(EquipmentSlot.HEAD);

        if (SonicDevice.checkItemStackIsSonicDevice(mainItemStack)) sonicDeviceItemStack = mainItemStack;
        else if (SonicDevice.checkItemStackIsSonicDevice(offItemStack)) sonicDeviceItemStack = offItemStack;
        else if (SonicDevice.checkItemStackIsSonicDevice(helmetItemStack)) sonicDeviceItemStack = helmetItemStack;
        else return;

        int screenWidth = mc.getWindow().getScaledWidth();
        int screenHeight = mc.getWindow().getScaledHeight();

        float modeTextScale = 0.5F;
        int modeTextOffset = player.isCreative() ? 3 : 5;
        MutableText modeTitle = SonicDevice.getInteractionMode(sonicDeviceItemStack).getTitle().copy().formatted(Formatting.GOLD);
        MutableText modeText = Text.translatable("title.dwm.sonic_device.mode", modeTitle);
        Vector2i modePos = new Vector2i(screenWidth - (screenWidth - 192 + PADDING) / 2 - (int) Math.floor(mc.textRenderer.getWidth(modeText) * modeTextScale), screenHeight - mc.textRenderer.fontHeight * modeTextOffset);

        matrixStack.push();
        matrixStack.scale(modeTextScale, modeTextScale, modeTextScale);
        RenderHelper.drawText(modeText, mc.textRenderer, context, modePos.x / modeTextScale, modePos.y / modeTextScale, 0xFFFFFF, true);
        matrixStack.pop();

        NbtCompound tag = SonicDevice.getData(sonicDeviceItemStack);
        if (mc.world.getTime() - tag.getLong("time") > 60) return;

        int maxTextLength = (int) Math.floor((screenWidth - 192) / 2F - PADDING * 1.5F);
        Text title = Text.Serialization.fromJson(tag.getString("title"));
        List<Text> lines = new ArrayList<>();
        List<String> keys = new ArrayList<>(tag.getCompound("linesTag").getKeys().stream().toList());
        List<OrderedText> titleLines = Language.getInstance().reorder(mc.textRenderer.getTextHandler().wrapLines(title, maxTextLength, Style.EMPTY));

        keys.sort(Comparator.comparing((key) -> key));
        keys.forEach((key) -> lines.add(Text.Serialization.fromJson(tag.getCompound("linesTag").getString(key))));

        int titleLineHeight = mc.textRenderer.fontHeight + LINE_MARGIN;
        int lineHeight = (int) Math.floor(mc.textRenderer.fontHeight * LINE_SCALE) + LINE_MARGIN;
        int height = titleLineHeight * titleLines.size() + lineHeight * lines.size() - LINE_MARGIN * 2;

        Vector2i pos = new Vector2i(screenWidth - maxTextLength - PADDING, screenHeight - height - PADDING / 2 - 1);
        int y = pos.y;

        // TODO
//        int color = 0x05000000;
//        int bgPadding = PADDING / 2;
//        Vector2i bgPos1 = new Vector2i(pos.x - bgPadding, pos.y - bgPadding);
//        Vector2i bgPos2 = new Vector2i(pos.x + maxTextLength + bgPadding, pos.y + height + bgPadding);
//        this.fillGradient(matrixStack, bgPos1.x, bgPos1.y, bgPos2.x, bgPos2.y, color, color);

        for (OrderedText titleLine : titleLines) {
            RenderHelper.drawText(titleLine, mc.textRenderer, context, pos.x, y, 0xFFFFFF, true);
            y += titleLineHeight;
        }

        matrixStack.push();
        matrixStack.scale(LINE_SCALE, LINE_SCALE, LINE_SCALE);
        for (Text line : lines) {
            RenderHelper.drawText(line, mc.textRenderer, context, pos.x / LINE_SCALE, y / LINE_SCALE, 0xFFFFFF, true);
            y += lineHeight;
        }
        matrixStack.pop();
    }
}
