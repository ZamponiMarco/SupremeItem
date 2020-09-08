package com.github.jummes.supremeitem.condition.numeric;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.condition.Condition;

@Enumerable.Parent(classArray = {LessThanCondition.class, MoreThanCondition.class, EqualsCondition.class})
public abstract class NumericCondition extends Condition {
}
