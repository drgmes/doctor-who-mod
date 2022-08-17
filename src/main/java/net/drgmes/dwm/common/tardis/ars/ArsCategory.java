package net.drgmes.dwm.common.tardis.ars;

import net.minecraft.text.Text;

public class ArsCategory {
    private final String path;
    private final String tag;
    private final String title;
    private final ArsCategory parent;

    public ArsCategory(String path, String title, String tag, ArsCategory parent) {
        this.path = path;
        this.tag = tag;
        this.title = title;
        this.parent = parent;
    }

    public String getPath() {
        return this.path;
    }

    public Text getTag() {
        return Text.translatable(this.tag);
    }

    public Text getTitle() {
        return Text.translatable(this.title);
    }

    public ArsCategory getParent() {
        return this.parent;
    }
}
