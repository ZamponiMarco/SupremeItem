package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.StringValue;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Map;

@Getter
@Setter
@Enumerable.Displayable(condition = "paperEnabled", name = "&c&lAction bar message", description = "gui.action.entity.action-bar.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjAyYWYzY2EyZDVhMTYwY2ExMTE0MDQ4Yjc5NDc1OTQyNjlhZmUyYjFiNWVjMjU1ZWU3MmI2ODNiNjBiOTliOSJ9fX0=")
@Enumerable.Child
public class ActionBarMessageAction extends EntityAction {
    private static final StringValue DEFAULT_MESSAGE = new StringValue("message");

    private static final String MESSAGE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2RkNjM5NzhlODRlMjA5MjI4M2U5Y2QwNmU5ZWY0YmMyMjhiYjlmMjIyMmUxN2VlMzgzYjFjOWQ5N2E4YTAifX19";

    @Serializable(headTexture = MESSAGE_HEAD, description = "gui.action.entity.action-bar.message",
            additionalDescription = {"gui.additional-tooltips.value"})
    private StringValue message;

    public ActionBarMessageAction() {
        this(TARGET_DEFAULT, DEFAULT_MESSAGE.clone());
    }

    public ActionBarMessageAction(boolean target, StringValue message) {
        super(target);
        this.message = message;
    }

    public ActionBarMessageAction(Map<String, Object> map) {
        super(map);
        this.message = (StringValue) map.getOrDefault("message", DEFAULT_MESSAGE.clone());
    }

    public static boolean paperEnabled(ModelPath path) {
        return SupremeItem.getInstance().getPaperHook().isSendActionBarEnabled();
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        LivingEntity e = getEntity(target, source);

        if (!(e instanceof Player) || !SupremeItem.getInstance().getPaperHook().isSendActionBarEnabled()) {
            return ActionResult.FAILURE;
        }

        ((Player) e).sendActionBar(MessageUtils.color(SupremeItem.getInstance().
                getSavedPlaceholderManager().computePlaceholders(message.getRealValue(target, source), source, target)));
        return ActionResult.SUCCESS;
    }

    @Override
    public Action clone() {
        return new ActionBarMessageAction(target, message.clone());
    }

    @Override
    public String getName() {
        return "&6&lAction bar message: &c" + message.getName();
    }

}
