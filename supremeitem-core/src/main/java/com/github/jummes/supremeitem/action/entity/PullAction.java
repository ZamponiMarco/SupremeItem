package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.LocationSource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericValue;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lPull action", description = "gui.action.pull.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGMzMDFhMTdjOTU1ODA3ZDg5ZjljNzJhMTkyMDdkMTM5M2I4YzU4YzRlNmU0MjBmNzE0ZjY5NmE4N2ZkZCJ9fX0=")
public class PullAction extends EntityAction {

    private static final NumericValue FORCE_DEFAULT = new NumericValue(1.0);

    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzZlZjgzZTZjYWIxOWNkMjM4MDdlZjIwNmQxY2E2NmU0MWJhYTNhMGZhNWJkYzllYTQ0YmJlOTZkMTg2YiJ9fX0=";

    @Serializable(headTexture = HEAD, description = "gui.action.pull.force")
    private NumericValue force;

    public PullAction() {
        this(FORCE_DEFAULT.clone());
    }

    public static PullAction deserialize(Map<String, Object> map) {
        NumericValue force;
        try {
            force = (NumericValue) map.getOrDefault("force", FORCE_DEFAULT.clone());
        } catch (ClassCastException e) {
            force = new NumericValue(((Double) map.getOrDefault("force", FORCE_DEFAULT.getValue())));
        }
        return new PullAction(force);
    }

    @Override
    protected ActionResult execute(Target target, Source source) {
        Vector difference = null;
        LivingEntity entityTarget = ((EntityTarget) target).getTarget();
        if (source instanceof EntitySource) {
            LivingEntity entitySource = ((EntitySource) source).getSource();
            if (entitySource.equals(entityTarget)) {
                difference = entityTarget.getLocation().getDirection().multiply(-1);
            } else {
                difference = entitySource.getLocation().toVector().
                        subtract(entityTarget.getLocation().toVector());
            }
        } else if (source instanceof LocationSource) {
            difference = ((LocationSource) source).getSource().toVector().
                    subtract(entityTarget.getLocation().toVector());
        }
        if (difference != null) {
            difference.normalize();
            if (Double.isFinite(difference.getX()) && Double.isFinite(difference.getY())
                    && Double.isFinite(difference.getZ())) {
                difference.multiply(force.getRealValue(target, source));
                entityTarget.setVelocity(difference);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(EntityTarget.class);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGMzMDFhMTdjOTU1ODA3ZDg5ZjljNzJhMTkyMDdkMTM5M2I4YzU4YzRlNmU0MjBmNzE0ZjY5NmE4N2ZkZCJ9fX0="),
                "&6&lPull", Libs.getLocale().getList("gui.action.description"));
    }
}
