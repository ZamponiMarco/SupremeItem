package com.github.jummes.supremeitem.value;

import com.github.jummes.libs.annotation.CustomClickable;
import com.github.jummes.libs.annotation.GUINameable;
import com.github.jummes.libs.gui.PluginInventoryHolder;
import com.github.jummes.libs.gui.model.ModelObjectInventoryHolder;
import com.github.jummes.libs.gui.setting.StringFieldChangeInventoryHolder;
import com.github.jummes.libs.gui.setting.change.FieldChangeInformation;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.supremeitem.placeholder.string.StringPlaceholder;
import com.github.jummes.supremeitem.placeholder.string.WorldNamePlaceholder;
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
public class StringValue extends Value<String, StringPlaceholder> {
    public StringValue() {
        this(OBJECT_VALUE_DEFAULT, "example", new WorldNamePlaceholder());
    }

    public StringValue(boolean objectValue, String value, StringPlaceholder placeholderValue) {
        super(objectValue, value, placeholderValue);
    }

    public StringValue(String i) {
        this(OBJECT_VALUE_DEFAULT, i, new WorldNamePlaceholder());
    }

    public StringValue(StringPlaceholder placeholder) {
        this(false, "example", placeholder);
    }

    public StringValue(Map<String, Object> map) {
        super(map);
        this.value = "example";
        this.placeholderValue = new WorldNamePlaceholder();
        if (this.objectValue) {
            this.value = MessageUtils.getColoredString((String) map.get("value"));
        } else {
            this.placeholderValue = (StringPlaceholder) map.get("placeholderValue");
        }
    }

    @Override
    public StringValue clone() {
        return new StringValue(objectValue, value, placeholderValue.clone());
    }

    @SneakyThrows
    public PluginInventoryHolder getCustomClickConsumer(JavaPlugin plugin, PluginInventoryHolder parent,
                                                        ModelPath<? extends Model> path, Field field,
                                                        InventoryClickEvent e) {
        Map<ClickType, Supplier<PluginInventoryHolder>> map = new HashMap<>();
        Field valueField = Value.class.getDeclaredField("value");
        map.putIfAbsent(ClickType.LEFT, () -> {
            if (objectValue) {
                path.addModel(this);
                return new StringFieldChangeInventoryHolder(plugin, parent, path,
                        new FieldChangeInformation(valueField));
            } else {
                path.addModel(placeholderValue);
                return new ModelObjectInventoryHolder(plugin, parent, path);
            }
        });
        return super.getCustomClickConsumer(plugin, parent, path, field, e, map);
    }
}
