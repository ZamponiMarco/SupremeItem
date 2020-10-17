package com.github.jummes.supremeitem.placeholder.numeric;

import com.github.jummes.libs.annotation.CustomClickable;
import com.github.jummes.libs.annotation.GUINameable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.gui.PluginInventoryHolder;
import com.github.jummes.libs.gui.model.ModelObjectInventoryHolder;
import com.github.jummes.libs.gui.model.create.ModelCreateInventoryHolderFactory;
import com.github.jummes.libs.gui.setting.DoubleFieldChangeInventoryHolder;
import com.github.jummes.libs.gui.setting.change.FieldChangeInformation;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

@GUINameable(GUIName = "getName")
@AllArgsConstructor
@Getter
@CustomClickable(customFieldClickConsumer = "getCustomClickConsumer")
public class NumericValue implements Model, Cloneable {

    private static final boolean DOUBLE_VALUE_DEFAULT = true;

    @Serializable
    @Serializable.Optional(defaultValue = "DOUBLE_VALUE_DEFAULT")
    private boolean doubleValue;
    @Serializable
    private double value;
    @Serializable
    private NumericPlaceholder placeholderValue;

    public NumericValue() {
        this(DOUBLE_VALUE_DEFAULT, 10.0, new HealthPlaceholder());
    }

    public NumericValue(double i) {
        this(true, i, new HealthPlaceholder());
    }

    public NumericValue(NumericPlaceholder numericPlaceholder) {
        this(false, 10.0, numericPlaceholder);
    }

    public static NumericValue deserialize(Map<String, Object> map) {
        boolean doubleValue = (boolean) map.getOrDefault("doubleValue", DOUBLE_VALUE_DEFAULT);
        double value = 10.0;
        NumericPlaceholder placeholderValue = new HealthPlaceholder();
        if (doubleValue) {
            value = (double) map.get("value");
        } else {
            placeholderValue = (NumericPlaceholder) map.get("placeholderValue");
        }
        return new NumericValue(doubleValue, value, placeholderValue);
    }

    @Override
    public NumericValue clone() {
        return new NumericValue(doubleValue, value, placeholderValue.clone());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NumericValue that = (NumericValue) o;

        if (doubleValue != that.doubleValue) return false;
        if (doubleValue) {
            return Double.compare(that.value, value) == 0;
        } else
            return placeholderValue != null ? placeholderValue.equals(that.placeholderValue) : that.placeholderValue == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (doubleValue ? 1 : 0);
        if (doubleValue) {
            result = 31 * result + (int) (Double.doubleToLongBits(value) ^ (Double.doubleToLongBits(value) >>> 32));
        } else {
            result = 31 * result + (placeholderValue != null ? placeholderValue.hashCode() : 0);
        }
        return result;
    }

    public double getRealValue(Target target, Source source) {
        return doubleValue ? value : placeholderValue.computePlaceholder(target, source);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap();
        map.put("==", this.getClass().getName());
        if (doubleValue != DOUBLE_VALUE_DEFAULT) {
            map.put("doubleValue", doubleValue);
        }
        if (doubleValue) {
            map.put("value", value);
        } else {
            map.put("placeholderValue", placeholderValue);
        }
        return map;
    }

    public String getName() {
        return doubleValue ? String.valueOf(value) : placeholderValue.getName();
    }

    public PluginInventoryHolder getCustomClickConsumer(JavaPlugin plugin, PluginInventoryHolder parent,
                                                        ModelPath<? extends Model> path, Field field,
                                                        InventoryClickEvent e) {
        try {
            if (e.getClick().equals(ClickType.LEFT)) {
                if (doubleValue) {
                    path.addModel(this);
                    if (field.isAnnotationPresent(Serializable.Number.class)) {
                        return new DoubleFieldChangeInventoryHolder(plugin, parent, path,
                                new FieldChangeInformation(getClass().getDeclaredField("value")),
                                field.getAnnotation(Serializable.Number.class));
                    } else {
                        return new DoubleFieldChangeInventoryHolder(plugin, parent, path,
                                new FieldChangeInformation(getClass().getDeclaredField("value")));
                    }
                } else {
                    path.addModel(placeholderValue);
                    return new ModelObjectInventoryHolder(plugin, parent, path);
                }
            } else if (e.getClick().equals(ClickType.RIGHT)) {
                if (!doubleValue) {
                    path.addModel(this);
                    return ModelCreateInventoryHolderFactory.create(plugin, parent, path,
                            getClass().getDeclaredField("placeholderValue"));
                }
            } else if (e.getClick().equals(ClickType.MIDDLE)) {
                doubleValue = !doubleValue;
                path.saveModel();
                return parent;
            }
        } catch (Exception ignored) {
        }
        return null;
    }

}
