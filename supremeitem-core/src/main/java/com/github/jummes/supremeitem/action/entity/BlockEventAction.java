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
// TODO tooltips
public class BlockEventAction extends EntityAction {

    private static final String AMOUNT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    private static final List<String> EVENTS_LIST = Lists.newArrayList("toolbar-slot-change", "jump");
    @Serializable(headTexture = AMOUNT_HEAD, fromList = "getEventsList", description = "gui.action.consume-item.amount", additionalDescription = {"gui.additional-tooltips.value"})
    private String blockedEvent;
    @Serializable(headTexture = AMOUNT_HEAD, description = "gui.action.consume-item.amount", additionalDescription = {"gui.additional-tooltips.value"})
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
}
