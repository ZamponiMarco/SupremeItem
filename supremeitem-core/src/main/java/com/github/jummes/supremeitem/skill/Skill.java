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
    protected static final boolean COOLDOWN_MESSAGE_DEFAULT = true;

    protected static final String COOLDOWN_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0=";
    protected static final String COOLDOWN_MESSAGE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0=";

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
