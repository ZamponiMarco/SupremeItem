package com.github.jummes.supremeitem.placeholder.numeric.entity.player;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.placeholder.numeric.entity.EntityNumericPlaceholder;

@Enumerable.Parent(classArray = {BalancePlaceholder.class, HungerPlaceholder.class})
@Enumerable.Displayable(name = "&c&lPlayer Placeholders", description = "gui.placeholder.double.entity.player.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDI4NDNjM2MyMzU3MTZmM2ViNWNkOWMzYmRiZjIwODUzZjUwYTY1ZGMyMjMwNThiMWUxZWVmZmRlOTlmNjExMCJ9fX0=")
public abstract class PlayerNumericPlaceholder extends EntityNumericPlaceholder {
    public PlayerNumericPlaceholder(boolean target) {
        super(target);
    }
}
