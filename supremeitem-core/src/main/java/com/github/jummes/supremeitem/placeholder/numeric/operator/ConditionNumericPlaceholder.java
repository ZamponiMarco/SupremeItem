package com.github.jummes.supremeitem.placeholder.numeric.operator;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.condition.AlwaysTrueCondition;
import com.github.jummes.supremeitem.condition.Condition;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import com.github.jummes.supremeitem.value.NumericValue;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable
public class ConditionNumericPlaceholder extends NumberOperatorPlaceholder {

    @Serializable(headTexture = ONE_HEAD, description = "gui.placeholder.double.operator.operand-one")
    private Condition condition;
    @Serializable(headTexture = ONE_HEAD, description = "gui.placeholder.double.operator.operand-one")
    private NumericValue onSucceed;
    @Serializable(headTexture = ONE_HEAD, description = "gui.placeholder.double.operator.operand-one")
    private NumericValue onFail;

    public ConditionNumericPlaceholder() {
        this(TARGET_DEFAULT, new AlwaysTrueCondition(), new NumericValue(), new NumericValue());
    }

    public ConditionNumericPlaceholder(boolean target, Condition condition, NumericValue onSucceed,
                                       NumericValue onFail) {
        super(target);
        this.condition = condition;
        this.onSucceed = onSucceed;
        this.onFail = onFail;
    }

    public ConditionNumericPlaceholder(Map<String, Object> map) {
        super(map);
        this.condition = (Condition) map.getOrDefault("condition", new AlwaysTrueCondition());
        this.onSucceed = (NumericValue) map.getOrDefault("onSucceed", new NumericValue());
        this.onFail = (NumericValue) map.getOrDefault("onFail", new NumericValue());
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        return condition.checkCondition(target, source) ? onSucceed.getRealValue(target, source) :
                onFail.getRealValue(target, source);
    }

    @Override
    public String getName() {
        return String.format("If %s then %s else %s", condition.getName(), onSucceed.getName(), onFail.getName());
    }

    @Override
    public NumericPlaceholder clone() {
        return new ConditionNumericPlaceholder(TARGET_DEFAULT, condition.clone(), onSucceed.clone(), onFail.clone());
    }
}
