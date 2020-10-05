package com.github.jummes.supremeitem.placeholder.bool;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.placeholder.Placeholder;

@Enumerable.Parent(classArray = {OnGroundPlaceholder.class, PossessItemPlaceholder.class, IsSneakingPlaceholder.class})
public abstract class BooleanPlaceholder extends Placeholder<Boolean> {
    public BooleanPlaceholder(boolean target) {
        super(target);
    }
}
