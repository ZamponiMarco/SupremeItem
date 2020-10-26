package com.github.jummes.supremeitem.placeholder.string;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.placeholder.Placeholder;
import com.github.jummes.supremeitem.placeholder.string.block.StringBlockPlaceholder;
import com.github.jummes.supremeitem.placeholder.string.operator.StringOperatorPlaceholder;

@Enumerable.Parent(classArray = {PlayerNamePlaceholder.class, WorldNamePlaceholder.class, ObjectToStringPlaceholder.class,
        StringBlockPlaceholder.class, StringOperatorPlaceholder.class})
public abstract class StringPlaceholder extends Placeholder<String> {
    public StringPlaceholder(boolean target) {
        super(target);
    }

    public abstract StringPlaceholder clone();
}
