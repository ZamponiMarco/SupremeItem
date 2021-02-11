package com.github.jummes.supremeitem.placeholder.bool;

import com.github.jummes.libs.annotation.Enumerable;
import org.bukkit.entity.Player;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lIs Sneaking Placeholder", description = "gui.placeholder.boolean.is-sneaking.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTQyMzI5YTljNDEwNDA4NDE5N2JkNjg4NjE1ODUzOTg0ZDM3ZTE3YzJkZDIzZTNlNDEyZGQ0MmQ3OGI5OGViIn19fQ==")
public class IsSneakingPlaceholder extends PlayerPropertyBooleanPlaceholder {
    public IsSneakingPlaceholder() {
        this(TARGET_DEFAULT);
    }

    public IsSneakingPlaceholder(boolean target) {
        super(target);
    }

    public IsSneakingPlaceholder(Map<String, Object> map) {
        super(map);
    }

    @Override
    public BooleanPlaceholder clone() {
        return new IsSneakingPlaceholder(target);
    }

    @Override
    public String getName() {
        return String.format("%s Is Sneaking", target ? "Target" : "Source");
    }

    @Override
    protected Boolean getPropertyValue(Player p) {
        return p.isSneaking();
    }
}
