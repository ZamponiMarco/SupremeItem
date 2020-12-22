package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.StringValue;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Enumerable.Displayable(name = "&c&lCommand", description = "gui.action.meta.command.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWY0YzIxZDE3YWQ2MzYzODdlYTNjNzM2YmZmNmFkZTg5NzMxN2UxMzc0Y2Q1ZDliMWMxNWU2ZTg5NTM0MzIifX19")
@Enumerable.Child
@Getter
@Setter
public class CommandAction extends MetaAction {

    private static final StringValue COMMAND_DEFAULT = new StringValue("say example");

    private static final String COMMAND_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWY0YzIxZDE3YWQ2MzYzODdlYTNjNzM2YmZmNmFkZTg5NzMxN2UxMzc0Y2Q1ZDliMWMxNWU2ZTg5NTM0MzIifX19";
    @Serializable(headTexture = COMMAND_HEAD, description = "gui.action.meta.command.description", additionalDescription = {"gui.additional-tooltips.value"})
    private StringValue command;

    public CommandAction() {
        this(TARGET_DEFAULT, COMMAND_DEFAULT.clone());
    }

    public CommandAction(boolean target, StringValue command) {
        super(target);
        this.command = command;
    }

    public CommandAction(Map<String, Object> map) {
        super(map);
        this.command = (StringValue) map.getOrDefault("command", COMMAND_DEFAULT.clone());
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), SupremeItem.getInstance().
                getSavedPlaceholderManager().computePlaceholders(command.getRealValue(target, source), source, target));
        return ActionResult.SUCCESS;
    }

    @Override
    public Action clone() {
        return new CommandAction(TARGET_DEFAULT, command);
    }

    @Override
    public ItemStack targetItem() {
        return null;
    }

    @Override
    public String getName() {
        return "&6&lCommand: &c" + command.getName();
    }
}
