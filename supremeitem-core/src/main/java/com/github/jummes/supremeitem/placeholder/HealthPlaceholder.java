package com.github.jummes.supremeitem.placeholder;

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
public class HealthPlaceholder extends NumericPlaceholder {

    private static final String ACTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";

    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.placeholder.target")
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
