package com.github.jummes.supremeitem.placeholder.bool;

import com.github.jummes.libs.annotation.Enumerable;
import org.bukkit.entity.Player;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lIs Blocking Placeholder", description = "gui.placeholder.boolean.is-blocking.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjUyNTU5ZjJiY2VhZDk4M2Y0YjY1NjFjMmI1ZjJiNTg4ZjBkNjExNmQ0NDY2NmNlZmYxMjAyMDc5ZDI3Y2E3NCJ9fX0")
public class IsBlockingPlaceholder extends PlayerPropertyBooleanPlaceholder {
    public IsBlockingPlaceholder() {
        this(TARGET_DEFAULT);
    }

    public IsBlockingPlaceholder(boolean target) {
        super(target);
    }

    public IsBlockingPlaceholder(Map<String, Object> map) {
        super(map);
    }

    @Override
    public BooleanPlaceholder clone() {
        return new IsFlyingPlaceholder(target);
    }

    @Override
    public String getName() {
        return String.format("%s Is Flying", target ? "Target" : "Source");
    }

    @Override
    protected Boolean getPropertyValue(Player p) {
        return p.isBlocking();
    }
}
