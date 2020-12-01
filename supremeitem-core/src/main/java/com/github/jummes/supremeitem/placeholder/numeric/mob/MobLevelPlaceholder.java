package com.github.jummes.supremeitem.placeholder.numeric.mob;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import com.github.jummes.suprememob.mob.Mob;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "Mob level")
public class MobLevelPlaceholder extends MobNumericPlaceholder {

    public MobLevelPlaceholder() {
        this(TARGET_DEFAULT);
    }

    public MobLevelPlaceholder(boolean target) {
        super(target);
    }

    public static MobLevelPlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        return new MobLevelPlaceholder(target);
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {

        if (!SupremeItem.getInstance().getSupremeMobHook().isEnabled()) {
            return 0.0;
        }

        LivingEntity entity = getEntity(target, source);

        if (entity == null) {
            return 0.0;
        }

        return (double) Mob.getLevel(entity);
    }

    @Override
    public String getName() {
        return "Mob level";
    }

    @Override
    public NumericPlaceholder clone() {
        return new MobLevelPlaceholder(target);
    }
}
