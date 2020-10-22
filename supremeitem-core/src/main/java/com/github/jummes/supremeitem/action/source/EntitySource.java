package com.github.jummes.supremeitem.action.source;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.MainHand;

@AllArgsConstructor
public class EntitySource implements Source {
    private final @NonNull LivingEntity source;
    @Getter
    private final MainHand hand;

    public EntitySource(LivingEntity source) {
        this(source, MainHand.RIGHT);
    }

    @Override
    public LivingEntity getCaster() {
        return source;
    }

    @Override
    public Location getLocation() {
        return source.getLocation();
    }
}
