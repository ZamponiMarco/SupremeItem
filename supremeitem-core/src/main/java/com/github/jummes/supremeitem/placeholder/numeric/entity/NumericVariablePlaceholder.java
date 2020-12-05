package com.github.jummes.supremeitem.placeholder.numeric.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Map;
import java.util.Objects;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lVariable Placeholder", description = "gui.placeholder.double.entity.variable.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjVjNGQyNGFmZmRkNDgxMDI2MjAzNjE1MjdkMjE1NmUxOGMyMjNiYWU1MTg5YWM0Mzk4MTU2NDNmM2NmZjlkIn19fQ==")
public class NumericVariablePlaceholder extends EntityNumericPlaceholder {

    private static final String NAME_DEFAULT = "var";

    private static final String NAME_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0=";

    @Serializable(headTexture = NAME_HEAD, description = "gui.placeholder.double.entity.variable.name")
    @Serializable.Optional(defaultValue = "NAME_DEFAULT")
    private String name;

    public NumericVariablePlaceholder() {
        this(TARGET_DEFAULT, NAME_DEFAULT);
    }

    public NumericVariablePlaceholder(boolean target, String name) {
        super(target);
        this.name = name;
    }

    public static NumericVariablePlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        String name = (String) map.getOrDefault("name", NAME_DEFAULT);
        return new NumericVariablePlaceholder(target, name);
    }

    @Override
    public NumericPlaceholder clone() {
        return new NumericVariablePlaceholder(target, name);
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        LivingEntity entity = getEntity(target, source);
        if (entity == null) {
            return 0.0;
        }

        return entity.getMetadata(name).stream().filter(m -> Objects.equals(m.getOwningPlugin(),
                SupremeItem.getInstance())).findFirst().orElse(new FixedMetadataValue(SupremeItem.getInstance(),
                0.0)).asDouble();
    }

    @Override
    public String getName() {
        return String.format("%s.%s", target ? "Target" : "Source", name);
    }
}
