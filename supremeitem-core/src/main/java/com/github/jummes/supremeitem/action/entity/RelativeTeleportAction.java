package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.math.Vector;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lRelative Teleport", description = "gui.action.relative-teleport.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTU5MzM1ZDkxYmIzOWJhNjg1YmE1NjEyY2NmY2FlOGFhZGEzNDYxYTlkMDc4NjZjZDRlMGIyNjZjODY0ZTEwNyJ9fX0=")
public class RelativeTeleportAction extends EntityAction {

    private static final String VECTOR_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNmYzUyMjY0ZDhhZDllNjU0ZjQxNWJlZjAxYTIzOTQ3ZWRiY2NjY2Y2NDkzNzMyODliZWE0ZDE0OTU0MWY3MCJ9fX0";

    @Serializable(headTexture = VECTOR_HEAD, description = "gui.action.relative-teleport.coordinates")
    private Vector relativeCoordinates;

    public RelativeTeleportAction() {
        this(TARGET_DEFAULT, new Vector());
    }

    public RelativeTeleportAction(boolean target, Vector relativeCoordinates) {
        super(target);
        this.relativeCoordinates = relativeCoordinates;
    }

    public static RelativeTeleportAction deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        Vector relativeCoordinates = (Vector) map.get("relativeCoordinates");
        return new RelativeTeleportAction(target, relativeCoordinates);
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        LivingEntity entity = getEntity(target, source);

        if (entity == null) {
            return ActionResult.FAILURE;
        }

        entity.teleport(entity.getLocation().clone().add(relativeCoordinates.computeVector(target, source)));

        return ActionResult.SUCCESS;
    }

    @Override
    public Action clone() {
        return new RelativeTeleportAction(target, relativeCoordinates.clone());
    }

    @Override
    public String getName() {
        return "&6&lRelative teleport: &c" + relativeCoordinates.toString();
    }
}
