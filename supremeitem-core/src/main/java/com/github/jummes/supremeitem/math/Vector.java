package com.github.jummes.supremeitem.math;

import com.github.jummes.libs.annotation.GUINameable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.NumericValue;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

@GUINameable(GUIName = "toString")
@Getter
@Setter
public class Vector implements Model, Cloneable {

    private static final String X_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkxZDZlZGE4M2VkMmMyNGRjZGNjYjFlMzNkZjM2OTRlZWUzOTdhNTcwMTIyNTViZmM1NmEzYzI0NGJjYzQ3NCJ9fX0=";
    private static final String Y_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODlmZjhjNzQ0OTUwNzI5ZjU4Y2I0ZTY2ZGM2OGVhZjYyZDAxMDZmOGE1MzE1MjkxMzNiZWQxZDU1ZTMifX19=";
    private static final String Z_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzA1ZjE4ZDQxNmY2OGU5YmQxOWQ1NWRmOWZhNzQyZWRmYmYxYTUyNWM4ZTI5ZjY1OWFlODMzYWYyMTdkNTM1In19fQ===";

    @Serializable(headTexture = Y_HEAD, description = "gui.vector.y")
    private NumericValue y;
    @Serializable(headTexture = X_HEAD, description = "gui.vector.x")
    private NumericValue x;
    @Serializable(headTexture = Z_HEAD, description = "gui.vector.z")
    private NumericValue z;

    public Vector() {
        this(new NumericValue(), new NumericValue(), new NumericValue());
    }

    public Vector(NumericValue x, NumericValue y, NumericValue z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector deserialize(Map<String, Object> map) {
        NumericValue x = (NumericValue) map.get("x");
        NumericValue y = (NumericValue) map.get("y");
        NumericValue z = (NumericValue) map.get("z");
        return new Vector(x, y, z);
    }

    public org.bukkit.util.Vector computeVector(Target target, Source source) {
        return new org.bukkit.util.Vector(x.getRealValue(target, source),
                y.getRealValue(target, source), z.getRealValue(target, source));
    }

    @Override
    public String toString() {
        NumberFormat f = new DecimalFormat("#0.00");
        return MessageUtils
                .color(String.format("&c%s&6/&c%s&6/&c%s", f.format(x), f.format(y), f.format(z)));
    }

    public Vector clone() {
        return new Vector(x.clone(), y.clone(), z.clone());
    }
}
