package com.github.jummes.supremeitem.condition;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;

import java.util.Map;

@Enumerable.Child
public class AlwaysTrueCondition extends Condition {

    public AlwaysTrueCondition() {
        this(NEGATE_DEFAULT);
    }

    public AlwaysTrueCondition(boolean negate) {
        super(negate);
    }

    public static AlwaysTrueCondition deserialize(Map<String, Object> map) {
        boolean negate = (boolean) map.getOrDefault("negate", NEGATE_DEFAULT);
        return new AlwaysTrueCondition(negate);
    }

    @Override
    public boolean testCondition(Target target, Source source) {
        return true;
    }

    @Override
    public String getName() {
        return !negate ? "&cTrue" : "&cFalse";
    }

    @Override
    public Condition clone() {
        return new AlwaysTrueCondition(negate);
    }
}
