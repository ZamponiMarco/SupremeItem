package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@Enumerable.Displayable(name = "&c&lMessage", description = "gui.action.message.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmJmM2ZjZGNjZmZkOTYzZTQzMzQ4MTgxMDhlMWU5YWUzYTgwNTY2ZDBkM2QyZDRhYjMwNTFhMmNkODExMzQ4YyJ9fX0=")
@Enumerable.Child
public class MessageAction extends Action {

    private static final String MESSAGE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2RkNjM5NzhlODRlMjA5MjI4M2U5Y2QwNmU5ZWY0YmMyMjhiYjlmMjIyMmUxN2VlMzgzYjFjOWQ5N2E4YTAifX19";

    @Serializable(headTexture = MESSAGE_HEAD, description = "gui.action.message.message")
    private String message;

    public MessageAction() {
        this("");
    }

    public static MessageAction deserialize(Map<String, Object> map) {
        String message = (String) map.getOrDefault("message", "");
        return new MessageAction(message);
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        ((EntityTarget) target).getTarget().sendMessage(MessageUtils.color(message));
        return ActionResult.SUCCESS;
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(EntityTarget.class);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmJmM2ZjZGNjZmZkOTYzZTQzMzQ4MTgxMDhlMWU5YWUzYTgwNTY2ZDBkM2QyZDRhYjMwNTFhMmNkODExMzQ4YyJ9fX0="),
                "&6&lMessage: &c" + message, Libs.getLocale().getList("gui.action.description"));
    }

}
