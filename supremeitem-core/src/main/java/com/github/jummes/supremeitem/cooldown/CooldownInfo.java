package com.github.jummes.supremeitem.cooldown;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CooldownInfo {
    @EqualsAndHashCode.Include
    private UUID itemId;
    private int remainingCooldown;
    @EqualsAndHashCode.Include
    private UUID skillId;
}
