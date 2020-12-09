package com.github.jummes.supremeitem.condition.bool;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.condition.Condition;

import java.util.Map;

@Enumerable.Parent(classArray = {BooleanCondition.class, AndCondition.class, OrCondition.class})
@Enumerable.Displayable(name = "&c&lTrue/False Condition", description = "gui.condition.true-false.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Y3YmMyYzFmNzdiM2Y5NTZjNTViZjU0NjY1YjdkYTVkOTYxMTFjY2M3NmY3NTdiN2I5ZmNkNTNlMjgxZjQifX19")
public abstract class TrueFalseCondition extends Condition {

    public TrueFalseCondition(boolean negate) {
        super(negate);
    }

    public TrueFalseCondition(Map<String, Object> map) {
        super(map);
    }
}
