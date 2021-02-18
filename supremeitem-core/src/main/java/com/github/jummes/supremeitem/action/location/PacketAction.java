package com.github.jummes.supremeitem.action.location;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.entity.selector.EntitySelector;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class PacketAction extends LocationAction {

    private static final String SELECTOR_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM4ZDI3NTk1NjlkNTE1ZDI0NTRkNGE3ODkxYTk0Y2M2M2RkZmU3MmQwM2JmZGY3NmYxZDQyNzdkNTkwIn19fQ==";

    @Serializable(headTexture = SELECTOR_HEAD, description = "gui.action.location.selectors")
    protected List<EntitySelector> selectors;

    public PacketAction(boolean target, List<EntitySelector> selectors) {
        super(target);
        this.selectors = selectors;
    }

    public PacketAction(Map<String, Object> map) {
        super(map);
        this.selectors = (List<EntitySelector>) map.getOrDefault("selectors", Lists.newArrayList());
    }

    protected Collection<Player> selectedPlayers(Location l, Target target, Source source) {
        return selectedPlayers(l, target, source, Bukkit.getViewDistance() << 4);
    }

    protected Collection<Player> selectedPlayers(Location l, Target target, Source source, int range) {
        Predicate<LivingEntity> select = selectors.stream().map(selector -> selector.getFilter(source, target)).
                reduce(e -> true, Predicate::and);
        BoundingBox box = new BoundingBox(l.getX() - range, l.getY() - 64, l.getZ() - range,
                l.getX() + range, l.getY() + 64, l.getZ() + range);

        World world = l.getWorld();

        if (world == null) {
            return Lists.newArrayList();
        }

        return l.getWorld().getPlayers().stream().filter(player -> box.contains(player.getBoundingBox()) &&
                select.test(player)).collect(Collectors.toList());
    }

}
