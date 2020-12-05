package com.github.jummes.supremeitem.placeholder.numeric.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lHealth Placeholder", description = "gui.placeholder.double.entity.health.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzZmZGQ0YjEzZDU0ZjZjOTFkZDVmYTc2NWVjOTNkZDk0NThiMTlmOGFhMzRlZWI1YzgwZjQ1NWIxMTlmMjc4In19fQ==")
public class HealthPlaceholder extends EntityNumericPlaceholder {

    public HealthPlaceholder() {
        this(TARGET_DEFAULT);
    }

    public HealthPlaceholder(boolean target) {
        super(target);
    }

    public static HealthPlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        return new HealthPlaceholder(target);
    }

    @Override
    public NumericPlaceholder clone() {
        return new MaxHealthPlaceholder(target);
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        LivingEntity entity = getEntity(target, source);
        if (entity == null) {
            return Double.NaN;
        }

        return entity.getHealth();
    }

    @Override
    public String getName() {
        return String.format("%s Health", target ? "Target" : "Source");
    }

}
