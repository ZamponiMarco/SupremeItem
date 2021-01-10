package com.github.jummes.supremeitem.action.item;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.StringValue;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lSet Item Name", description = "gui.action.item.name.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDU4NGNmN2Q3OWYxYWViMjU1NGMxYmZkNDZlNmI3OGNhNmFlM2FhMmEyMTMyMzQ2YTQxMGYxNWU0MjZmMzEifX19")
public class ItemNameAction extends ItemAction {

    private final static String NAME_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDU4NGNmN2Q3OWYxYWViMjU1NGMxYmZkNDZlNmI3OGNhNmFlM2FhMmEyMTMyMzQ2YTQxMGYxNWU0MjZmMzEifX19";

    private static final StringValue NAME_DEFAULT = new StringValue("example");

    @Serializable(headTexture = NAME_HEAD, description = "gui.action.item.name.name", additionalDescription = {"gui.additional-tooltips.value"})
    private StringValue name;

    public ItemNameAction() {
        this(TARGET_DEFAULT, NAME_DEFAULT);
    }

    public ItemNameAction(boolean target, StringValue name) {
        super(target);
        this.name = name;
    }

    public ItemNameAction(Map<String, Object> map) {
        super(map);
        this.name = (StringValue) map.getOrDefault("name", NAME_DEFAULT.clone());
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        ItemStack item = getItemStack(target, source);
        if (item != null) {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(MessageUtils.color(name.getRealValue(target, source)));
            item.setItemMeta(meta);
        }
        return ActionResult.FAILURE;
    }

    @Override
    public String getName() {
        return "&6&lSet Item name: &c" + name.getName();
    }

    @Override
    public Action clone() {
        return new ItemNameAction(target, name.clone());
    }
}
