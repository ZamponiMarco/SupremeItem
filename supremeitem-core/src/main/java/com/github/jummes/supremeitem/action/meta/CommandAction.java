package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Map;

@Enumerable.Child
@AllArgsConstructor
public class CommandAction extends MetaAction {

    private static final String ACTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";
    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.action.area-entities.actions")
    private String command;

    public CommandAction() {
        this("say example");
    }

    public static CommandAction deserialize(Map<String, Object> map) {
        String command = (String) map.get("command");
        return new CommandAction(command);
    }

    @Override
    protected ActionResult execute(Target target, Source source) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        return ActionResult.SUCCESS;
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(LocationTarget.class, EntityTarget.class);
    }
}
