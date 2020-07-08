package com.github.jummes.supremeitem.action.source;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.MainHand;

@AllArgsConstructor
@Getter
public class EntitySource implements Source {
    private final LivingEntity source;
    private final MainHand hand;

    public EntitySource(LivingEntity source) {
        this(source, MainHand.RIGHT);
    }
}
