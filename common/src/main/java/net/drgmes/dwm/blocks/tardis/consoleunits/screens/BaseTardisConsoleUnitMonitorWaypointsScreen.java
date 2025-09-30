package net.drgmes.dwm.blocks.tardis.consoleunits.screens;

import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public abstract class BaseTardisConsoleUnitMonitorWaypointsScreen extends BaseTardisConsoleUnitMonitorScreen {
    protected static final int MAX_XZ_COORDS = 9999999;
    protected static final int MAX_Y_COORDS = 999;

    protected BlockPos blockPos;

    protected TextFieldWidget nameField;
    protected TextFieldWidget xField;
    protected TextFieldWidget yField;
    protected TextFieldWidget zField;

    public BaseTardisConsoleUnitMonitorWaypointsScreen(Text title, BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
        super(title, tardisConsoleUnitBlockEntity);
    }

    @Override
    public boolean shouldCloseOnInventoryKey() {
        return !this.nameField.isFocused() && !this.xField.isFocused() && !this.yField.isFocused() && !this.zField.isFocused();
    }

    protected void updateCoordsFields() {
        Integer x = null, y = null, z = null;
        if (this.blockPos != null) {
            x = this.blockPos.getX();
            y = this.blockPos.getY();
            z = this.blockPos.getZ();
        }

        String xText = this.xField.getText();
        if (xText != null) xText = xText.replaceAll("[^0-9-]", "");
        if (xText != null && !xText.isEmpty()) try { x = Integer.parseInt(xText); } catch (Exception ignored) {}

        String yText = this.yField.getText();
        if (yText != null) yText = yText.replaceAll("[^0-9-]", "");
        if (yText != null && !yText.isEmpty()) try { y = Integer.parseInt(yText); } catch (Exception ignored) {}

        String zText = this.zField.getText();
        if (zText != null) zText = zText.replaceAll("[^0-9-]", "");
        if (zText != null && !zText.isEmpty()) try { z = Integer.parseInt(zText); } catch (Exception ignored) {}

        if (x != null) x = Math.min(MAX_XZ_COORDS, Math.max(-MAX_XZ_COORDS, x));
        if (y != null) y = Math.min(MAX_Y_COORDS, Math.max(-MAX_Y_COORDS, y));
        if (z != null) z = Math.min(MAX_XZ_COORDS, Math.max(-MAX_XZ_COORDS, z));

        if (this.blockPos != null) {
            if (x != null) this.blockPos = new BlockPos(x, this.blockPos.getY(), this.blockPos.getZ());
            if (y != null) this.blockPos = new BlockPos(this.blockPos.getX(), y, this.blockPos.getZ());
            if (z != null) this.blockPos = new BlockPos(this.blockPos.getX(), this.blockPos.getY(), z);
        }
        else if (x != null && y != null && z != null) {
            this.blockPos = new BlockPos(x, y, z);
        }

        if (xText != null) {
            if (x != null && !xText.isEmpty() && !this.xField.getText().startsWith("-") && !this.xField.getText().equals(String.valueOf(x))) this.xField.setText(String.valueOf(x));
            else if (!this.xField.getText().equals(xText)) this.xField.setText(xText);
        }

        if (yText != null) {
            if (y != null && !yText.isEmpty() && !this.yField.getText().startsWith("-") && !this.yField.getText().equals(String.valueOf(y))) this.yField.setText(String.valueOf(y));
            else if (!this.yField.getText().equals(yText)) this.yField.setText(yText);
        }

        if (zText != null) {
            if (z != null && !zText.isEmpty() && !this.zField.getText().startsWith("-") && !this.zField.getText().equals(String.valueOf(z))) this.zField.setText(String.valueOf(z));
            else if (!this.zField.getText().equals(zText)) this.zField.setText(zText);
        }
    }
}
