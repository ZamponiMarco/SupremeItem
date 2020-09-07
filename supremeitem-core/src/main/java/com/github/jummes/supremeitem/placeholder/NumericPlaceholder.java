package com.github.jummes.supremeitem.placeholder;

import com.github.jummes.libs.annotation.Enumerable;

@Enumerable.Parent(classArray = {ConstantNumberPlaceholder.class, TargetHealthPlaceholder.class})
public abstract class NumericPlaceholder extends Placeholder<Double> {
}
