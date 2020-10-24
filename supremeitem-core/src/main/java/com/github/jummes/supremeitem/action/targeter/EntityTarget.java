package com.github.jummes.supremeitem.action.targeter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

@AllArgsConstructor
@Getter
public class EntityTarget implements Target {

    private final LivingEntity target;

    @Override
    public Location getLocation() {
        return target.getLocation();
    }
}
