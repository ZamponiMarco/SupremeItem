package com.github.jummes.supremeitem.value;

import com.github.jummes.libs.annotation.CustomClickable;
import com.github.jummes.libs.annotation.GUINameable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.gui.PluginInventoryHolder;
import com.github.jummes.libs.gui.model.ModelObjectInventoryHolder;
import com.github.jummes.libs.gui.setting.DoubleFieldChangeInventoryHolder;
import com.github.jummes.libs.gui.setting.change.FieldChangeInformation;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.supremeitem.placeholder.numeric.HealthPlaceholder;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@GUINameable(GUIName = "getName")
@Getter
@CustomClickable(customFieldClickConsumer = "getCustomClickConsumer")
public class NumericValue extends Value<Double, NumericPlaceholder> {

    public NumericValue() {
        this(OBJECT_VALUE_DEFAULT, 10.0, new HealthPlaceholder());
    }

    public NumericValue(boolean objectValue, Double value, NumericPlaceholder placeholderValue) {
        super(objectValue, value, placeholderValue);
    }

    public NumericValue(Number i) {
        this(OBJECT_VALUE_DEFAULT, i.doubleValue(), new HealthPlaceholder());
    }

    public NumericValue(NumericPlaceholder placeholder) {
        this(false, 10.0, placeholder);
    }

    public static NumericValue deserialize(Map<String, Object> map) {
        boolean objectValue = (boolean) map.getOrDefault("objectValue", OBJECT_VALUE_DEFAULT);
        objectValue = (boolean) map.getOrDefault("doubleValue", objectValue);
        double value = 10.0;
        NumericPlaceholder placeholderValue = new HealthPlaceholder();
        if (objectValue) {
            value = (double) map.getOrDefault("value", 10.0);
        } else {
            placeholderValue = (NumericPlaceholder) map.get("placeholderValue");
        }
        return new NumericValue(objectValue, value, placeholderValue);
    }

    @Override
    public NumericValue clone() {
        return new NumericValue(objectValue, value, placeholderValue.clone());
    }

    @SneakyThrows
    public PluginInventoryHolder getCustomClickConsumer(JavaPlugin plugin, PluginInventoryHolder parent,
                                                        ModelPath<? extends Model> path, Field field,
                                                        InventoryClickEvent e) {
        Field valueField = Value.class.getDeclaredField("value");
        Map<ClickType, Supplier<PluginInventoryHolder>> map = new HashMap<>();
        map.put(ClickType.LEFT, () -> {
            if (objectValue) {
                path.addModel(this);
                if (field.isAnnotationPresent(Serializable.Number.class)) {
                    return new DoubleFieldChangeInventoryHolder(plugin, parent, path,
                            new FieldChangeInformation(valueField),
                            field.getAnnotation(Serializable.Number.class));
                } else {
                    return new DoubleFieldChangeInventoryHolder(plugin, parent, path,
                            new FieldChangeInformation(valueField));
                }
            } else {
                path.addModel(placeholderValue);
                return new ModelObjectInventoryHolder(plugin, parent, path);
            }
        });
        return super.getCustomClickConsumer(plugin, parent, path, field, e, map);
    }
}
