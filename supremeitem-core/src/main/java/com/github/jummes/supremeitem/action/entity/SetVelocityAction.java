package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.VectorValue;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lVelocity Action", description = "gui.action.entity.velocity.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWE4NjhjZTkxN2MwOWFmOGU0YzM1MGE1ODA3MDQxZjY1MDliZjJiODlhY2E0NWU1OTFmYmJkN2Q0YjExN2QifX19")
public class SetVelocityAction extends EntityAction {

    private static final String VECTOR_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWFjYWNiODM2YzVlNDI4YjQ5YjVkMjI0Y2FiMjI4MjhlZmUyZjZjNzA0Zjc1OTM2NGQ3MWM2NTZlMzAxNDIwIn19fQ===";
    private static final String FALL_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDlmMWYwN2UyYjFjMzJiYjY0YTEyOGU1MjlmM2FmMWU1Mjg2ZTUxODU0NGVkZjhjYmFhNmM0MDY1YjQ3NmIifX19";

    @Serializable(headTexture = VECTOR_HEAD, description = "gui.action.entity.velocity.vector",
            additionalDescription = {"gui.additional-tooltips.value"})
    private VectorValue vector;

    @Serializable(headTexture = FALL_HEAD, description = "gui.action.entity.velocity.fall")
    private boolean resetFallDistance;

    public SetVelocityAction() {
        this(TARGET_DEFAULT, new VectorValue(), true);
    }

    public SetVelocityAction(boolean target, VectorValue vector, boolean resetFallDistance) {
        super(target);
        this.vector = vector;
        this.resetFallDistance = resetFallDistance;
    }

    public SetVelocityAction(Map<String, Object> map) {
        super(map);
        this.vector = (VectorValue) map.getOrDefault("vector", new VectorValue());
        this.resetFallDistance = (boolean) map.getOrDefault("resetFallDistance", true);
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        LivingEntity entity = getEntity(target, source);
        if (entity == null) {
            return ActionResult.FAILURE;
        }

        entity.setVelocity(vector.getRealValue(target, source).computeVector(target, source));

        if (resetFallDistance) {
            entity.setFallDistance(0);
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public String getName() {
        return "&6&lSet entity velocity: &c" + vector.getName();
    }

    @Override
    public Action clone() {
        return new SetVelocityAction(target, vector.clone(), resetFallDistance);
    }
}
