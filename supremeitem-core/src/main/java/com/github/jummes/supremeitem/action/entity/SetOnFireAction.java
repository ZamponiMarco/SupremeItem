package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.NumericValue;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

@Getter
@Setter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lSet On Fire", description = "gui.action.set-fire.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkNTdiNWJjOWFiM2Y1M2VjOWNjMmY5NGI3MmMxMzRhY2RlODU1YTY0M2MyNWU1YTI2YzNlMGIyYTYwM2FkZCJ9fX0")
public class SetOnFireAction extends EntityAction {

    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0=";

    @Serializable(headTexture = HEAD, description = "gui.action.set-fire.ticks", additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Number(scale = 1, minValue = 0)
    private NumericValue ticks;

    public SetOnFireAction(boolean target, NumericValue ticks) {
        super(target);
        this.ticks = ticks;
    }

    public SetOnFireAction() {
        this(TARGET_DEFAULT, new NumericValue());
    }

    public static SetOnFireAction deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        NumericValue ticks = (NumericValue) map.get("ticks");
        return new SetOnFireAction(target, ticks);
    }

    @Override
    protected ActionResult execute(Target target, Source source) {
        LivingEntity e = getEntity(target, source);

        if (e == null) {
            return ActionResult.FAILURE;
        }

        e.setFireTicks(ticks.getRealValue(target, source).intValue());
        return ActionResult.SUCCESS;
    }

    @Override
    public Action clone() {
        return new SetOnFireAction(target, ticks);
    }

    @Override
    public String getName() {
        return String.format("&6&lSet On Fire: &c%s ticks", ticks.getName());
    }
}
