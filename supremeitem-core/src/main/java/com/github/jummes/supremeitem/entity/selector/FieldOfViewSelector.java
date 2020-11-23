package com.github.jummes.supremeitem.entity.selector;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.NumericValue;
import org.bukkit.entity.LivingEntity;

import java.util.Map;
import java.util.function.Predicate;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lFilter entities in FOV", description = "gui.entity.selector.fov.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg0OTI5NjgxNzFiOWZhYjk4Njg0MDU1MDNjNTc2YzQyNzIzNzQ0YTMxYmYxZTFkMGUzOWJkYTRkN2ZiMCJ9fX0=")

public class FieldOfViewSelector extends EntitySelector {

    protected static final boolean TARGET_DEFAULT = false;
    private static final NumericValue FOV_DEFAULT = new NumericValue(90);

    private static final String TARGET_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc4N2I3YWZiNWE1OTk1Mzk3NWJiYTI0NzM3NDliNjAxZDU0ZDZmOTNjZWFjN2EwMmFjNjlhYWU3ZjliOCJ9fX0==";
    private static final String FOV_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg0OTI5NjgxNzFiOWZhYjk4Njg0MDU1MDNjNTc2YzQyNzIzNzQ0YTMxYmYxZTFkMGUzOWJkYTRkN2ZiMCJ9fX0";

    @Serializable(headTexture = TARGET_HEAD, description = "gui.entity.selector.fov.target")
    private boolean target;
    @Serializable(headTexture = FOV_HEAD, description = "gui.entity.selector.fov.fov")
    @Serializable.Number(minValue = 0, maxValue = 360, scale = 1)
    private NumericValue fov;

    public FieldOfViewSelector() {
        this(DENY_DEFAULT, TARGET_DEFAULT, FOV_DEFAULT.clone());
    }

    public FieldOfViewSelector(Map<String, Object> map) {
        super(map);
        this.target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        this.fov = (NumericValue) map.getOrDefault("fov", FOV_DEFAULT.clone());
    }

    public FieldOfViewSelector(boolean deny, boolean target, NumericValue fov) {
        super(deny);
        this.target = target;
        this.fov = fov;
    }

    @Override
    public Predicate<LivingEntity> getAbstractFilter(Source source, Target target) {
        return entity -> {
            if (this.target && !(target instanceof EntityTarget) || !this.target && !(source instanceof EntitySource)) {
                return true;
            }
            LivingEntity e = getEntity(target, source);
            float toCheck = entity.getLocation().clone().subtract(e.getLocation()).getYaw();
            float fov = this.fov.getRealValue(target, source).floatValue();
            float minYaw = e.getLocation().getYaw() - fov / 2.0f;
            float maxYaw = minYaw + fov;
            return toCheck > minYaw && toCheck < maxYaw;
        };
    }

    private LivingEntity getEntity(Target target, Source source) {
        if (this.target) {
            return ((EntityTarget) target).getTarget();
        }
        return source.getCaster();
    }

    @Override
    public EntitySelector clone() {
        return new FieldOfViewSelector(deny, target, fov.clone());
    }

    @Override
    protected String getAbstractName() {
        return "&cFov &6&lselector";
    }
}
