package com.github.jummes.supremeitem.placeholder.numeric;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.manager.VariableManager;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lVariable Placeholder", description = "gui.placeholder.double.variable.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjVjNGQyNGFmZmRkNDgxMDI2MjAzNjE1MjdkMjE1NmUxOGMyMjNiYWU1MTg5YWM0Mzk4MTU2NDNmM2NmZjlkIn19fQ==")
public class NumericVariablePlaceholder extends NumericPlaceholder {

    private static final String NAME_DEFAULT = "var";

    private static final String NAME_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0=";

    @Serializable(headTexture = NAME_HEAD, description = "gui.placeholder.double.variable.name")
    @Serializable.Optional(defaultValue = "NAME_DEFAULT")
    private String name;

    public NumericVariablePlaceholder() {
        this(TARGET_DEFAULT, NAME_DEFAULT);
    }

    public NumericVariablePlaceholder(boolean target, String name) {
        super(target);
        this.name = name;
    }

    public static NumericVariablePlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        String name = (String) map.getOrDefault("name", NAME_DEFAULT);
        return new NumericVariablePlaceholder(target, name);
    }

    @Override
    public NumericPlaceholder clone() {
        return new NumericVariablePlaceholder(target, name);
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        VariableManager variableManager = SupremeItem.getInstance().getVariableManager();
        if (this.target && (target instanceof EntityTarget)) {
            return variableManager.getNumericVariable(((EntityTarget) target).getTarget(), name);
        } else if (!this.target) {
            return variableManager.getNumericVariable(source.getCaster(), name);
        }
        return 0.0;
    }

    @Override
    public String getName() {
        return String.format("%s.%s", target ? "Target" : "Source", name);
    }
}
