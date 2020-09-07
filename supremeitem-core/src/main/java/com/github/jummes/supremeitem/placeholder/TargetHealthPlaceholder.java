package com.github.jummes.supremeitem.placeholder;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;

import java.util.Map;

@Enumerable.Child
public class TargetHealthPlaceholder extends NumericPlaceholder {

    public TargetHealthPlaceholder() {

    }

    public static TargetHealthPlaceholder deserialize(Map<String, Object> map) {
        return new TargetHealthPlaceholder();
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        if (target instanceof EntityTarget) {
            return ((EntityTarget) target).getTarget().getHealth();
        }
        return Double.NaN;
    }

}
