package com.github.jummes.supremeitem.action.variable;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.NumericValue;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lSet Numeric Variable", description = "gui.action.numeric-variable.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==")
public class SetNumericVariableAction extends VariableAction {

    private static final String NAME_DEFAULT = "var";
    private static final NumericValue VALUE_DEFAULT = new NumericValue(10);

    private static final String NAME_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0=";
    private static final String VALUE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(headTexture = NAME_HEAD, description = "gui.action.numeric-variable.name")
    @Serializable.Optional(defaultValue = "NAME_DEFAULT")
    private String name;
    @Serializable(headTexture = VALUE_HEAD, description = "gui.action.numeric-variable.value", additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Optional(defaultValue = "VALUE_DEFAULT")
    private NumericValue value;

    public SetNumericVariableAction() {
        this(TARGET_DEFAULT, NAME_DEFAULT, VALUE_DEFAULT.clone());
    }

    public SetNumericVariableAction(boolean target, String name, NumericValue value) {
        super(target);
        this.name = name;
        this.value = value;
    }

    public static SetNumericVariableAction deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        String name = (String) map.getOrDefault("name", "var");
        NumericValue value = (NumericValue) map.getOrDefault("value", VALUE_DEFAULT.clone());
        return new SetNumericVariableAction(target, name, value);
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        LivingEntity e = getEntity(target, source);

        if (e == null) {
            return ActionResult.FAILURE;
        }

        e.setMetadata(name, new FixedMetadataValue(SupremeItem.getInstance(), value.getRealValue(target, source)));
        return ActionResult.SUCCESS;
    }

    @Override
    public Action clone() {
        return new SetNumericVariableAction(target, name, value.clone());
    }

    @Override
    public String getName() {
        return String.format("&6&lSet Variable: &c%s.%s &6&l» &c%s", target ? "Target" : "Source", name, value.getName());
    }
}
