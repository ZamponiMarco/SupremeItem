package com.github.jummes.supremeitem.placeholder.numeric.entity.player;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lBalance Placeholder", condition = "vaultEnabled", description = "gui.placeholder.double.entity.player.balance.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWUyNWRiZTQ3NjY3ZDBjZTIzMWJhYTIyM2RlZTk1M2JiZmM5Njk2MDk3Mjc5ZDcyMzcwM2QyY2MzMzk3NjQ5ZSJ9fX0")
public class BalancePlaceholder extends PlayerNumericPlaceholder {

    public BalancePlaceholder() {
        this(TARGET_DEFAULT);
    }

    public BalancePlaceholder(boolean target) {
        super(target);
    }

    public static BalancePlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        return new BalancePlaceholder(target);
    }

    public static boolean vaultEnabled(ModelPath path) {
        return SupremeItem.getInstance().getVaultHook().isEnabled();
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        if (!SupremeItem.getInstance().getVaultHook().isEnabled()) {
            return Double.NaN;
        }

        LivingEntity entity = getEntity(target, source);
        if (!(entity instanceof Player)) {
            return Double.NaN;
        }

        return SupremeItem.getInstance().getVaultHook().getBalance((Player) entity);
    }

    @Override
    public String getName() {
        return String.format("%s Balance", target ? "Target" : "Source");
    }

    @Override
    public NumericPlaceholder clone() {
        return new BalancePlaceholder(target);
    }
}
