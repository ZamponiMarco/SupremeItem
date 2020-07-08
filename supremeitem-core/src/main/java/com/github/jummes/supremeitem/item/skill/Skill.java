package com.github.jummes.supremeitem.item.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.model.Model;

@Enumerable.Parent(classArray = {RightClickSkill.class, LeftClickSkill.class, HitEntitySkill.class, TimerSkill.class})
public abstract class Skill implements Model {
    @Override
    public boolean equals(Object obj) {
        return getClass().equals(obj.getClass());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
