package com.github.jummes.supremeitem.action.item;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.StringValue;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lSet Item Lore", description = "gui.action.item.lore.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDg4M2Q2NTZlNDljMzhjNmI1Mzc4NTcyZjMxYzYzYzRjN2E1ZGQ0Mzc1YjZlY2JjYTQzZjU5NzFjMmNjNGZmIn19fQ==")
public class ItemLoreAction extends ItemAction {

    private static final int LINE_DEFAULT = 0;
    private static final StringValue LORE_DEFAULT = new StringValue("example");

    private final static String LORE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDg4M2Q2NTZlNDljMzhjNmI1Mzc4NTcyZjMxYzYzYzRjN2E1ZGQ0Mzc1YjZlY2JjYTQzZjU5NzFjMmNjNGZmIn19fQ";
    private static final String LINE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(headTexture = LINE_HEAD, description = "gui.action.item.lore.line")
    @Serializable.Number(minValue = 0, scale = 1)
    private int line;
    @Serializable(headTexture = LORE_HEAD, description = "gui.action.item.lore.lore", additionalDescription = {"gui.additional-tooltips.value"})
    private StringValue lore;

    public ItemLoreAction() {
        this(TARGET_DEFAULT, LINE_DEFAULT, LORE_DEFAULT);
    }

    public ItemLoreAction(boolean target, int line, StringValue lore) {
        super(target);
        this.line = line;
        this.lore = lore;
    }

    public ItemLoreAction(Map<String, Object> map) {
        super(map);
        this.line = (int) map.getOrDefault("line", 0);
        this.lore = (StringValue) map.getOrDefault("lore", LORE_DEFAULT);
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        ItemStack item = getItemStack(target, source);
        if (item != null) {
            ItemMeta meta = item.getItemMeta();
            List<String> itemLore = meta.getLore();
            String realValue = lore.getRealValue(target, source);
            if (itemLore == null) {
                itemLore = new ArrayList<>(line + 1);
            }
            if (itemLore.size() < line + 1) {
                while (itemLore.size() < line + 1) {
                    itemLore.add("");
                }
            }
            itemLore.set(line, realValue);
            meta.setLore(itemLore);
            item.setItemMeta(meta);
        }
        return ActionResult.FAILURE;
    }

    @Override
    public String getName() {
        return "&6&lSet Item Lore » Line: &c" + line + "&6&l, Lore: &c" + lore.getName();
    }

    @Override
    public Action clone() {
        return new ItemLoreAction(target, line, lore.clone());
    }
}
