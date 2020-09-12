package com.github.jummes.supremeitem.condition.numeric;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.condition.Condition;
import com.github.jummes.supremeitem.placeholder.numeric.ConstantNumberPlaceholder;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import lombok.AllArgsConstructor;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&6&lLess Than Condition", description = "gui.condition.less-than.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzg2NzExOTgzODJkZTkzZTFkM2M3ODM0ZGU4NjcwNGE2ZWNjNzkxNDE5ZjBkZGI0OWE0MWE5NjA4YWQ0NzIifX19")
public class LessThanCondition extends Condition {

    private static final String ONE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTBhMTllMjNkMjFmMmRiMDYzY2M1NWU5OWFlODc0ZGM4YjIzYmU3NzliZTM0ZTUyZTdjN2I5YTI1In19fQ==";
    private static final String TWO_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2M1OTZhNDFkYWVhNTFiZTJlOWZlYzdkZTJkODkwNjhlMmZhNjFjOWQ1N2E4YmRkZTQ0YjU1OTM3YjYwMzcifX19";

    @Serializable(headTexture = ONE_HEAD, description = "gui.action.less-than.operand-one")
    private NumericPlaceholder operandOne;
    @Serializable(headTexture = TWO_HEAD, description = "gui.action.less-than.operand-two")
    private NumericPlaceholder operandTwo;

    public LessThanCondition() {
        this(false, new ConstantNumberPlaceholder(), new ConstantNumberPlaceholder());
    }

    public LessThanCondition(boolean negate, NumericPlaceholder operandOne, NumericPlaceholder operandTwo) {
        super(negate);
        this.operandOne = operandOne;
        this.operandTwo = operandTwo;
    }

    public static LessThanCondition deserialize(Map<String, Object> map) {
        boolean negate = (boolean) map.get("negate");
        NumericPlaceholder operandOne = (NumericPlaceholder) map.get("operandOne");
        NumericPlaceholder operandTwo = (NumericPlaceholder) map.get("operandTwo");
        return new LessThanCondition(negate, operandOne, operandTwo);
    }

    @Override
    public boolean testCondition(Target target, Source source) {
        return operandOne.computePlaceholder(target, source) < operandTwo.computePlaceholder(target, source);
    }
}
