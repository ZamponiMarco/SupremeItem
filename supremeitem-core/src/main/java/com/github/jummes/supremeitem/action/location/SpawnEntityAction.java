package com.github.jummes.supremeitem.action.location;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.entity.Entity;
import com.github.jummes.supremeitem.entity.GenericEntity;
import org.apache.commons.lang.WordUtils;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lSpawn entity", description = "gui.action.spawn.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZmYzg1NGJiODRjZjRiNzY5NzI5Nzk3M2UwMmI3OWJjMTA2OTg0NjBiNTFhNjM5YzYwZTVlNDE3NzM0ZTExIn19fQ==")
public class SpawnEntityAction extends LocationAction {
    private static final String ENTITY_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTNjOGFhM2ZkZTI5NWZhOWY5YzI3ZjczNGJkYmFiMTFkMzNhMmU0M2U4NTVhY2NkNzQ2NTM1MjM3NzQxM2IifX19";

    @Serializable(headTexture = ENTITY_HEAD, description = "gui.action.spawn.entity", additionalDescription = {"gui.additional-tooltips.recreate"})
    private Entity entity;

    public SpawnEntityAction() {
        this(TARGET_DEFAULT, new GenericEntity());
    }

    public SpawnEntityAction(boolean target, Entity entity) {
        super(target);
        this.entity = entity;
    }

    public static SpawnEntityAction deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        Entity entity = (Entity) map.getOrDefault("entity", new GenericEntity());
        return new SpawnEntityAction(target, entity);
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        entity.spawnEntity(getLocation(target, source), target, source);
        return ActionResult.SUCCESS;
    }

    @Override
    public Action clone() {
        return new SpawnEntityAction(target, entity.clone());
    }

    @Override
    public String getName() {
        return "&6&lSpawn: &c" + WordUtils.capitalizeFully(entity.getType().name());
    }
}
