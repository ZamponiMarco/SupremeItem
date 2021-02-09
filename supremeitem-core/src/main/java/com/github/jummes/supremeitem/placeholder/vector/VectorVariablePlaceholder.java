package com.github.jummes.supremeitem.placeholder.vector;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.math.Vector;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Map;
import java.util.Objects;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lVector Variable Placeholder", description = "gui.placeholder.vector.variable.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjVjNGQyNGFmZmRkNDgxMDI2MjAzNjE1MjdkMjE1NmUxOGMyMjNiYWU1MTg5YWM0Mzk4MTU2NDNmM2NmZjlkIn19fQ")
public class VectorVariablePlaceholder extends VectorPlaceholder {

    private static final String NAME_DEFAULT = "var";

    private static final String NAME_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0=";

    @Serializable(headTexture = NAME_HEAD, description = "gui.placeholder.vector.variable.name")
    @Serializable.Optional(defaultValue = "NAME_DEFAULT")
    private String name;

    public VectorVariablePlaceholder() {
        this(TARGET_DEFAULT, NAME_DEFAULT);
    }

    public VectorVariablePlaceholder(boolean target, String name) {
        super(target);
        this.name = name;
    }

    public VectorVariablePlaceholder(Map<String, Object> map) {
        super(map);
        this.name = (String) map.getOrDefault("name", NAME_DEFAULT);
    }

    @Override
    public Vector computePlaceholder(Target target, Source source) {
        LivingEntity entity = null;
        if (this.target && (target instanceof EntityTarget)) {
            entity = ((EntityTarget) target).getTarget();
        } else if (!this.target) {
            entity = source.getCaster();
        }

        if (entity == null) {
            return new Vector();
        }

        return (Vector) entity.getMetadata(name).stream().filter(m -> Objects.equals(m.getOwningPlugin(),
                SupremeItem.getInstance())).findFirst().orElse(new FixedMetadataValue(SupremeItem.getInstance(),
                new Vector())).value();
    }

    @Override
    public String getName() {
        return String.format("%s.%s", target ? "Target" : "Source", name);
    }

    @Override
    public VectorPlaceholder clone() {
        return new VectorVariablePlaceholder(target, name);
    }
}
