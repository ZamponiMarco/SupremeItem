package com.github.jummes.supremeitem.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.NumericValue;
import com.github.jummes.suprememob.mob.Mob;
import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lSupremeMob", description = "gui.entity.supreme.description", condition = "supremeMobsEnabled", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZmYzg1NGJiODRjZjRiNzY5NzI5Nzk3M2UwMmI3OWJjMTA2OTg0NjBiNTFhNjM5YzYwZTVlNDE3NzM0ZTExIn19fQ==")
public class SupremeMobEntity extends Entity {

    private static final String MOB_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZmYzg1NGJiODRjZjRiNzY5NzI5Nzk3M2UwMmI3OWJjMTA2OTg0NjBiNTFhNjM5YzYwZTVlNDE3NzM0ZTExIn19fQ";
    private static final String LEVEL_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjUwNTU2YTg0MTY0MDY5ZGYzYjg5NTkzOWQwYWI1MDhmZmE4ZTE0MDQ3MTA2OTM4YjU1OWY1ODg5ZTViZmJlNCJ9fX0=";

    @Serializable(headTexture = MOB_HEAD, description = "gui.entity.supreme.mob", fromList = "getMobs", fromListMapper = "mobsMapper")
    private String mob;
    @Serializable(headTexture = LEVEL_HEAD, description = "gui.entity.supreme.level", additionalDescription = {"gui.additional-tooltips.value"})
    private NumericValue level;

    public SupremeMobEntity() {
        this("", new NumericValue(1));
    }

    public SupremeMobEntity(String mob, NumericValue level) {
        this.mob = mob;
        this.level = level;
    }

    public SupremeMobEntity(Map<String, Object> map) {
        super(map);
        this.mob = (String) map.getOrDefault("mob", "");
        this.level = (NumericValue) map.getOrDefault("level", new NumericValue(1));
    }

    public static boolean supremeMobsEnabled(ModelPath path) {
        return SupremeItem.getInstance().getSupremeMobHook().isEnabled();
    }

    public static List<Object> getMobs(ModelPath path) {
        return new ArrayList<>(SupremeItem.getInstance().getSupremeMobHook().getMobsList());
    }

    public static Function<Object, ItemStack> mobsMapper() {
        return obj -> {
            String mobStr = (String) obj;
            Mob mob = SupremeItem.getInstance().getSupremeMobHook().getByName(mobStr);
            if (mob != null) {
                return ItemUtils.getNamedItem(mob.getGUIItem(), mob.getGUIItem().getItemMeta().getDisplayName(),
                        Lists.newArrayList());
            }
            return new ItemStack(Material.CARROT);
        };
    }

    @Override
    public org.bukkit.entity.Entity spawnEntity(Location l, Target target, Source source) {
        if (!SupremeItem.getInstance().getSupremeMobHook().isEnabled()) {
            return null;
        }

        Mob selected = SupremeItem.getInstance().getSupremeMobHook().getByName(mob);
        if (selected != null) {
            return selected.spawn(l, 1);
        }
        return null;
    }

    @Override
    public Entity clone() {
        return new SupremeMobEntity(mob, level.clone());
    }

    @Override
    public EntityType getType() {
        Mob selected = SupremeItem.getInstance().getSupremeMobHook().getByName(mob);
        if (selected != null) {
            return selected.getType();
        }
        return EntityType.UNKNOWN;
    }
}
