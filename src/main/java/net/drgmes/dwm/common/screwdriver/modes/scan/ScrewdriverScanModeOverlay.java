package net.drgmes.dwm.common.screwdriver.modes.scan;

import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Language;
import net.minecraft.util.math.Vec2f;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ScrewdriverScanModeOverlay {
    public static final ScrewdriverScanModeOverlay INSTANCE = new ScrewdriverScanModeOverlay();

    private static final int PADDING = 10;
    private static final int LINE_MARGIN = 1;
    private static final float LINE_SCALE = 0.5F;

    public void render(MatrixStack matrixStack, float delta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player == null || mc.world == null) return;

        ItemStack screwdriverItemStack = null;
        ItemStack mainItemStack = player.getMainHandStack();
        ItemStack offItemStack = player.getOffHandStack();

        if (Screwdriver.checkItemStackIsScrewdriver(mainItemStack)) screwdriverItemStack = mainItemStack;
        if (Screwdriver.checkItemStackIsScrewdriver(offItemStack)) screwdriverItemStack = offItemStack;
        if (screwdriverItemStack == null) return;

        int screenWidth = mc.getWindow().getScaledWidth();
        int screenHeight = mc.getWindow().getScaledHeight();
        int maxTextLength = Math.round((screenWidth - 192) / 2F - PADDING * 1.5F);

        float modeTextScale = 0.5F;
        float modeTextOffset = player.isCreative() ? 3.175F : 5.175F;
        Vec2f modePos = new Vec2f((screenWidth - 192 + PADDING) / 2F + 2, screenHeight - mc.textRenderer.fontHeight * modeTextOffset);
        MutableText modeTitle = Screwdriver.getInteractionMode(screwdriverItemStack).getTitle().copy().formatted(Formatting.GOLD);
        MutableText modeText = Text.translatable("title.dwm.screwdriver.mode", modeTitle);

        matrixStack.push();
        matrixStack.scale(modeTextScale, modeTextScale, modeTextScale);
        mc.textRenderer.drawWithShadow(matrixStack, modeText, modePos.x / modeTextScale, modePos.y / modeTextScale, 0xFFFFFF);
        matrixStack.pop();

        NbtCompound tag = Screwdriver.getData(screwdriverItemStack);
        if (mc.world.getTime() - tag.getLong("time") > 60) return;

        Text title = Text.Serializer.fromJson(tag.getString("title"));
        List<Text> lines = new ArrayList<>();
        List<String> keys = new ArrayList<>(tag.getCompound("linesTag").getKeys().stream().toList());
        List<OrderedText> titleLines = Language.getInstance().reorder(mc.textRenderer.getTextHandler().wrapLines(title, maxTextLength, Style.EMPTY));

        keys.sort(Comparator.comparing((key) -> key));
        keys.forEach((key) -> lines.add(Text.Serializer.fromJson(tag.getCompound("linesTag").getString(key))));

        float titleLineHeight = mc.textRenderer.fontHeight + LINE_MARGIN;
        float lineHeight = mc.textRenderer.fontHeight * LINE_SCALE + LINE_MARGIN;
        float height = titleLineHeight * titleLines.size() + lineHeight * lines.size() - LINE_MARGIN * 2;

        Vec2f pos = new Vec2f(screenWidth - maxTextLength - PADDING, screenHeight - height - PADDING / 2F - 1);
        float y = pos.y;

//        int color = 0x05000000;
//        int bgPadding = PADDING / 2;
//        Vec2f bgPos1 = new Vec2f(pos.x - bgPadding, pos.y - bgPadding);
//        Vec2f bgPos2 = new Vec2f(pos.x + maxTextLength + bgPadding, pos.y + height + bgPadding);
//        this.fillGradient(matrixStack, (int) bgPos1.x, (int) bgPos1.y, (int) bgPos2.x, (int) bgPos2.y, color, color);

        for (OrderedText titleLine : titleLines) {
            mc.textRenderer.drawWithShadow(matrixStack, titleLine, pos.x, y, 0xFFFFFF);
            y += titleLineHeight;
        }

        matrixStack.push();
        matrixStack.scale(LINE_SCALE, LINE_SCALE, LINE_SCALE);
        for (Text line : lines) {
            mc.textRenderer.drawWithShadow(matrixStack, line, pos.x / LINE_SCALE, y / LINE_SCALE, 0xFFFFFF);
            y += lineHeight;
        }
        matrixStack.pop();
    }
}
