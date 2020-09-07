package com.github.jummes.supremeitem.condition;

import com.github.jummes.libs.model.Model;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;

public abstract class Condition implements Model {

    public abstract boolean testCondition(Target target, Source source);
    
}
