package net.drgmes.dwm.common.tardis.ars;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class ArsCategory {
    public final String name;
    public final String title;
    public final String tag;
    public final String parent;

    public ArsCategory(String name, String title, String tag, String parent) {
        this.name = name;
        this.title = title;
        this.tag = tag;
        this.parent = parent;
    }

    public static void toPacket(PacketByteBuf buf, ArsCategory arsCategory) {
        buf.writeString(arsCategory.name);
        buf.writeString(arsCategory.title);
        buf.writeString(arsCategory.tag);
        buf.writeString(arsCategory.parent);
    }

    public static ArsCategory fromPacket(PacketByteBuf buf) {
        return new ArsCategory(buf.readString(), buf.readString(), buf.readString(), buf.readString());
    }

    public Text getTag() {
        return Text.translatable(this.tag);
    }

    public Text getTitle() {
        return Text.translatable(this.title);
    }
}
