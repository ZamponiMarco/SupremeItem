package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.action.Action;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Enumerable.Parent(classArray = {CooldownSkill.class, LeftClickSkill.class, RightClickSkill.class, HitEntitySkill.class,
        TimerSkill.class, DamageEntitySkill.class, EntitySneakSkill.class, EntitySprintSkill.class, EntityBowShootSkill.class})
public abstract class Skill implements Model {

    protected static final List<Action> ACTIONS_DEFAULT = Lists.newArrayList();
    protected static final boolean CONSUMABLE_DEFAULT = false;
    protected static final Set<EquipmentSlot> DEFAULT_SLOTS = Sets.newHashSet(EquipmentSlot.values());

    protected static final String CONSUMABLE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTg0YTY4ZmQ3YjYyOGQzMDk2NjdkYjdhNTU4NTViNTRhYmMyM2YzNTk1YmJlNDMyMTYyMTFiZTVmZTU3MDE0In19fQ==";
    private static final String SLOTS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGQ5YjY4OTE1YjE0NzJkODllNWUzYTliYTZjOTM1YWFlNjAzZDEyYzE0NTRmMzgyMjgyNWY0M2RmZThhMmNhYyJ9fX0=";
    private static final String ITEM_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWI0MjVhYTNkOTQ2MThhODdkYWM5Yzk0ZjM3N2FmNmNhNDk4NGMwNzU3OTY3NGZhZDkxN2Y2MDJiN2JmMjM1In19fQ==";

    @Serializable(headTexture = CONSUMABLE_HEAD, description = "gui.skill.consumable")
    @Serializable.Optional(defaultValue = "CONSUMABLE_DEFAULT")
    protected boolean consumable;
    @Serializable(headTexture = SLOTS_HEAD, description = "gui.skill.slots")
    protected Set<EquipmentSlot> allowedSlots;
    @Serializable(headTexture = ITEM_HEAD, description = "gui.skill.item-actions")
    protected List<Action> onItemActions;

    public Skill(boolean consumable, Set<EquipmentSlot> allowedSlots, List<Action> onItemActions) {
        this.consumable = consumable;
        this.allowedSlots = allowedSlots;
        this.onItemActions = onItemActions;
    }

    public Skill(Map<String, Object> map) {
        this.consumable = (boolean) map.getOrDefault("consumable", CONSUMABLE_DEFAULT);
        this.allowedSlots = ((List<String>) map.getOrDefault("allowedSlots",
                Arrays.stream(EquipmentSlot.values()).map(EquipmentSlot::name).collect(Collectors.toList()))).
                stream().map(EquipmentSlot::valueOf).collect(Collectors.toSet());
        this.onItemActions = (List<Action>) map.getOrDefault("onItemActions", Lists.newArrayList());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        if (consumable != CONSUMABLE_DEFAULT)
            map.put("consumable", consumable);
        if (!allowedSlots.equals(DEFAULT_SLOTS))
            map.put("allowedSlots", allowedSlots.stream().map(EquipmentSlot::name).collect(Collectors.toList()));
        map.put("onItemActions", onItemActions);
        return map;
    }

    @Override
    public boolean equals(Object obj) {
        return getClass().equals(obj.getClass());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    protected void consumeIfConsumable(UUID id, ItemStack item) {
        if (consumable) {
            int amount = item.getAmount();
            item.setAmount(--amount);
        }
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(getClass().getAnnotation(Enumerable.Displayable.class).headTexture()),
                getName(), Libs.getLocale().getList("gui.additional-tooltips.delete"));
    }

    public abstract String getName();

    public abstract void changeSkillName(String oldName, String newName);

    public enum SkillResult {
        SUCCESS,
        CANCELLED,
        FAILURE
    }
}
