package com.github.jummes.supremeitem.action.variable;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.ItemTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.NumericValue;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lSet Numeric Variable", description = "gui.action.variable.numeric-variable.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==")
public class SetNumericVariableAction extends VariableAction {

    private static final NumericValue VALUE_DEFAULT = new NumericValue(10);

    private static final String VALUE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";


    @Serializable(headTexture = VALUE_HEAD, description = "gui.action.variable.value", additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Optional(defaultValue = "VALUE_DEFAULT")
    private NumericValue value;

    public SetNumericVariableAction() {
        this(TARGET_DEFAULT, NAME_DEFAULT, PERSISTENT_DEFAULT, VALUE_DEFAULT.clone());
    }

    public SetNumericVariableAction(boolean target, String name, boolean persistent, NumericValue value) {
        super(target, name, persistent);
        this.value = value;
    }

    public SetNumericVariableAction(Map<String, Object> map) {
        super(map);
        this.value = (NumericValue) map.getOrDefault("value", VALUE_DEFAULT.clone());
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
                        PersistentDataType.DOUBLE, value.getRealValue(target, source));
                item.setItemMeta(meta);
            } else {
                PersistentDataContainer container = getDataContainer(target, source);

                if (container == null) {
                    return ActionResult.FAILURE;
                }
                container.set(new NamespacedKey(SupremeItem.getInstance(), name), PersistentDataType.DOUBLE,
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
        return new SetNumericVariableAction(target, name, persistent, value.clone());
    }

    @Override
    public String getName() {
        return String.format("&6&lSet Variable: &c%s.%s &6&l» &c%s", target ? "Target" : "Source", name, value.getName());
    }
}
