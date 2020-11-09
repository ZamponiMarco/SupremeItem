package com.github.jummes.supremeitem.area;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.NumericValue;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Enumerable.Child
public class CylindricArea extends Area {

    private static final String RANGE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWQ2YjEyOTNkYjcyOWQwMTBmNTM0Y2UxMzYxYmJjNTVhZTVhOGM4ZjgzYTE5NDdhZmU3YTg2NzMyZWZjMiJ9fX0=";
    @Serializable(headTexture = RANGE_HEAD, description = "gui.area.spheric.range", additionalDescription = {"gui.additional-tooltips.value"})
    private NumericValue height;
    @Serializable(headTexture = RANGE_HEAD, description = "gui.area.spheric.range", additionalDescription = {"gui.additional-tooltips.value"})
    private NumericValue range;
    @Serializable(headTexture = RANGE_HEAD, description = "gui.area.spheric.range")
    private boolean onlyBorders;

    public CylindricArea(NumericValue height, NumericValue range, boolean onlyBorders) {
        this.height = height;
        this.range = range;
        this.onlyBorders = onlyBorders;
    }

    public CylindricArea() {
        this(new NumericValue(3), new NumericValue(3), false);
    }

    public static CylindricArea deserialize(Map<String, Object> map) {
        NumericValue height = (NumericValue) map.get("height");
        NumericValue range = (NumericValue) map.get("range");
        boolean onlyBorders = (boolean) map.get("onlyBorders");
        return new CylindricArea(height, range, onlyBorders);
    }

    /**
     * Method implementation provided by WorldEdit library
     * <p>
     * WorldEdit, a Minecraft world manipulation toolkit
     * Copyright (C) sk89q <http://www.sk89q.com>
     * Copyright (C) WorldEdit team and contributors
     */
    @Override
    public List<Location> getBlocks(Location center, Target target, Source source) {
        List<Location> blocks = new ArrayList<>();
        World world = center.getWorld();
        Location pos = center.clone();
        double radius = range.getRealValue(target, source);
        int height = this.height.getRealValue(target, source).intValue();

        radius += 0.5;


        if (height == 0) {
            return blocks;
        } else if (height < 0) {
            height = -height;
            pos.subtract(0, height, 0);
        }

        if (pos.getBlockY() < 0) {
            pos.setY(0);
        } else if (pos.getBlockY() + height - 1 > 255) {
            height = 255 - pos.getBlockY() + 1;
        }

        final double invRadius = 1 / radius;

        final int ceilRadius = (int) Math.ceil(radius);

        double nextXn = 0;
        forX:
        for (int x = 0; x <= ceilRadius; ++x) {
            final double xn = nextXn;
            nextXn = (x + 1) * invRadius;
            double nextZn = 0;
            for (int z = 0; z <= ceilRadius; ++z) {
                final double zn = nextZn;
                nextZn = (z + 1) * invRadius;

                double distanceSq = lengthSq(xn, zn);
                if (distanceSq > 1) {
                    if (z == 0) {
                        break forX;
                    }
                    break;
                }

                if (onlyBorders) {
                    if (lengthSq(nextXn, zn) <= 1 && lengthSq(xn, nextZn) <= 1) {
                        continue;
                    }
                }

                for (int y = 0; y < height; ++y) {
                    blocks.add(pos.clone().add(x, y, z));
                    blocks.add(pos.clone().add(-x, y, z));
                    blocks.add(pos.clone().add(x, y, -z));
                    blocks.add(pos.clone().add(-x, y, -z));
                }
            }
        }
        return blocks;
    }

    @Override
    public String getName() {
        return "Cylinder range: " + range.getName() + ", height: " + height.getName();
    }
}
