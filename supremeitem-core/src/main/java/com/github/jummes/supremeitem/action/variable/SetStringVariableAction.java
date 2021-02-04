package com.github.jummes.supremeitem.action.variable;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.ItemTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.StringValue;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lSet String Variable", description = "gui.action.variable.string-variable.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2RkNjM5NzhlODRlMjA5MjI4M2U5Y2QwNmU5ZWY0YmMyMjhiYjlmMjIyMmUxN2VlMzgzYjFjOWQ5N2E4YTAifX19")
public class SetStringVariableAction extends VariableAction {

    protected static final StringValue VALUE_DEFAULT = new StringValue("example");

    @Serializable(headTexture = VALUE_HEAD, description = "gui.action.variable.value", additionalDescription = {"gui.additional-tooltips.value"})
    private StringValue value;

    public SetStringVariableAction() {
        this(TARGET_DEFAULT, NAME_DEFAULT, PERSISTENT_DEFAULT, VALUE_DEFAULT.clone());
    }

    public SetStringVariableAction(boolean target, String name, boolean persistent, StringValue value) {
        super(target, name, persistent);
        this.value = value;
    }

    public SetStringVariableAction(Map<String, Object> map) {
        super(map);
        this.value = (StringValue) map.getOrDefault("value", VALUE_DEFAULT.clone());
    }

    @Override
    public ActionResult execute(Target target, Source source, Map<String, Object> map) {
        if (persistent) {
            if (target instanceof ItemTarget) {
                ItemStack item = ((ItemTarget) target).getTarget();
                ItemMeta meta = item.getItemMeta();
                if (meta == null) {
                    return ActionResult.FAILURE;
                }
                meta.getPersistentDataContainer().set(new NamespacedKey(SupremeItem.getInstance(), name),
                        PersistentDataType.STRING, value.getRealValue(target, source));
                item.setItemMeta(meta);
            } else {
                PersistentDataContainer container = getDataContainer(target, source);

                if (container == null) {
                    return ActionResult.FAILURE;
                }
                container.set(new NamespacedKey(SupremeItem.getInstance(), name), PersistentDataType.STRING,
                        value.getRealValue(target, source));
            }

            return ActionResult.SUCCESS;
        }
        Metadatable metadatable = getMetadatable(target, source);

        if (metadatable == null) {
            return ActionResult.FAILURE;
        }

        metadatable.setMetadata(name, new FixedMetadataValue(SupremeItem.getInstance(), value.getRealValue(target, source)));
        return ActionResult.SUCCESS;
    }

    @Override
    public Action clone() {
        return new SetStringVariableAction(target, name, persistent, value.clone());
    }

    @Override
    public String getName() {
        return String.format("&6&lSet Variable: &c%s.%s &6&l» &c%s", target ? "Target" : "Source", name, value.getName());
    }
}
