package com.github.jummes.supremeitem.condition;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.model.Model;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.condition.numeric.NumericCondition;

@Enumerable.Parent(classArray = {NumericCondition.class})
public abstract class Condition implements Model {

    public abstract boolean testCondition(Target target, Source source);

}
