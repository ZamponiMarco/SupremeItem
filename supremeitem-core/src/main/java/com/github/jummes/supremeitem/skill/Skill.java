package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.libs.util.MapperUtils;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.savedskill.SavedSkill;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Enumerable.Parent(classArray = {CooldownSkill.class, InteractionSkill.class, CombatSkill.class,
        MovementSkill.class, TimerSkill.class})
public abstract class Skill implements Model {

    protected static final List<Action> ACTIONS_DEFAULT = Lists.newArrayList();
    protected static final boolean CONSUMABLE_DEFAULT = false;
    @Getter
    protected static final List<Slot> DEFAULT_SLOTS = Arrays.stream(org.bukkit.inventory.EquipmentSlot.values()).
            map(EquipmentSlot::new).collect(Collectors.toList());

    protected static final String CASTER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjY4YjQzMTE1MmU4MmFmNWRlZjg4ZjkxYmI2MWM2MjNiM2I3YWMzYWJlODJkMjc2ZmFkMzQ3Nzc2NDBmOTU5MCJ9fX0=";
    protected static final String CONSUMABLE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTg0YTY4ZmQ3YjYyOGQzMDk2NjdkYjdhNTU4NTViNTRhYmMyM2YzNTk1YmJlNDMyMTYyMTFiZTVmZTU3MDE0In19fQ==";
    private static final String SLOTS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGQ5YjY4OTE1YjE0NzJkODllNWUzYTliYTZjOTM1YWFlNjAzZDEyYzE0NTRmMzgyMjgyNWY0M2RmZThhMmNhYyJ9fX0=";
    private static final String ITEM_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWI0MjVhYTNkOTQ2MThhODdkYWM5Yzk0ZjM3N2FmNmNhNDk4NGMwNzU3OTY3NGZhZDkxN2Y2MDJiN2JmMjM1In19fQ==";

    public static Set<Integer> additionalSlots = Sets.newHashSet();
    public static List<Slot> slots = new ArrayList<>(DEFAULT_SLOTS);

    @EqualsAndHashCode.Include
    protected UUID id = UUID.randomUUID();

    @Serializable(headTexture = CONSUMABLE_HEAD, description = "gui.skill.consumable")
    @Serializable.Optional(defaultValue = "CONSUMABLE_DEFAULT")
    protected boolean consumable;
    @Serializable(headTexture = SLOTS_HEAD, description = "gui.skill.slots")
    protected Set<Slot> allowedSlots;
    @Serializable(headTexture = ITEM_HEAD, description = "gui.skill.item-actions")
    protected List<Action> onItemActions;

    public Skill(boolean consumable, Set<Slot> allowedSlots, List<Action> onItemActions) {
        this.consumable = consumable;
        this.allowedSlots = allowedSlots;
        this.onItemActions = onItemActions;
    }

    public Skill(Map<String, Object> map) {
        this.consumable = (boolean) map.getOrDefault("consumable", CONSUMABLE_DEFAULT);
        this.onItemActions = (List<Action>) map.getOrDefault("onItemActions", Lists.newArrayList());
        try {
            this.allowedSlots = Sets.newHashSet((List<Slot>) map.getOrDefault("allowedSlots", DEFAULT_SLOTS.
                    stream().map(Slot::clone).collect(Collectors.toList())));
            this.allowedSlots.stream().findAny().ifPresent(Model::onCreation);
        } catch (ClassCastException e) {
            List<String> slots = (List<String>) map.getOrDefault("allowedSlots", Lists.newArrayList());
            this.allowedSlots = slots.stream().map(string -> new EquipmentSlot(org.bukkit.inventory.EquipmentSlot.
                    valueOf(string))).collect(Collectors.toSet());
        }
    }

    public abstract void executeSkill(UUID id, ItemStack item, Map<String, Object> args);

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        if (consumable != CONSUMABLE_DEFAULT)
            map.put("consumable", consumable);
        map.put("allowedSlots", new ArrayList<>(allowedSlots));
        map.put("onItemActions", onItemActions);
        return map;
    }

    protected void consumeIfConsumable(UUID id, ItemStack item) {
        if (consumable) {
            int amount = item.getAmount();
            item.setAmount(--amount);
        }
    }

    @Override
    public ItemStack getGUIItem() {
        List<String> lore = getCustomLore();
        lore.addAll(Libs.getLocale().getList("gui.additional-tooltips.delete"));
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(getClass().getAnnotation(Enumerable.Displayable.class).headTexture()),
                getName(), lore);
    }

    public abstract String getName();

    public List<String> getCustomLore() {
        return new ArrayList<>();
    }

    public void changeSkillName(String oldName, String newName) {
        getAllActions().forEach(action -> action.changeSkillName(oldName, newName));
    }

    public List<Action> getAllActions() {
        List<Action> list = new ArrayList<>(this.onItemActions);
        list.addAll(getAbstractActions());
        return list;
    }

    public abstract List<Action> getAbstractActions();

    public List<SavedSkill> getUsedSavedSkills() {
        return getAllActions().stream().reduce(Lists.newArrayList(), (list, action) -> {
            list.addAll(action.getUsedSavedSkills());
            return list;
        }, (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        });
    }

    @Enumerable.Parent(classArray = {EquipmentSlot.class, NumberedSlot.class})
    @Enumerable.Displayable
    public abstract static class Slot implements Model, Cloneable {

        public Slot() {

        }

        public Slot(Map<String, Object> map) {

        }

        public abstract Slot clone();

    }

    @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
    @Enumerable.Child
    @Enumerable.Displayable(name = "&c&lEquipment Slot", description = "gui.skill.slot.armor.description",
            headTexture = SLOTS_HEAD)
    @Getter
    @ToString
    public static class EquipmentSlot extends Slot {

        @Serializable(headTexture = SLOTS_HEAD, description = "gui.skill.slot.slot", stringValue = true)
        @EqualsAndHashCode.Include
        private org.bukkit.inventory.EquipmentSlot slot;

        public EquipmentSlot() {
            this(org.bukkit.inventory.EquipmentSlot.HAND);
        }

        public EquipmentSlot(org.bukkit.inventory.EquipmentSlot slot) {
            this.slot = slot;
        }

        public EquipmentSlot(Map<String, Object> map) {
            super(map);
            this.slot = org.bukkit.inventory.EquipmentSlot.valueOf((String) map.getOrDefault("slot", "HAND"));
        }

        @Override
        public ItemStack getGUIItem() {
            return MapperUtils.getEquipmentSlotMapper().apply(slot);
        }

        @Override
        public Slot clone() {
            return new EquipmentSlot(slot);
        }
    }

    @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
    @Enumerable.Child
    @Enumerable.Displayable(name = "&c&lNumbered Slot", description = "gui.skill.slot.numbered.description",
            condition = "additionalSlotsEnabled", headTexture = SLOTS_HEAD)
    @Getter
    @ToString
    public static class NumberedSlot extends Slot {

        @Serializable(headTexture = SLOTS_HEAD, description = "gui.skill.slot.slot", fromList = "getSlots",
                fromListMapper = "slotsMapper")
        @EqualsAndHashCode.Include
        private int slot;

        public NumberedSlot() {
            this(additionalSlots.iterator().next());
        }

        public NumberedSlot(int slot) {
            this.slot = slot;
        }

        public NumberedSlot(Map<String, Object> map) {
            super(map);
            this.slot = (int) map.getOrDefault("slot", additionalSlots.iterator().next());
        }

        public static List<Object> getSlots(ModelPath<?> path) {
            return new ArrayList<>(Skill.additionalSlots);
        }

        public static Function<Object, ItemStack> slotsMapper() {
            return obj -> {
                int slot = (int) obj;
                return getItem(slot);
            };
        }

        private static ItemStack getItem(int slot) {
            ItemStack item = ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(SLOTS_HEAD),
                    "&6&lSlot: &c" + slot, Lists.newArrayList());
            item.setAmount(slot);
            return item;
        }

        public static boolean additionalSlotsEnabled(ModelPath path) {
            return !Skill.additionalSlots.isEmpty();
        }

        @Override
        public ItemStack getGUIItem() {
            return getItem(slot);
        }

        @Override
        public Slot clone() {
            return new NumberedSlot(slot);
        }
    }

}
