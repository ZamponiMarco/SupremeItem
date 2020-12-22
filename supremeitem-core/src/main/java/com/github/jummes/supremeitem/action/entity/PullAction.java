package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.LocationSource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.NumericValue;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Map;

@Getter
@Setter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lPull action", description = "gui.action.entity.pull.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGMzMDFhMTdjOTU1ODA3ZDg5ZjljNzJhMTkyMDdkMTM5M2I4YzU4YzRlNmU0MjBmNzE0ZjY5NmE4N2ZkZCJ9fX0=")
public class PullAction extends EntityAction {

    private static final NumericValue FORCE_DEFAULT = new NumericValue(1.0);

    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzZlZjgzZTZjYWIxOWNkMjM4MDdlZjIwNmQxY2E2NmU0MWJhYTNhMGZhNWJkYzllYTQ0YmJlOTZkMTg2YiJ9fX0=";

    @Serializable(headTexture = HEAD, description = "gui.action.entity.pull.force", additionalDescription = {"gui.additional-tooltips.value"})
    private NumericValue force;

    public PullAction() {
        this(TARGET_DEFAULT, FORCE_DEFAULT.clone());
    }

    public PullAction(boolean target, NumericValue force) {
        super(target);
        this.force = force;
    }

    public PullAction(Map<String, Object> map) {
        super(map);
        this.force = (NumericValue) map.getOrDefault("force", FORCE_DEFAULT.clone());
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        Vector difference = null;
        LivingEntity entityTarget = getEntity(target, source);
        LivingEntity entitySource = source.getCaster();

        if (entityTarget == null) {
            return ActionResult.FAILURE;
        }

        if (source instanceof EntitySource) {
            if (entitySource.equals(entityTarget)) {
                difference = entityTarget.getLocation().getDirection().multiply(-1);
            } else {
                difference = entitySource.getLocation().toVector().
                        subtract(entityTarget.getLocation().toVector());
            }
        } else if (source instanceof LocationSource) {
            difference = source.getLocation().toVector().
                    subtract(entityTarget.getLocation().toVector());
        }
        if (difference != null) {
            difference.normalize();
            if (Double.isFinite(difference.getX()) && Double.isFinite(difference.getY())
                    && Double.isFinite(difference.getZ())) {
                difference.multiply(force.getRealValue(target, source));
                entityTarget.setVelocity(difference);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.FAILURE;
    }

    @Override
    public Action clone() {
        return new PullAction(target, force.clone());
    }

    @Override
    public String getName() {
        return "&6&lPull";
    }
}
