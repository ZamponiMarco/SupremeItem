package com.github.jummes.supremeitem.condition.numeric;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.GUINameable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.condition.Condition;
import com.github.jummes.supremeitem.value.NumericValue;

import java.util.Map;

@GUINameable(GUIName = "getName")
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lEquals Condition", description = "gui.condition.equals.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzAzMDgyZjAzM2Y5NzI0Y2IyMmZlMjdkMGRlNDk3NTA5MDMzNTY0MWVlZTVkOGQ5MjdhZGY1YThiNjdmIn19fQ==")
public class EqualsCondition extends NumericCondition {

    private static final String ONE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTBhMTllMjNkMjFmMmRiMDYzY2M1NWU5OWFlODc0ZGM4YjIzYmU3NzliZTM0ZTUyZTdjN2I5YTI1In19fQ==";
    private static final String TWO_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2M1OTZhNDFkYWVhNTFiZTJlOWZlYzdkZTJkODkwNjhlMmZhNjFjOWQ1N2E4YmRkZTQ0YjU1OTM3YjYwMzcifX19";

    @Serializable(headTexture = ONE_HEAD, description = "gui.condition.operand-one", additionalDescription = {"gui.additional-tooltips.value"})
    private NumericValue operandOne;
    @Serializable(headTexture = TWO_HEAD, description = "gui.condition.operand-two", additionalDescription = {"gui.additional-tooltips.value"})
    private NumericValue operandTwo;

    public EqualsCondition() {
        this(NEGATE_DEFAULT, new NumericValue(), new NumericValue());
    }

    public EqualsCondition(boolean negate, NumericValue operandOne, NumericValue operandTwo) {
        super(negate);
        this.operandOne = operandOne;
        this.operandTwo = operandTwo;
    }

    public EqualsCondition(Map<String, Object> map) {
        super(map);
        this.operandOne = (NumericValue) map.get("operandOne");
        this.operandTwo = (NumericValue) map.get("operandTwo");
    }

    @Override
    public boolean testCondition(Target target, Source source) {
        return operandOne.getRealValue(target, source).equals(operandTwo.getRealValue(target, source));
    }

    @Override
    public String getName() {
        return String.format("&c" + operandOne.getName() + "&6&l%s&c" + operandTwo.getName(), negate ? " ≠ " : " = ");
    }

    @Override
    public Condition clone() {
        return new EqualsCondition(negate, operandOne.clone(), operandTwo.clone());
    }
}
