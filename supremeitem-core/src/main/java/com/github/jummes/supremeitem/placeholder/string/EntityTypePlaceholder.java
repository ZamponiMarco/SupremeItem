package com.github.jummes.supremeitem.placeholder.string;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lEntity Type Placeholder", description = "gui.placeholder.string.entity.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZmYzg1NGJiODRjZjRiNzY5NzI5Nzk3M2UwMmI3OWJjMTA2OTg0NjBiNTFhNjM5YzYwZTVlNDE3NzM0ZTExIn19fQ==")
public class EntityTypePlaceholder extends StringPlaceholder {

    public EntityTypePlaceholder() {
        this(TARGET_DEFAULT);
    }

    public EntityTypePlaceholder(boolean target) {
        super(target);
    }

    public EntityTypePlaceholder(Map<String, Object> map) {
        super(map);
    }

    @Override
    public String computePlaceholder(Target target, Source source) {
        LivingEntity e = null;
        if (this.target) {
            if (target instanceof EntityTarget) {
                e = ((EntityTarget) target).getTarget();
            }
        } else {
            e = source.getCaster();
        }

        if (e == null) {
            return "";
        }

        return e.getType().name();
    }

    @Override
    public String getName() {
        return String.format("%s Entity Name", target ? "Target" : "Source");
    }

    @Override
    public StringPlaceholder clone() {
        return new EntityTypePlaceholder(target);
    }
}
