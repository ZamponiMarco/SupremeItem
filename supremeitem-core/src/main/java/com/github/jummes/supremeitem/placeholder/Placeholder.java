package com.github.jummes.supremeitem.placeholder;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.model.Model;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.bool.BooleanPlaceholder;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;

@Enumerable.Parent(classArray = {NumericPlaceholder.class, BooleanPlaceholder.class})
public abstract class Placeholder<S> implements Model {

    public abstract S computePlaceholder(Target target, Source source);

}
