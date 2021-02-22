package com.github.jummes.supremeitem.item;

import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.database.NamedModel;
import com.github.jummes.supremeitem.savedskill.SavedSkill;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public abstract class AbstractItem extends NamedModel {

    public AbstractItem(String name) {
        super(name);
    }

    public AbstractItem(Map<String, Object> map) {
        super(map);
    }

    protected ItemStack getCorruptedItem() {
        return ItemUtils.getNamedItem(new ItemStack(Material.BARRIER), "&4&lCorrupted", Lists.
                newArrayList(MessageUtils.color("&cThe display item is corrupted"),
                        MessageUtils.color("&cTry to set it again.")));
    }

    abstract public void changeSkillName(String oldName, String newName);

    abstract public Set<SavedSkill> getUsedSavedSkills();

    abstract public Item getByName(String name);

    abstract public Item getById(UUID uuid);

    @Override
    protected boolean isAlreadyPresent(String name) {
        return SupremeItem.getInstance().getItemManager().getAbstractItemByName(name) != null;
    }
}
