package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.LocationSource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.ItemTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.google.common.collect.Lists;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lChange Source", description = "gui.action.meta.wrapper.source.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM2Mzc5NjFmODQ1MWE1M2I2N2QyNTMxMmQzNTBjNjIwZjMyYjVmNjA4YmQ2YWRlMDY2MzdiZTE3MTJmMzY0ZSJ9fX0")
public class ChangeSourceAction extends WrapperAction {
    private static final String ACTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";

    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.action.meta.wrapper.source.actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> actions;

    public ChangeSourceAction() {
        this(TARGET_DEFAULT, Lists.newArrayList());
    }

    public ChangeSourceAction(boolean target, List<Action> actions) {
        super(target);
        this.actions = actions;
    }

    public ChangeSourceAction(Map<String, Object> map) {
        super(map);
        this.actions = (List<Action>) map.getOrDefault("actions", Lists.newArrayList());
    }

    @Override
    public List<Action> getWrappedActions() {
        return actions;
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        Source newSource = null;
        if (target instanceof LocationTarget) {
            newSource = new LocationSource(target.getLocation().clone(), source.getCaster());
        } else if (target instanceof EntityTarget) {
            newSource = new EntitySource(((EntityTarget) target).getTarget());
        } else if (target instanceof ItemTarget) {
            newSource = new EntitySource(((ItemTarget) target).getOwner());
        }
        Source finalNewSource = newSource;
        if (actions.stream().filter(action -> action.execute(target, finalNewSource).equals(ActionResult.CANCELLED)).
                count() > 0) {
            return ActionResult.CANCELLED;
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public String getName() {
        return "&6&lChange source";
    }

    @Override
    public ItemStack targetItem() {
        return null;
    }

    @Override
    public Action clone() {
        return new ChangeSourceAction(TARGET_DEFAULT, actions.stream().map(Action::clone).collect(Collectors.toList()));
    }
}
