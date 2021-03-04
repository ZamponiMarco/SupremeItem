package com.github.jummes.supremeitem.action.location;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import org.bukkit.Location;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lLightning Action", description = "gui.action.location.lightning.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzNkNjlhNjBkOTcwYWQwYjhhYTE1ODk3OTE0ZjVhYWMyNjVlOTllNmY1MDE2YTdkOGFhN2JlOWFjMDNiNjE0OCJ9fX0=")
public class LightningAction extends LocationAction {

    private static final String EFFECT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWVlMTE4ZWRkYWVlMGRmYjJjYmMyYzNkNTljMTNhNDFhN2Q2OGNjZTk0NWU0MjE2N2FhMWRjYjhkMDY3MDUxNyJ9fX0=";
    private static final String SILENT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGYxMzBmNDg1YzNmNzY5N2YzMjBkZGMxMTI4Y2QzZjE3Y2RiZDM3OTE3NjRmN2E3YmI5NWNmMjUyNzM4NTg4In19fQ==";

    private static boolean EFFECT_DEFAULT = false;
    private static boolean SILENT_DEFAULT = false;

    @Serializable(headTexture = EFFECT_HEAD, description = "gui.action.location.lightning.effect")
    @Serializable.Optional(defaultValue = "EFFECT_DEFAULT")
    private boolean effect;
    @Serializable(headTexture = SILENT_HEAD, description = "gui.action.location.lightning.silent")
    @Serializable.Optional(defaultValue = "SILENT_DEFAULT")
    private boolean silent;

    public LightningAction() {
        this(TARGET_DEFAULT, EFFECT_DEFAULT, SILENT_DEFAULT);
    }

    public LightningAction(boolean target, boolean effect, boolean silent) {
        super(target);
        this.effect = effect;
        this.silent = silent;
    }

    public LightningAction(Map<String, Object> map) {
        super(map);
        this.effect = (boolean) map.getOrDefault("effect", EFFECT_DEFAULT);
        this.silent = (boolean) map.getOrDefault("silent", SILENT_DEFAULT);
    }

    @Override
    public ActionResult execute(Target target, Source source, Map<String, Object> map) {
        Location l = getLocation(target, source);

        if (l == null) {
            return ActionResult.FAILURE;
        }

        if (effect) {
            l.getWorld().spigot().strikeLightningEffect(l, silent);
        } else {
            l.getWorld().spigot().strikeLightning(l, silent);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public String getName() {
        return "&c&lSummon Lightning";
    }

    @Override
    public Action clone() {
        return new LightningAction(target, effect, silent);
    }
}
