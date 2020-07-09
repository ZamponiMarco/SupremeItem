package com.github.jummes.supremeitem.entity.selector;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.Source;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.function.Predicate;

@AllArgsConstructor
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lFilter the skill source", description = "gui.entity.selector.source.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM2Mzc5NjFmODQ1MWE1M2I2N2QyNTMxMmQzNTBjNjIwZjMyYjVmNjA4YmQ2YWRlMDY2MzdiZTE3MTJmMzY0ZSJ9fX0=")
public class SourceSelector extends EntitySelector {

    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(headTexture = HEAD, description = "gui.entity.selector.source.deny")
    private boolean deny;

    public SourceSelector() {
        this(true);
    }

    public static SourceSelector deserialize(Map<String, Object> map) {
        boolean deny = (boolean) map.get("deny");
        return new SourceSelector(deny);
    }

    @Override
    public Predicate<LivingEntity> getFilter(Source source) {
        return entity -> {
            if (source instanceof EntitySource) {
                return deny != ((EntitySource) source).getSource().equals(entity);
            }
            return deny;
        };
    }

    @Override
    public ItemStack getGUIItem() {
        return new ItemStack(Material.CARROT);
    }
}
