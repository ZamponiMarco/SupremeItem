package com.github.jummes.supremeitem.condition;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lAlways True", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzdiNjJkMjc1ZDg3YzA5Y2UxMGFjYmNjZjM0YzRiYTBiNWYxMzVkNjQzZGM1MzdkYTFmMWRmMzU1YTIyNWU4MiJ9fX0")
public class AlwaysTrueCondition extends Condition {

    public AlwaysTrueCondition() {
        this(NEGATE_DEFAULT);
    }

    public AlwaysTrueCondition(boolean negate) {
        super(negate);
    }

    public AlwaysTrueCondition(Map<String, Object> map) {
        super(map);
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
