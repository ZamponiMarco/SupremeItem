package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.VectorValue;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

@Enumerable.Displayable
@Enumerable.Child
public class SetVelocityAction extends EntityAction {

    private static final String NAME_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0=";

    @Serializable(headTexture = NAME_HEAD, description = "gui.placeholder.double.variable.name")
    private VectorValue vector;

    public SetVelocityAction() {
        this(TARGET_DEFAULT, new VectorValue());
    }

    public SetVelocityAction(boolean target, VectorValue vector) {
        super(target);
        this.vector = vector;
    }

    public SetVelocityAction(Map<String, Object> map) {
        super(map);
        this.vector = (VectorValue) map.getOrDefault("vector", new VectorValue());
    }

    // TODO

    @Override
    public ActionResult execute(Target target, Source source) {
        LivingEntity entity = getEntity(target, source);
        if (entity == null) {
            return ActionResult.FAILURE;
        }

        entity.setVelocity(vector.getRealValue(target, source).computeVector(target, source));

        return ActionResult.SUCCESS;
    }

    @Override
    public String getName() {
        return "Velocity";
    }

    @Override
    public Action clone() {
        return new SetVelocityAction(target, vector.clone());
    }
}
