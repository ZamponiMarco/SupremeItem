package com.github.jummes.supremeitem.action.location;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.entity.selector.EntitySelector;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public abstract class PacketAction extends LocationAction {

    private static final String ALLOW_MATERIALS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2U2MTdiZWQ4ZTk3ZDQwODc5OTNlYzBjODk4Zjg3NzJjNDUyYjk5ZDhiNGI5YTQ1ZTNlYzQzNDkzMDQwOWVlOSJ9fX0";
    @Serializable(headTexture = ALLOW_MATERIALS_HEAD, description = "gui.action.location.set-block.allow-materials")
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
        return l.getNearbyPlayers(range, 64, range, select::test);
    }

}
