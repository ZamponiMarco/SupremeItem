package com.github.jummes.supremeitem.item;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.wrapper.ItemStackWrapper;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.savedskill.SavedSkill;
import com.github.jummes.supremeitem.skill.Skill;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public class Item extends AbstractItem {

    private static final String SKILL_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmJiMTI1NmViOWY2NjdjMDVmYjIxZTAyN2FhMWQ1MzU1OGJkYTc0ZTI0MGU0ZmE5ZTEzN2Q4NTFjNDE2ZmU5OCJ9fX0=";
    protected static int itemCounter = 1;
    @Serializable(stringValue = true)
    private UUID id;
    @Serializable(description = "gui.item.item", displayItem = "getUsableItem",
            additionalDescription = {"gui.additional-tooltips.item"})
    private ItemStackWrapper item;
    @Serializable(headTexture = SKILL_HEAD, description = "gui.item.skill-set")
    private Set<Skill> skillSet;

    public Item() {
        super(nextAvailableName());
        this.id = UUID.randomUUID();
        this.item = new ItemStackWrapper(true);
        this.skillSet = Sets.newHashSet();
    }

    public Item(UUID id, String name, ItemStackWrapper item, Set<Skill> skillSet) {
        super(name);
        this.id = id;
        this.item = item;
        this.skillSet = skillSet;
        itemCounter++;
    }

    public Item(Map<String, Object> map) {
        super(map);
        this.id = UUID.fromString((String) map.get("id"));
        this.item = (ItemStackWrapper) map.getOrDefault("item", new ItemStackWrapper());
        this.skillSet = new HashSet<>((List<Skill>) map.getOrDefault("skillSet", Lists.newArrayList()));
        itemCounter++;
    }

    protected static String nextAvailableName() {
        String name;
        do {
            name = "item" + itemCounter;
            itemCounter++;
        } while (SupremeItem.getInstance().getItemManager().getAbstractItemByName(name) != null);
        return name;
    }

    public ItemStack getUsableItem() {
        if (item == null) {
            return getCorruptedItem();
        }
        ItemStack item = this.item.getWrapped().clone();
        item.setAmount(1);
        return Libs.getWrapper().addTagToItem(item, "supreme-item", getId().toString());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("==", getClass().getName());
        map.put("id", id.toString());
        map.put("name", name);
        map.put("item", item);
        map.put("skillSet", new ArrayList<>(skillSet));
        return map;
    }

    @Override
    public ItemStack getGUIItem() {
        if (item == null) {
            return getCorruptedItem();
        }
        List<String> lore = item.getWrapped().getItemMeta() == null ? null : item.getWrapped().getItemMeta().getLore();
        lore = lore == null ? Lists.newArrayList() : lore;
        lore.add(MessageUtils.color("&6&lName: &c" + name));
        lore.addAll(Libs.getLocale().getList("gui.item.description"));
        ItemStack itemStack = item.getWrapped().clone();
        itemStack.setAmount(1);
        return ItemUtils.getNamedItem(itemStack, item.getWrapped().getItemMeta().getDisplayName(), lore);
    }

    public void changeSkillName(String oldName, String newName) {
        skillSet.forEach(skill -> skill.changeSkillName(oldName, newName));
        SupremeItem.getInstance().getItemManager().saveModel(this);
    }

    public List<SavedSkill> getUsedSavedSkills() {
        return skillSet.stream().reduce(Lists.newArrayList(), (list, skill) -> {
            list.addAll(skill.getUsedSavedSkills());
            return list;
        }, (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        });
    }

    @Override
    public Item getByName(String name) {
        return this.name.equals(name) ? this : null;
    }

    @Override
    public Item getById(UUID uuid) {
        return this.id.equals(uuid) ? this : null;
    }

}
