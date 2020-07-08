package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Enumerable.Displayable(name = "&c&lHeal", description = "gui.action.heal.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjEyNjZiNzQ4MjQyMTE1YjMwMzcwOGQ1OWNlOWQ1NTIzYjdkNzljMTNmNmRiNGViYzkxZGQ0NzIwOWViNzU5YyJ9fX0=")
@Enumerable.Child
public class HealAction extends Action {

    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(headTexture = HEAD, description = "gui.action.heal.amount")
    private int amount;

    public HealAction() {
        this(1);
    }

    public static HealAction deserialize(Map<String, Object> map) {
        int amount = (int) map.getOrDefault("amount", 1);
        return new HealAction(amount);
    }

    @Override
    public void execute(Target target, Source source) {
        healEntity(((EntityTarget) target).getTarget());
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(EntityTarget.class);
    }

    private void healEntity(LivingEntity e) {
        double maxHealth = e.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double currHealth = e.getHealth();

        e.setHealth(Math.min(currHealth + amount, maxHealth));
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjEyNjZiNzQ4MjQyMTE1YjMwMzcwOGQ1OWNlOWQ1NTIzYjdkNzljMTNmNmRiNGViYzkxZGQ0NzIwOWViNzU5YyJ9fX0="),
                "&6&lHeal: &c" + amount, Libs.getLocale().getList("gui.action.description"));
    }

}
