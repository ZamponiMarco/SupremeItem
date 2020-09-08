package com.github.jummes.supremeitem.placeholder.numeric;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.placeholder.Placeholder;

@Enumerable.Parent(classArray = {ConstantNumberPlaceholder.class, HealthPlaceholder.class})
public abstract class NumericPlaceholder extends Placeholder<Double> {
}
