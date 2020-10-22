package com.github.jummes.supremeitem.action.source;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.MainHand;

@AllArgsConstructor
@Getter
public class EntitySource implements Source {
    private final @NonNull LivingEntity source;
    private final MainHand hand;

    public EntitySource(LivingEntity source) {
        this(source, MainHand.RIGHT);
    }
}
