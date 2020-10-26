package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.StringValue;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@Enumerable.Displayable(name = "&c&lCommand", description = "gui.action.command.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWY0YzIxZDE3YWQ2MzYzODdlYTNjNzM2YmZmNmFkZTg5NzMxN2UxMzc0Y2Q1ZDliMWMxNWU2ZTg5NTM0MzIifX19")
@Enumerable.Child
@Getter
@Setter
@AllArgsConstructor
public class CommandAction extends MetaAction {

    private static final StringValue COMMAND_DEFAULT = new StringValue("say example");

    private static final String COMMAND_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWY0YzIxZDE3YWQ2MzYzODdlYTNjNzM2YmZmNmFkZTg5NzMxN2UxMzc0Y2Q1ZDliMWMxNWU2ZTg5NTM0MzIifX19";
    @Serializable(headTexture = COMMAND_HEAD, description = "gui.action.command.description", additionalDescription = {"gui.additional-tooltips.value"})
    private StringValue command;

    public CommandAction() {
        this(COMMAND_DEFAULT.clone());
    }

    public static CommandAction deserialize(Map<String, Object> map) {
        StringValue command;
        try {
            command = (StringValue) map.getOrDefault("command", COMMAND_DEFAULT.clone());
        } catch (ClassCastException e) {
            command = new StringValue((String) map.getOrDefault("command", COMMAND_DEFAULT.getValue()));
        }
        return new CommandAction(command);
    }

    @Override
    protected ActionResult execute(Target target, Source source) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), SupremeItem.getInstance().
                getSavedPlaceholderManager().computePlaceholders(command.getRealValue(target, source), source, target));
        return ActionResult.SUCCESS;
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(LocationTarget.class, EntityTarget.class);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWY0YzIxZDE3YWQ2MzYzODdlYTNjNzM2YmZmNmFkZTg5NzMxN2UxMzc0Y2Q1ZDliMWMxNWU2ZTg5NTM0MzIifX19"),
                "&6&lCommand", Libs.getLocale().getList("gui.action.description"));
    }

    @Override
    public Action clone() {
        return new CommandAction(command);
    }
}
