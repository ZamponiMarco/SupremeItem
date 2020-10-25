package com.github.jummes.supremeitem.placeholder.numeric;

import com.github.jummes.libs.annotation.CustomClickable;
import com.github.jummes.libs.annotation.GUINameable;
import com.github.jummes.supremeitem.value.Value;
import lombok.Getter;

import java.util.Map;

@GUINameable(GUIName = "getName")
@Getter
@CustomClickable(customFieldClickConsumer = "getCustomClickConsumer")
public class NumericValue extends Value<Double, NumericPlaceholder> {

    public NumericValue(boolean objectValue, Double value, NumericPlaceholder placeholderValue) {
        super(objectValue, value, placeholderValue);
    }

    public static NumericValue deserialize(Map<String, Object> map) {
        boolean objectValue = (boolean) map.getOrDefault("objectValue", DOUBLE_VALUE_DEFAULT);
        double value = 10.0;
        NumericPlaceholder placeholderValue = new HealthPlaceholder();
        if (objectValue) {
            value = (double) map.get("value");
        } else {
            placeholderValue = (NumericPlaceholder) map.get("placeholderValue");
        }
        return new NumericValue(objectValue, value, placeholderValue);
    }

    @Override
    public Value<Double, NumericPlaceholder> clone() {
        return null;
    }
}
