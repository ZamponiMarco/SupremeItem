package com.github.jummes.supremeitem.action.source;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public interface Source {

    LivingEntity getCaster();

    Location getLocation();
}
