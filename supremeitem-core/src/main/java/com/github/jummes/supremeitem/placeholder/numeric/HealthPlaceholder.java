package com.github.jummes.supremeitem.placeholder.numeric;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import lombok.AllArgsConstructor;

import java.util.Map;

@Enumerable.Child
@AllArgsConstructor
@Enumerable.Displayable(name = "&c&lHealth Placeholder", description = "gui.placeholder.health.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjEyNjZiNzQ4MjQyMTE1YjMwMzcwOGQ1OWNlOWQ1NTIzYjdkNzljMTNmNmRiNGViYzkxZGQ0NzIwOWViNzU5YyJ9fX0=")
public class HealthPlaceholder extends NumericPlaceholder {

    @Serializable(headTexture = TARGET_HEAD, description = "gui.placeholder.target")
    private boolean target;

    public HealthPlaceholder() {
        this(true);
    }

    public static HealthPlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.get("target");
        return new HealthPlaceholder(target);
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        if (this.target) {
            if (target instanceof EntityTarget) {
                return ((EntityTarget) target).getTarget().getHealth();
            }
            return Double.NaN;
        }
        if (source instanceof EntitySource) {
            return ((EntitySource) source).getSource().getHealth();
        }
        return Double.NaN;
    }

}
