package com.github.jummes.supremeitem.action.variable;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import org.bukkit.entity.LivingEntity;

import javax.annotation.Nullable;

@Enumerable.Parent(classArray = {SetNumericVariableAction.class, SetStringVariableAction.class})
@Enumerable.Displayable(name = "&9&lSet variable &6» &cEntity Targetable", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjVjNGQyNGFmZmRkNDgxMDI2MjAzNjE1MjdkMjE1NmUxOGMyMjNiYWU1MTg5YWM0Mzk4MTU2NDNmM2NmZjlkIn19fQ")
public abstract class VariableAction extends Action {
    public VariableAction(boolean target) {
        super(target);
    }

    @Nullable
    public LivingEntity getEntity(Target target, Source source) {
        if (this.target && target instanceof EntityTarget) {
            return ((EntityTarget) target).getTarget();
        } else if (!this.target) {
            return source.getCaster();
        }
        return null;
    }
}
