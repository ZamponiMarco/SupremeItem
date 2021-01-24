package com.github.jummes.supremeitem.item;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.savedskill.SavedSkill;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Getter
public class ItemFolder extends AbstractItem {

    private static final String SKILL_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmJiMTI1NmViOWY2NjdjMDVmYjIxZTAyN2FhMWQ1MzU1OGJkYTc0ZTI0MGU0ZmE5ZTEzN2Q4NTFjNDE2ZmU5OCJ9fX0=";
    private static final String FOLDER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTYzMzBhNGEyMmZmNTU4NzFmYzhjNjE4ZTQyMWEzNzczM2FjMWRjYWI5YzhlMWE0YmI3M2FlNjQ1YTRhNGUifX19";
    protected static int folderCounter = 1;
    @Serializable(headTexture = SKILL_HEAD)
    private List<AbstractItem> items;

    public ItemFolder() {
        super(nextAvailableName());
        this.items = Lists.newArrayList();
    }

    public ItemFolder(String name, List<AbstractItem> items) {
        super(name);
        this.items = items;
        folderCounter++;
    }

    public ItemFolder(Map<String, Object> map) {
        super(map);
        this.items = (List<AbstractItem>) map.getOrDefault("items", Lists.newArrayList());
        folderCounter++;
    }

    protected static String nextAvailableName() {
        String name;
        do {
            name = "folder" + folderCounter;
            folderCounter++;
        } while (SupremeItem.getInstance().getItemManager().getAbstractItemByName(name) != null);
        return name;
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(FOLDER_HEAD), "&c&l" + name,
                Libs.getLocale().getList("gui.item.folder-description"));
    }

    @Override
    public void changeSkillName(String oldName, String newName) {
        items.forEach(item -> item.changeSkillName(oldName, newName));
    }

    @Override
    public List<SavedSkill> getUsedSavedSkills() {
        return items.stream().reduce(Lists.newArrayList(), (list, item) -> {
            list.addAll(item.getUsedSavedSkills());
            return list;
        }, (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        });
    }

    @Override
    public Item getByName(String name) {
        return (Item) items.stream().filter(item -> Objects.nonNull(item.getByName(name))).findFirst().
                orElse(null);
    }

    @Override
    public Item getById(UUID uuid) {
        return (Item) items.stream().filter(item -> Objects.nonNull(item.getById(uuid))).findFirst().
                orElse(null);
    }

}
