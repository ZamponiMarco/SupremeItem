package com.github.jummes.supremeitem.placeholder.bool;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import org.bukkit.entity.Player;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lHas Permission Placeholder", description = "gui.placeholder.boolean.has-permission.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkMWFiYTczZjYzOWY0YmM0MmJkNDgxOTZjNzE1MTk3YmUyNzEyYzNiOTYyYzk3ZWJmOWU5ZWQ4ZWZhMDI1In19fQ")
public class HasPermissionPlaceholder extends PlayerPropertyBooleanPlaceholder {

    private static final String PERMISSION_DEFAULT = "permission.example";

    private static final String PERMISSION_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkMWFiYTczZjYzOWY0YmM0MmJkNDgxOTZjNzE1MTk3YmUyNzEyYzNiOTYyYzk3ZWJmOWU5ZWQ4ZWZhMDI1In19fQ==";

    @Serializable(headTexture = PERMISSION_HEAD, description = "gui.placeholder.boolean.has-permission.permission")
    @Serializable.Optional(defaultValue = "PERMISSION_DEFAULT")
    private String permission;

    public HasPermissionPlaceholder() {
        this(TARGET_DEFAULT, PERMISSION_DEFAULT);
    }

    public HasPermissionPlaceholder(boolean target, String permission) {
        super(target);
        this.permission = permission;
    }

    public static HasPermissionPlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        String permission = (String) map.getOrDefault("permission", PERMISSION_DEFAULT);
        return new HasPermissionPlaceholder(target, permission);
    }

    @Override
    protected Boolean getPropertyValue(Player p) {
        return p.hasPermission(permission);
    }

    @Override
    public String getName() {
        return String.format("%s Has Permission '%s'", target ? "Target" : "Source", permission);
    }
}
