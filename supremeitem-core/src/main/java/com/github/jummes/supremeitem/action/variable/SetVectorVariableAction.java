package com.github.jummes.supremeitem.action.variable;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.VectorValue;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.Metadatable;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lSet Vector Variable", description = "gui.action.variable.vector-variable.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWFjYWNiODM2YzVlNDI4YjQ5YjVkMjI0Y2FiMjI4MjhlZmUyZjZjNzA0Zjc1OTM2NGQ3MWM2NTZlMzAxNDIwIn19fQ")
public class SetVectorVariableAction extends VariableAction {

    private static final VectorValue VALUE_DEFAULT = new VectorValue();

    @Serializable(headTexture = VALUE_HEAD, description = "gui.action.variable.value", additionalDescription = {"gui.additional-tooltips.value"})
    private VectorValue value;

    public SetVectorVariableAction() {
        this(TARGET_DEFAULT, NAME_DEFAULT, PERSISTENT_DEFAULT, VALUE_DEFAULT.clone());
    }

    public SetVectorVariableAction(boolean target, String name, boolean persistent, VectorValue value) {
        super(target, name, persistent);
        this.value = value;
    }

    public SetVectorVariableAction(Map<String, Object> map) {
        super(map);
        this.value = (VectorValue) map.getOrDefault("value", VALUE_DEFAULT.clone());
    }

    @Override
    public ActionResult execute(Target target, Source source, Map<String, Object> map) {
        Metadatable metadatable = getMetadatable(target, source);

        if (metadatable == null) {
            return ActionResult.FAILURE;
        }

        metadatable.setMetadata(name, new FixedMetadataValue(SupremeItem.getInstance(), value.getRealValue(target, source)));
        return ActionResult.SUCCESS;
    }

    @Override
    public String getName() {
        return String.format("&6&lSet Variable: &c%s.%s &6&l» &c%s", target ? "Target" : "Source", name, value.getName());
    }

    @Override
    public Action clone() {
        return new SetVectorVariableAction(target, name, PERSISTENT_DEFAULT, value.clone());
    }

    @Override
    public ItemStack getPersistentItem() {
        return null;
    }
}
