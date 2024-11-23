package net.drgmes.dwm.common.tardis.ars;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.text.Text;

public class ArsCategory {
    public static final PacketCodec<PacketByteBuf, ArsCategory> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public void encode(PacketByteBuf buf, ArsCategory payload) {
            buf.writeString(payload.name);
            buf.writeString(payload.title);
            buf.writeString(payload.tag);
            buf.writeString(payload.parent);
        }

        @Override
        public ArsCategory decode(PacketByteBuf buf) {
            return new ArsCategory(buf.readString(), buf.readString(), buf.readString(), buf.readString());
        }
    };

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

    public Text getTag() {
        return Text.translatable(this.tag);
    }

    public Text getTitle() {
        return Text.translatable(this.title);
    }
}
