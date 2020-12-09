package com.github.jummes.supremeitem.condition.string;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.condition.Condition;

import java.util.Map;

@Enumerable.Parent(classArray = {StringEqualsCondition.class})
@Enumerable.Displayable(name = "&c&lString Condition", description = "gui.condition.string.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWZjMTMwMWVhYTgyMzZhMWVhYjNmN2ViYjM4ZDgwZjUwZWU4OTY0OWY4MWUyZGUxMjY0MTVjOTM1ZjhjZTEifX19")
public abstract class StringCondition extends Condition {

    public StringCondition(boolean negate) {
        super(negate);
    }

    public StringCondition(Map<String, Object> map) {
        super(map);
    }
}
