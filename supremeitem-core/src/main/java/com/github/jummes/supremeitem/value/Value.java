package com.github.jummes.supremeitem.value;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.gui.FieldInventoryHolderFactory;
import com.github.jummes.libs.gui.PluginInventoryHolder;
import com.github.jummes.libs.gui.model.ModelObjectInventoryHolder;
import com.github.jummes.libs.gui.model.create.ModelCreateInventoryHolderFactory;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.Placeholder;
import com.google.common.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

@AllArgsConstructor
@Getter
public abstract class Value<S, T extends Placeholder<S>> implements Model, Cloneable {

    protected static final boolean OBJECT_VALUE_DEFAULT = true;

    @Serializable
    @Serializable.Optional(defaultValue = "DOUBLE_VALUE_DEFAULT")
    protected boolean objectValue;
    @Serializable
    protected S value;
    @Serializable
    protected T placeholderValue;

    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("==", this.getClass().getName());
        if (objectValue != OBJECT_VALUE_DEFAULT) {
            map.put("objectValue", objectValue);
        }
        if (objectValue) {
            map.put("value", value);
        } else {
            map.put("placeholderValue", placeholderValue);
        }
        return map;
    }

    public S getRealValue(Target target, Source source) {
        return objectValue ? value : placeholderValue.computePlaceholder(target, source);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Value<S, T> that = (Value<S, T>) o;

        if (objectValue != that.objectValue) return false;
        if (objectValue) {
            return that.value.equals(value);
        } else
            return placeholderValue != null ? placeholderValue.equals(that.placeholderValue) : that.placeholderValue == null;

    }

    @Override
    public int hashCode() {
        int result;
        result = (objectValue ? 1 : 0);
        if (objectValue) {
            result = 31 * result + value.hashCode();
        } else {
            result = 31 * result + (placeholderValue != null ? placeholderValue.hashCode() : 0);
        }
        return result;
    }

    @SneakyThrows
    public PluginInventoryHolder getCustomClickConsumer(JavaPlugin plugin, PluginInventoryHolder parent,
                                                        ModelPath<? extends Model> path, Field field, InventoryClickEvent e,
                                                        Map<ClickType, Supplier<PluginInventoryHolder>> clickMap) {
        Class<? extends Value> clazz = getClass();
        Class<Placeholder> placeholderClass = (Class<Placeholder>) TypeToken.of(clazz).resolveType(Value.class.getTypeParameters()[1]).getRawType();
        Field placeholderValueField = Value.class.getDeclaredField("placeholderValue");
        clickMap.putIfAbsent(ClickType.RIGHT, () -> {
            if (!objectValue) {
                path.addModel(this);
                return ModelCreateInventoryHolderFactory.create(plugin, parent, path,
                        placeholderValueField, placeholderClass);
            }
            return parent;
        });
        clickMap.putIfAbsent(ClickType.MIDDLE, () -> {
            objectValue = !objectValue;
            path.saveModel();
            return parent;
        });
        return clickMap.getOrDefault(e.getClick(), () -> parent).get();
    }

    public String getName() {
        return objectValue ? String.valueOf(value) : placeholderValue.getName();
    }

}