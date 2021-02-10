package com.github.jummes.supremeitem.placeholder.vector.operator;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.math.Vector;
import com.github.jummes.supremeitem.placeholder.vector.VectorPlaceholder;
import com.github.jummes.supremeitem.value.NumericValue;
import com.github.jummes.supremeitem.value.VectorValue;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lVector Rotate Placeholder", description = "gui.placeholder.vector.operator.rotate.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTFkNzIwY2QzOWRmM2JlNzRiMGNhYzc1ZTM5MzdmMDA4NWEzNzgyNDc0M2NhZDYzMzBkYzlmNDY2NmE0NTEwZCJ9fX0=")
public class VectorRotatePlaceholder extends VectorOperatorPlaceholder {

    private static final String ROTATE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWFjYWNiODM2YzVlNDI4YjQ5YjVkMjI0Y2FiMjI4MjhlZmUyZjZjNzA0Zjc1OTM2NGQ3MWM2NTZlMzAxNDIwIn19fQ==";
    private static final String AXIS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGJlNjIyNmQ2MWVjY2NkYjU3MzJiNmY3MTU2MGQ2NDAxZDJjYTBlZmY4ZTFhYWZiYmUzY2I3M2ZiNWE5ZiJ9fX0=";
    private static final String ANGLE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzg2NzExOTgzODJkZTkzZTFkM2M3ODM0ZGU4NjcwNGE2ZWNjNzkxNDE5ZjBkZGI0OWE0MWE5NjA4YWQ0NzIifX19";

    @Serializable(headTexture = ROTATE_HEAD, description = "gui.placeholder.vector.operator.rotate.vector",
            additionalDescription = {"gui.additional-tooltips.value"})
    private VectorValue toRotate;
    @Serializable(headTexture = AXIS_HEAD, description = "gui.placeholder.vector.operator.rotate.axis",
            additionalDescription = {"gui.additional-tooltips.value"})
    private VectorValue axis;

    @Serializable(headTexture = ANGLE_HEAD, description = "gui.placeholder.vector.operator.rotate.angle",
            additionalDescription = {"gui.additional-tooltips.value"})
    private NumericValue angle;

    public VectorRotatePlaceholder() {
        this(TARGET_DEFAULT, new VectorValue(), new VectorValue(), new NumericValue());
    }

    public VectorRotatePlaceholder(boolean target, VectorValue toRotate, VectorValue axis, NumericValue angle) {
        super(target);
        this.toRotate = toRotate;
        this.axis = axis;
        this.angle = angle;
    }

    public VectorRotatePlaceholder(Map<String, Object> map) {
        super(map);
        this.toRotate = (VectorValue) map.getOrDefault("toRotate", new VectorValue());
        this.axis = (VectorValue) map.getOrDefault("axis", new VectorValue());
        this.angle = (NumericValue) map.getOrDefault("angle", new NumericValue());
    }

    @Override
    public VectorPlaceholder clone() {
        return new VectorRotatePlaceholder(TARGET_DEFAULT, toRotate.clone(), axis.clone(), angle.clone());
    }

    @Override
    public Vector computePlaceholder(Target target, Source source) {
        Vector one = toRotate.getRealValue(target, source);
        Vector two = axis.getRealValue(target, source);
        try {
            return new Vector(one.computeVector(target, source).rotateAroundAxis(two.computeVector(target, source),
                    angle.getRealValue(target, source)));
        } catch (Exception e) {
            return new Vector();
        }
    }

    @Override
    public String getName() {
        return String.format("Rotate %s around %s for %s degrees", toRotate.getName(), axis.getName(), angle.getName());
    }

}
