package com.github.jummes.supremeitem.placeholder.string;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.placeholder.Placeholder;

@Enumerable.Parent(classArray = {PlayerNamePlaceholder.class, WorldNamePlaceholder.class, ObjectToStringPlaceholder.class})
public abstract class StringPlaceholder extends Placeholder<String> {
    public StringPlaceholder(boolean target) {
        super(target);
    }

    public abstract StringPlaceholder clone();
}
