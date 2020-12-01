package com.github.jummes.supremeitem.placeholder.numeric.mob;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;

@Enumerable.Parent(classArray = {MobLevelPlaceholder.class})
@Enumerable.Displayable(name = "&c&lSupremeMob Placeholders", condition = "supremeMobsEnabled", description = "gui.placeholder.double.mob.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjEyYjU4Yzg0MWIzOTQ4NjNkYmNjNTRkZTFjMmFkMjY0OGFmOGYwM2U2NDg5ODhjMWY5Y2VmMGJjMjBlZTIzYyJ9fX0=")
public abstract class MobNumericPlaceholder extends NumericPlaceholder {
    public MobNumericPlaceholder(boolean target) {
        super(target);
    }

    public static boolean supremeMobsEnabled(ModelPath path) {
        return SupremeItem.getInstance().getSupremeMobHook().isEnabled();
    }
}
