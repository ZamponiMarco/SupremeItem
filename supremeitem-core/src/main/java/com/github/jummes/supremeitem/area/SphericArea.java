package com.github.jummes.supremeitem.area;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.NumericValue;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lSpheric area", description = "gui.area.spheric.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjcyYmNkZjZhNTQ4OTc0YmY3YjExNWFjZWU2M2NiMjg0MzY3YTBmNzQ1YmMwZmYzNTdjMTQyMzE1MjQzZDhkOSJ9fX0")
public class SphericArea extends Area {

    private static final String RANGE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWQ2YjEyOTNkYjcyOWQwMTBmNTM0Y2UxMzYxYmJjNTVhZTVhOGM4ZjgzYTE5NDdhZmU3YTg2NzMyZWZjMiJ9fX0=";
    private static final String BORDERS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA5NWE3ZmQ5MGRhYTFiYmU3MDY5MDg5NzQwZTA1ZDBiZmM2NjI5NmVlM2M0MGVlNzFhNGUwYTY2MTZiMmJiYyJ9fX0=";

    @Serializable(headTexture = RANGE_HEAD, description = "gui.area.spheric.range", additionalDescription = {"gui.additional-tooltips.value"})
    private NumericValue range;
    @Serializable(headTexture = BORDERS_HEAD, description = "gui.area.spheric.borders")
    private boolean onlyBorders;

    public SphericArea() {
        this(new NumericValue(3.0), false);
    }

    public SphericArea(NumericValue range, boolean onlyBorders) {
        this.range = range;
        this.onlyBorders = onlyBorders;
    }

    public static SphericArea deserialize(Map<String, Object> map) {
        NumericValue range = (NumericValue) map.get("range");
        boolean onlyBorders = (boolean) map.getOrDefault("onlyBorders", false);
        return new SphericArea(range, onlyBorders);
    }

    private static double lengthSq(double x, double y, double z) {
        return (x * x) + (y * y) + (z * z);
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
        double radius = range.getRealValue(target, source) + 0.5;

        final double invRadius = 1 / radius;

        final int ceilRadius = (int) Math.ceil(radius);

        double nextXn = 0;
        forX:
        for (int x = 0; x <= radius; ++x) {
            final double xn = nextXn;
            nextXn = (x + 1) * invRadius;
            double nextYn = 0;
            forY:
            for (int y = 0; y <= radius; ++y) {
                final double yn = nextYn;
                nextYn = (y + 1) * invRadius;
                double nextZn = 0;
                forZ:
                for (int z = 0; z <= radius; ++z) {
                    final double zn = nextZn;
                    nextZn = (z + 1) * invRadius;

                    double distanceSq = lengthSq(xn, yn, zn);
                    if (distanceSq > 1) {
                        if (z == 0) {
                            if (y == 0) {
                                break forX;
                            }
                            break forY;
                        }
                        break forZ;
                    }

                    if (onlyBorders) {
                        if (lengthSq(nextXn, yn, zn) <= 1 && lengthSq(xn, nextYn, zn) <= 1 && lengthSq(xn, yn, nextZn) <= 1) {
                            continue;
                        }
                    }

                    blocks.add(center.clone().add(x, y, z));
                    blocks.add(center.clone().add(-x, y, z));
                    blocks.add(center.clone().add(x, -y, z));
                    blocks.add(center.clone().add(-x, -y, z));
                    blocks.add(center.clone().add(x, y, -z));
                    blocks.add(center.clone().add(-x, y, -z));
                    blocks.add(center.clone().add(x, -y, -z));
                    blocks.add(center.clone().add(-x, -y, -z));
                }
            }
        }

        return blocks;
    }

    @Override
    public String getName() {
        return "Sphere range: " + range.getName();
    }
}
