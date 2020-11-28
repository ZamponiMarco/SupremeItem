package com.github.jummes.supremeitem.value;

import com.github.jummes.libs.annotation.CustomClickable;
import com.github.jummes.libs.annotation.GUINameable;
import com.github.jummes.libs.gui.PluginInventoryHolder;
import com.github.jummes.libs.gui.model.ModelObjectInventoryHolder;
import com.github.jummes.libs.gui.setting.FromListFieldChangeInventoryHolder;
import com.github.jummes.libs.gui.setting.change.FieldChangeInformation;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.MapperUtils;
import com.github.jummes.supremeitem.placeholder.material.BlockMaterialPlaceholder;
import com.github.jummes.supremeitem.placeholder.material.MaterialPlaceholder;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

@GUINameable(GUIName = "getName")
@Getter
@CustomClickable(customFieldClickConsumer = "getCustomClickConsumer")
public class MaterialValue extends Value<Material, MaterialPlaceholder> {
    public MaterialValue() {
        this(OBJECT_VALUE_DEFAULT, Material.STONE, new BlockMaterialPlaceholder());
    }

    public MaterialValue(boolean objectValue, Material value, MaterialPlaceholder placeholderValue) {
        super(objectValue, value, placeholderValue);
    }

    public MaterialValue(Material i) {
        this(OBJECT_VALUE_DEFAULT, i, new BlockMaterialPlaceholder());
    }

    public MaterialValue(MaterialPlaceholder placeholder) {
        this(false, Material.STONE, placeholder);
    }

    public static MaterialValue deserialize(Map<String, Object> map) {
        boolean objectValue = (boolean) map.getOrDefault("objectValue", OBJECT_VALUE_DEFAULT);
        Material value = Material.STONE;
        MaterialPlaceholder placeholderValue = new BlockMaterialPlaceholder();
        if (objectValue) {
            value = Material.valueOf((String) map.get("value"));
        } else {
            placeholderValue = (MaterialPlaceholder) map.get("placeholderValue");
        }
        return new MaterialValue(objectValue, value, placeholderValue);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("==", this.getClass().getName());
        if (objectValue != OBJECT_VALUE_DEFAULT) {
            map.put("objectValue", objectValue);
        }
        if (objectValue) {
            map.put("value", value.name());
        } else {
            map.put("placeholderValue", placeholderValue);
        }
        return map;
    }

    @Override
    public MaterialValue clone() {
        return new MaterialValue(objectValue, value, placeholderValue.clone());
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
                return new FromListFieldChangeInventoryHolder(plugin, parent, path, new FieldChangeInformation(valueField),
                        1, Lists.newArrayList(Material.values()), MapperUtils.getMaterialMapper());
            } else {
                path.addModel(placeholderValue);
                return new ModelObjectInventoryHolder(plugin, parent, path);
            }
        });
        return super.getCustomClickConsumer(plugin, parent, path, field, e, map);
    }
}
