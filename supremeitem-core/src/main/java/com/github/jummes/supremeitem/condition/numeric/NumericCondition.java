package com.github.jummes.supremeitem.condition.numeric;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.condition.Condition;

@Enumerable.Parent(classArray = {EqualsCondition.class, LessThanCondition.class, MoreThanCondition.class})
@Enumerable.Displayable(name = "&c&lNumeric Condition", description = "gui.condition.numeric.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==")
public abstract class NumericCondition extends Condition {
    public NumericCondition(boolean negate) {
        super(negate);
    }
}
