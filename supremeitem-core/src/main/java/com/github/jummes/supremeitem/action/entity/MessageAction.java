package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.StringValue;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

@Getter
@Setter
@Enumerable.Displayable(name = "&c&lMessage", description = "gui.action.entity.message.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmJmM2ZjZGNjZmZkOTYzZTQzMzQ4MTgxMDhlMWU5YWUzYTgwNTY2ZDBkM2QyZDRhYjMwNTFhMmNkODExMzQ4YyJ9fX0=")
@Enumerable.Child
public class MessageAction extends EntityAction {

    private static final StringValue DEFAULT_MESSAGE = new StringValue("message");

    private static final String MESSAGE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2RkNjM5NzhlODRlMjA5MjI4M2U5Y2QwNmU5ZWY0YmMyMjhiYjlmMjIyMmUxN2VlMzgzYjFjOWQ5N2E4YTAifX19";

    @Serializable(headTexture = MESSAGE_HEAD, description = "gui.action.entity.message.message", additionalDescription = {"gui.additional-tooltips.value"})
    private StringValue message;

    public MessageAction() {
        this(TARGET_DEFAULT, DEFAULT_MESSAGE.clone());
    }

    public MessageAction(boolean target, StringValue message) {
        super(target);
        this.message = message;
    }

    public MessageAction(Map<String, Object> map) {
        super(map);
        this.message = (StringValue) map.getOrDefault("message", DEFAULT_MESSAGE.clone());
    }

    @Override
    public ActionResult execute(Target target, Source source, Map<String, Object> map) {
        LivingEntity e = getEntity(target, source);

        if (e == null) {
            return ActionResult.FAILURE;
        }

        e.sendMessage(MessageUtils.color(SupremeItem.getInstance().
                getSavedPlaceholderManager().computePlaceholders(message.getRealValue(target, source), source, target)));
        return ActionResult.SUCCESS;
    }

    @Override
    public Action clone() {
        return new MessageAction(target, message.clone());
    }

    @Override
    public String getName() {
        return "&6&lMessage: &c" + message.getName();
    }

}
