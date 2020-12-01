package com.github.jummes.supremeitem.placeholder.numeric;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.Placeholder;
import com.github.jummes.supremeitem.placeholder.numeric.block.NumericBlockPlaceholder;
import com.github.jummes.supremeitem.placeholder.numeric.location.LocationPlaceholder;
import com.github.jummes.supremeitem.placeholder.numeric.mob.MobNumericPlaceholder;
import com.github.jummes.supremeitem.placeholder.numeric.operator.NumberOperatorPlaceholder;
import org.bukkit.entity.LivingEntity;

@Enumerable.Parent(classArray = {HealthPlaceholder.class, MaxHealthPlaceholder.class,
        HungerPlaceholder.class, NumericVariablePlaceholder.class, NumberOperatorPlaceholder.class,
        LocationPlaceholder.class, NumericBlockPlaceholder.class, SavedNumericPlaceholder.class,
        BalancePlaceholder.class, MobNumericPlaceholder.class})
public abstract class NumericPlaceholder extends Placeholder<Double> {
    public NumericPlaceholder(boolean target) {
        super(target);
    }

    public abstract NumericPlaceholder clone();

    protected LivingEntity getEntity(Target target, Source source) {
        if (this.target) {
            if (target instanceof EntityTarget) {
                return ((EntityTarget) target).getTarget();
            }
            return null;
        }
        return source.getCaster();
    }
}
