package com.github.jummes.supremeitem.item.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.action.Action;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@Enumerable.Child
public class TimerSkill extends Skill {

    private static final String CUSTOM_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFkY2Y4NWRlYTg0NDFmN2FmMjg3ZmU3ZTAyMTFjNzRmYzY5YzI5MjNlZDQ5YTE2ZjZkZDFiOWU4MWEyNDlkMyJ9fX0=";

    @Serializable(headTexture = CUSTOM_HEAD)
    private List<Action> onWearerActions;
    @Serializable(headTexture = CUSTOM_HEAD)
    private int timer;


    public TimerSkill() {
        this(Lists.newArrayList(), 20);
    }

    public static TimerSkill deserialize(Map<String, Object> map) {
        List<Action> onWearerActions = (List<Action>) map.get("onWearerActions");
        int timer = (int) map.get("timer");
        return new TimerSkill(onWearerActions, timer);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(new ItemStack(Material.STICK), "&6Timer", Lists.newArrayList());
    }

}
