package com.github.jummes.supremeitem.action.source;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;

@Getter
@AllArgsConstructor
public class LocationSource implements Source {
    private final Location source;
}
