package com.github.jummes.supremeitem.placeholder.numeric.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import com.github.jummes.supremeitem.placeholder.numeric.entity.player.PlayerNumericPlaceholder;

@Enumerable.Parent(classArray = {PlayerNumericPlaceholder.class, HealthPlaceholder.class, MaxHealthPlaceholder.class,
        NumericVariablePlaceholder.class})
@Enumerable.Displayable(name = "&c&lEntity Placeholders", description = "gui.placeholder.double.entity.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZmYzg1NGJiODRjZjRiNzY5NzI5Nzk3M2UwMmI3OWJjMTA2OTg0NjBiNTFhNjM5YzYwZTVlNDE3NzM0ZTExIn19fQ==")
public abstract class EntityNumericPlaceholder extends NumericPlaceholder {
    public EntityNumericPlaceholder(boolean target) {
        super(target);
    }
}
