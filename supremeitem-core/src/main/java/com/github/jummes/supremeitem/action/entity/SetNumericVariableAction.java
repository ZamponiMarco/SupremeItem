package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.manager.VariableManager;
import com.github.jummes.supremeitem.placeholder.numeric.NumericValue;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@Enumerable.Child
@AllArgsConstructor
@Enumerable.Displayable(name = "&c&lSet Numeric Variable", description = "gui.action.numeric-variable.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjVjNGQyNGFmZmRkNDgxMDI2MjAzNjE1MjdkMjE1NmUxOGMyMjNiYWU1MTg5YWM0Mzk4MTU2NDNmM2NmZjlkIn19fQ==")
public class SetNumericVariableAction extends EntityAction {

    private static final boolean TARGET_DEFAULT = true;
    private static final String NAME_DEFAULT = "var";
    private static final NumericValue VALUE_DEFAULT = new NumericValue(10);

    private static final String TARGET_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc4N2I3YWZiNWE1OTk1Mzk3NWJiYTI0NzM3NDliNjAxZDU0ZDZmOTNjZWFjN2EwMmFjNjlhYWU3ZjliOCJ9fX0==";
    private static final String NAME_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0=";
    private static final String VALUE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(headTexture = TARGET_HEAD, description = "gui.action.numeric-variable.target")
    @Serializable.Optional(defaultValue = "TARGET_DEFAULT")
    private boolean target;
    @Serializable(headTexture = NAME_HEAD, description = "gui.action.numeric-variable.name")
    @Serializable.Optional(defaultValue = "NAME_DEFAULT")
    private String name;
    @Serializable(headTexture = VALUE_HEAD, description = "gui.action.numeric-variable.value")
    @Serializable.Optional(defaultValue = "VALUE_DEFAULT")
    private NumericValue value;

    public SetNumericVariableAction() {
        this(TARGET_DEFAULT, NAME_DEFAULT, VALUE_DEFAULT.clone());
    }

    public static SetNumericVariableAction deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        String name = (String) map.getOrDefault("name", "var");
        NumericValue value = (NumericValue) map.getOrDefault("value", VALUE_DEFAULT.clone());
        return new SetNumericVariableAction(target, name, value);
    }

    @Override
    protected ActionResult execute(Target target, Source source) {
        VariableManager variableManager = SupremeItem.getInstance().getVariableManager();
        if (this.target) {
            if (target instanceof EntityTarget) {
                variableManager.setNumericVariable(((EntityTarget) target).getTarget(), name, value.getRealValue(target, source));
                return ActionResult.SUCCESS;
            }
        }
        if (source instanceof EntitySource) {
            variableManager.setNumericVariable(source.getCaster(), name, value.getRealValue(target, source));
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAILURE;
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(EntityTarget.class);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjVjNGQyNGFmZmRkNDgxMDI2MjAzNjE1MjdkMjE1NmUxOGMyMjNiYWU1MTg5YWM0Mzk4MTU2NDNmM2NmZjlkIn19fQ"),
                String.format("&6&lSet Variable: &c%s.%s &6&l» &c%s", target ? "Target" : "Source", name, value.getName()), Libs.getLocale().getList("gui.action.description"));
    }

    @Override
    public Action clone() {
        return new SetNumericVariableAction(target, name, value.clone());
    }
}
