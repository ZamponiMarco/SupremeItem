package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.NumericValue;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;
import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lBlock Entity Event", description = "gui.action.block-event.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkMWFiYTczZjYzOWY0YmM0MmJkNDgxOTZjNzE1MTk3YmUyNzEyYzNiOTYyYzk3ZWJmOWU5ZWQ4ZWZhMDI1In19fQ==")
public class BlockEventAction extends EntityAction {

    private static final String BLOCK_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkMWFiYTczZjYzOWY0YmM0MmJkNDgxOTZjNzE1MTk3YmUyNzEyYzNiOTYyYzk3ZWJmOWU5ZWQ4ZWZhMDI1In19fQ";
    private static final String TICKS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0=";

    private static final List<String> EVENTS_LIST = Lists.newArrayList("toolbar-slot-change", "jump");
    @Serializable(headTexture = BLOCK_HEAD, fromList = "getEventsList", description = "gui.action.block-event.blocked-event")
    private String blockedEvent;
    @Serializable(headTexture = TICKS_HEAD, description = "gui.action.block-event.ticks", additionalDescription = {"gui.additional-tooltips.value"})
    private NumericValue ticks;

    public BlockEventAction(boolean target, String blockedEvent, NumericValue ticks) {
        super(target);
        this.blockedEvent = blockedEvent;
        this.ticks = ticks;
    }

    public BlockEventAction() {
        this(TARGET_DEFAULT, "jump", new NumericValue(100));
    }

    public static BlockEventAction deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        String blockedEvent = (String) map.get("blockedEvent");
        NumericValue ticks = (NumericValue) map.get("ticks");
        return new BlockEventAction(target, blockedEvent, ticks);
    }

    public static List<String> getEventsList(ModelPath path) {
        return EVENTS_LIST;
    }

    @Override
    protected ActionResult execute(Target target, Source source) {
        LivingEntity e = getEntity(target, source);

        if (e == null) {
            return ActionResult.FAILURE;
        }

        e.setMetadata(blockedEvent, new FixedMetadataValue(SupremeItem.getInstance(), true));

        Bukkit.getScheduler().runTaskLater(SupremeItem.getInstance(), () -> {
            e.removeMetadata(blockedEvent, SupremeItem.getInstance());
        }, ticks.getRealValue(target, source).intValue());
        return ActionResult.SUCCESS;
    }

    @Override
    public Action clone() {
        return null;
    }

    @Override
    public String getName() {
        return String.format("&6&lBlock Event: &c%s", blockedEvent);
    }
}
