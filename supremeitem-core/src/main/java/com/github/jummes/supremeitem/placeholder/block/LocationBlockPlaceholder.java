package com.github.jummes.supremeitem.placeholder.block;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.math.Vector;
import com.github.jummes.supremeitem.value.VectorValue;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.Map;

@Enumerable.Child
@Getter
@Setter
public class LocationBlockPlaceholder extends BlockPlaceholder {

    private static final String PLACEHOLDER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzcyMzcwNGE5ZDU5MTBiOWNkNTA1ZGM5OWM3NzliZjUwMzc5Y2I4NDc0NWNjNzE5ZTlmNzg0ZGQ4YyJ9fX0=";

    @Serializable(headTexture = PLACEHOLDER_HEAD, description = "gui.placeholder.string.to-string.placeholder",
            additionalDescription = {"gui.additional-tooltips.value"})
    private VectorValue vector;

    public LocationBlockPlaceholder() {
        this(TARGET_DEFAULT, new VectorValue());
    }

    public LocationBlockPlaceholder(boolean target, VectorValue vector) {
        super(target);
        this.vector = vector;
    }

    public LocationBlockPlaceholder(Map<String, Object> map) {
        super(map);
        try {
            this.vector = (VectorValue) map.getOrDefault("vector", new VectorValue());
        } catch (ClassCastException e) {
            this.vector = new VectorValue((Vector) map.getOrDefault("vector", new Vector()));
        }
    }

    @Override
    public Block computePlaceholder(Target target, Source source) {
        Location l;
        if (this.target) {
            l = target.getLocation();
        } else {
            l = source.getLocation();
        }

        return l.clone().add(vector.getRealValue(target, source).computeVector(target, source)).getBlock();
    }

    @Override
    public String getName() {
        return String.format("&cBlock &6&l(&c%s&6&l/&c%s&6&l)&c", target ? "Target" : "Source", vector.toString());
    }

    @Override
    public BlockPlaceholder clone() {
        return new LocationBlockPlaceholder(target, vector.clone());
    }
}
