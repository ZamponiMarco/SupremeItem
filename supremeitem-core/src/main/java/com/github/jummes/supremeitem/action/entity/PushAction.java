package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.LocationSource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.NumericValue;
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
@Enumerable.Displayable(name = "&c&lPush action", description = "gui.action.push.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjU0ZmFiYjE2NjRiOGI0ZDhkYjI4ODk0NzZjNmZlZGRiYjQ1MDVlYmE0Mjg3OGM2NTNhNWQ3OTNmNzE5YjE2In19fQ==")
public class PushAction extends EntityAction {

    private static final NumericValue HORIZONTAL_DEFAULT = new NumericValue(1.0);
    private static final NumericValue VERTICAL_DEFAULT = new NumericValue(0.5);

    private static final String HORIZONTAL_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNmYzUyMjY0ZDhhZDllNjU0ZjQxNWJlZjAxYTIzOTQ3ZWRiY2NjY2Y2NDkzNzMyODliZWE0ZDE0OTU0MWY3MCJ9fX0=";
    private static final String VERTICAL_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk5YWFmMjQ1NmE2MTIyZGU4ZjZiNjI2ODNmMmJjMmVlZDlhYmI4MWZkNWJlYTFiNGMyM2E1ODE1NmI2NjkifX19";

    @Serializable(headTexture = HORIZONTAL_HEAD, description = "gui.action.push.horizontal", additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Optional(defaultValue = "HORIZONTAL_DEFAULT")
    private NumericValue horizontalVelocity;

    @Serializable(headTexture = VERTICAL_HEAD, description = "gui.action.push.vertical", additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Optional(defaultValue = "VERTICAL_DEFAULT")
    private NumericValue verticalVelocity;

    public PushAction() {
        this(HORIZONTAL_DEFAULT.clone(), VERTICAL_DEFAULT.clone());
    }

    public static PushAction deserialize(Map<String, Object> map) {
        NumericValue horizontalVelocity;
        NumericValue verticalVelocity;
        try {
            horizontalVelocity = (NumericValue) map.getOrDefault("horizontalVelocity", HORIZONTAL_DEFAULT.clone());
            verticalVelocity = (NumericValue) map.getOrDefault("verticalVelocity", VERTICAL_DEFAULT.clone());
        } catch (ClassCastException e) {

            horizontalVelocity = new NumericValue(((Number) map.getOrDefault("horizontalVelocity", HORIZONTAL_DEFAULT.getValue())));
            verticalVelocity = new NumericValue(((Number) map.getOrDefault("verticalVelocity", VERTICAL_DEFAULT.getValue())));
        }
        return new PushAction(horizontalVelocity, verticalVelocity);
    }

    @Override
    protected ActionResult execute(Target target, Source source) {
        Vector difference = null;
        LivingEntity entityTarget = ((EntityTarget) target).getTarget();
        if (source instanceof EntitySource) {
            LivingEntity entitySource = source.getCaster();
            if (entitySource.equals(entityTarget)) {
                difference = entityTarget.getLocation().getDirection();
            } else {
                difference = entityTarget.getLocation().toVector().
                        subtract((entitySource.getLocation().toVector()));
            }
        } else if (source instanceof LocationSource) {
            difference = entityTarget.getLocation().toVector().
                    subtract(source.getLocation().toVector());
        }
        if (difference != null) {
            difference.normalize();
            double hV = horizontalVelocity.getRealValue(target, source);
            double vV = verticalVelocity.getRealValue(target, source);
            if (Double.isFinite(difference.getX()) && Double.isFinite(difference.getY())
                    && Double.isFinite(difference.getZ())) {
                difference.setX(difference.getX() * hV);
                difference.setZ(difference.getZ() * hV);
                difference.setY(vV);
                entityTarget.setVelocity(difference.setY(0).multiply(hV)
                        .add(new Vector(0, vV, 0)));
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.FAILURE;
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(EntityTarget.class);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjU0ZmFiYjE2NjRiOGI0ZDhkYjI4ODk0NzZjNmZlZGRiYjQ1MDVlYmE0Mjg3OGM2NTNhNWQ3OTNmNzE5YjE2In19fQ="),
                "&6&lPush", Libs.getLocale().getList("gui.action.description"));
    }

    @Override
    public Action clone() {
        return new PushAction(horizontalVelocity.clone(), verticalVelocity.clone());
    }
}
