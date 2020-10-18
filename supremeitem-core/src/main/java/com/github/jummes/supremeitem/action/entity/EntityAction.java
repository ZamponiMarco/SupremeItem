package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.Action;

@Enumerable.Parent(classArray = {DamageAction.class, EffectAction.class, HealAction.class, MessageAction.class,
        PullAction.class, PushAction.class, ConsumeItemAction.class, SetNumericVariableAction.class})
@Enumerable.Displayable(name = "&9&lAction &6» &cEntity Targetable", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA2NjliNDYyMjVjNjhiMzZmYTJkOTQ4OTU4YmMzYjQ0MTQyNzViZTFjNjUyZjg3NWM4NTkwNzg1MzIxYjYifX19")
public abstract class EntityAction extends Action {
}
