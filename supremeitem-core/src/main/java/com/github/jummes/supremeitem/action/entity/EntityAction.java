package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import org.bukkit.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.Map;

@Enumerable.Parent(classArray = {DamageAction.class, EffectAction.class, HealAction.class, MessageAction.class,
        PullAction.class, PushAction.class, ConsumeItemAction.class, SetOnFireAction.class, BlockEventAction.class,
        RelativeTeleportAction.class, MoneyAction.class, GiveItemAction.class, ActionBarMessageAction.class,
        SetVelocityAction.class})
@Enumerable.Displayable(name = "&9&lAction &6» &cEntity Targetable", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA2NjliNDYyMjVjNjhiMzZmYTJkOTQ4OTU4YmMzYjQ0MTQyNzViZTFjNjUyZjg3NWM4NTkwNzg1MzIxYjYifX19")
public abstract class EntityAction extends Action {

    public EntityAction(boolean target) {
        super(target);
    }

    public EntityAction(Map<String, Object> map) {
        super(map);
    }

    @Nullable
    protected LivingEntity getEntity(Target target, Source source) {
        if (this.target && target instanceof EntityTarget) {
            return ((EntityTarget) target).getTarget();
        } else if (!this.target) {
            return source.getCaster();
        }
        return null;
    }
}
