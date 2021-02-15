package newhorizon.func;

import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.layout.Table;
import mindustry.gen.Tex;
import mindustry.world.meta.StatValue;

import static newhorizon.func.TableFuncs.LEN;
import static newhorizon.func.TableFuncs.OFFSET;

public class TextureFilterValue implements StatValue {
    public final TextureRegion region;
    public String description;

    public TextureFilterValue(TextureRegion region, String description) {
        this.region = region;
        this.description = description;
    }

    @Override
    public void display(Table table) {
        table.table(t2 ->{
            t2.left();
            t2.table(t -> t.image(region).size(LEN * 1.5f).left()).size(LEN * 1.5f + OFFSET / 2f).pad(OFFSET / 2f).left();
            t2.table(t -> t.add(description).left()).pad(OFFSET / 2f).left();
        }).left().grow().row();
    }
}
