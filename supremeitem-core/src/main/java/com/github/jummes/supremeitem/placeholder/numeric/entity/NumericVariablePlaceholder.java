package com.github.jummes.supremeitem.placeholder.numeric.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.Objects;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lVariable Placeholder", description = "gui.placeholder.double.entity.variable.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjVjNGQyNGFmZmRkNDgxMDI2MjAzNjE1MjdkMjE1NmUxOGMyMjNiYWU1MTg5YWM0Mzk4MTU2NDNmM2NmZjlkIn19fQ==")
public class NumericVariablePlaceholder extends EntityNumericPlaceholder {

    private static final String NAME_DEFAULT = "var";
    private static final boolean PERSISTENT_DEFAULT = false;

    private static final String NAME_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0=";
    private static final String PERSISTENT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTViMzRjNmNlZTY4NGQ3MTcxNGIzYTFjNzExNTExY2JkNjkyNDQ3ODIwYmM5YTExMzYyOGMxZDM1ODA0ODI1ZSJ9fX0=";

    @Serializable(headTexture = NAME_HEAD, description = "gui.placeholder.double.entity.variable.name")
    @Serializable.Optional(defaultValue = "NAME_DEFAULT")
    private String name;

    @Serializable(headTexture = PERSISTENT_HEAD, description = "gui.placeholder.double.entity.variable.persistent")
    @Serializable.Optional(defaultValue = "PERSISTENT_DEFAULT")
    private boolean persistent;

    public NumericVariablePlaceholder() {
        this(TARGET_DEFAULT, NAME_DEFAULT, PERSISTENT_DEFAULT);
    }


    public NumericVariablePlaceholder(boolean target, String name, boolean persistent) {
        super(target);
        this.name = name;
        this.persistent = persistent;
    }

    public NumericVariablePlaceholder(Map<String, Object> map) {
        super(map);
        this.name = (String) map.getOrDefault("name", NAME_DEFAULT);
        this.persistent = (boolean) map.getOrDefault("persistent", PERSISTENT_DEFAULT);
    }

    @Override
    public NumericPlaceholder clone() {
        return new NumericVariablePlaceholder(target, name, persistent);
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        LivingEntity entity = getEntity(target, source);

        if (entity == null) {
            return 0.0;
        }

        if (persistent) {
            return entity.getPersistentDataContainer().get(new NamespacedKey(SupremeItem.getInstance(), name),
                    PersistentDataType.DOUBLE);
        }
        return entity.getMetadata(name).stream().filter(m -> Objects.equals(m.getOwningPlugin(),
                SupremeItem.getInstance())).findFirst().orElse(new FixedMetadataValue(SupremeItem.getInstance(),
                0.0)).asDouble();
    }

    @Override
    public String getName() {
        return String.format(persistent ? "Persistent" : "" + "% s. % s", target ? "Target" : "Source", name);
    }
}
