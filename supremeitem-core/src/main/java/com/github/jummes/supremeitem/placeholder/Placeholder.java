package com.github.jummes.supremeitem.placeholder;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.Model;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.bool.BooleanPlaceholder;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;

@Enumerable.Parent(classArray = {NumericPlaceholder.class, BooleanPlaceholder.class})
public abstract class Placeholder<S> implements Model, Cloneable {

    protected static final String TARGET_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc4N2I3YWZiNWE1OTk1Mzk3NWJiYTI0NzM3NDliNjAxZDU0ZDZmOTNjZWFjN2EwMmFjNjlhYWU3ZjliOCJ9fX0==";
    protected static final boolean TARGET_DEFAULT = true;
    @Serializable(headTexture = TARGET_HEAD, description = "gui.placeholder.target")
    @Serializable.Optional(defaultValue = "TARGET_DEFAULT")
    protected boolean target;

    public Placeholder(boolean target) {
        this.target = target;
    }

    public abstract S computePlaceholder(Target target, Source source);

    public abstract String getName();
}
