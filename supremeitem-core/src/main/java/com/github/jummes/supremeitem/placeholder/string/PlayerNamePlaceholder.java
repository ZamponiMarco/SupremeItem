package com.github.jummes.supremeitem.placeholder.string;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lPlayer Name Placeholder", description = "gui.placeholder.string.player-name.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjVlNTIyMzMxN2E4OTBhMzAzNTFmNmY3OGQwYWJmOGRkNzZjYmQwOGRmNmY5MTg4ODM5MzQ1NjRkMjhlNThlIn19fQ==")
public class PlayerNamePlaceholder extends StringPlaceholder {
    public PlayerNamePlaceholder(boolean target) {
        super(target);
    }

    public PlayerNamePlaceholder() {
        this(TARGET_DEFAULT);
    }

    public static PlayerNamePlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        return new PlayerNamePlaceholder(target);
    }

    @Override
    public StringPlaceholder clone() {
        return new PlayerNamePlaceholder(target);
    }

    @Override
    public String computePlaceholder(Target target, Source source) {
        if (this.target) {
            if (target instanceof EntityTarget) {
                LivingEntity livingEntity = ((EntityTarget) target).getTarget();
                if (livingEntity instanceof Player) {
                    Player player = (Player) livingEntity;
                    return player.getName();
                }
            }
        }
        if (source instanceof EntitySource) {
            LivingEntity livingEntity = ((EntitySource) source).getSource();
            if (livingEntity instanceof Player) {
                Player player = (Player) livingEntity;
                return player.getName();
            }
        }
        return "";
    }

    @Override
    public String getName() {
        return String.format("%s Player Name", target ? "Target" : "Source");
    }
}
