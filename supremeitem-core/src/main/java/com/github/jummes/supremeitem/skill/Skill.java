package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.model.Model;
import com.github.jummes.supremeitem.action.Action;
import com.google.common.collect.Lists;

import java.util.List;

@Enumerable.Parent(classArray = {RightClickSkill.class, LeftClickSkill.class, HitEntitySkill.class, TimerSkill.class, DamageEntitySkill.class})
public abstract class Skill implements Model {

    protected static final List<Action> ACTIONS_DEFAULT = Lists.newArrayList();
    protected static final int COOLDOWN_DEFAULT = 0;

    @Override
    public boolean equals(Object obj) {
        return getClass().equals(obj.getClass());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public enum SkillResult {
        SUCCESS,
        CANCELLED,
        FAILURE
    }
}
