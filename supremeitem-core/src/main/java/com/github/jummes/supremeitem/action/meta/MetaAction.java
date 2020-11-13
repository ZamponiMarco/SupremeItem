package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.Action;
import com.google.common.collect.Lists;

import java.util.List;

@Enumerable.Parent(classArray = {WrapperAction.class, AbstractProjectileAction.class, DelayedAction.class,
        ProjectileAction.class, AreaEntitiesAction.class, SkillAction.class, TimerAction.class, CancelEventAction.class,
        ConditionAction.class, CommandAction.class, RepeatUntilAction.class, AreaBlocksAction.class, HomingProjectileAction.class})
@Enumerable.Displayable(name = "&9&lAction &6» &cMeta", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjgzODRjYjFiYmEyNWE1NzE5YjQyOTkyMzFhNWI1NDEzZTQzYjU3MDk5YzMyNzk5ZTczYTUxMTM2OTE3YWY4MyJ9fX0=")
public abstract class MetaAction extends Action {

    protected static final List<Action> ACTIONS_DEFAULT = Lists.newArrayList();

    public MetaAction(boolean target) {
        super(target);
    }
}
