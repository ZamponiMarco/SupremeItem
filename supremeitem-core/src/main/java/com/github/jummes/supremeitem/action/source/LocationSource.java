package com.github.jummes.supremeitem.action.source;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

@AllArgsConstructor
public class LocationSource implements Source {
    private final @NonNull Location source;
    private final @NonNull LivingEntity originalCaster;

    @Override
    public LivingEntity getCaster() {
        return originalCaster;
    }

    @Override
    public Location getLocation() {
        return source;
    }
}
