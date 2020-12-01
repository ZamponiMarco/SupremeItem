package com.github.jummes.supremeitem.placeholder.numeric.mob;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;

@Enumerable.Parent(classArray = {MobLevelPlaceholder.class})
@Enumerable.Displayable(name = "SupremeMob placeholder", condition = "supremeMobsEnabled")
public abstract class MobNumericPlaceholder extends NumericPlaceholder {
    public MobNumericPlaceholder(boolean target) {
        super(target);
    }

    public static boolean supremeMobsEnabled(ModelPath path) {
        return SupremeItem.getInstance().getSupremeMobHook().isEnabled();
    }
}
