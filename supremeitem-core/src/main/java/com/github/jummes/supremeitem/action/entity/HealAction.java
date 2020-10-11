package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericValue;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Enumerable.Displayable(name = "&c&lHeal", description = "gui.action.heal.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjEyNjZiNzQ4MjQyMTE1YjMwMzcwOGQ1OWNlOWQ1NTIzYjdkNzljMTNmNmRiNGViYzkxZGQ0NzIwOWViNzU5YyJ9fX0=")
@Enumerable.Child
@Getter
@Setter
public class HealAction extends Action {

    private static final int AMOUNT_DEFAULT = 1;

    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(headTexture = HEAD, description = "gui.action.heal.amount")
    @Serializable.Number(minValue = 0)
    @Serializable.Optional(defaultValue = "AMOUNT_DEFAULT")
    private NumericValue amount;

    public HealAction() {
        this(new NumericValue(AMOUNT_DEFAULT));
    }

    public static HealAction deserialize(Map<String, Object> map) {
        NumericValue amount;
        try {
            amount = (NumericValue) map.getOrDefault("amount", new NumericValue(AMOUNT_DEFAULT));
        } catch (ClassCastException e) {
            amount = new NumericValue(((Number) map.getOrDefault("amount", AMOUNT_DEFAULT)).doubleValue());
        }
        return new HealAction(amount);
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        healEntity(((EntityTarget) target).getTarget(), target, source);
        return ActionResult.SUCCESS;
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(EntityTarget.class);
    }

    private void healEntity(LivingEntity e, Target target, Source source) {
        AttributeInstance attribute = e.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute != null) {
            double maxHealth = attribute.getValue();
            double currHealth = e.getHealth();

            e.setHealth(Math.min(currHealth + amount.getRealValue(target, source), maxHealth));
        }
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjEyNjZiNzQ4MjQyMTE1YjMwMzcwOGQ1OWNlOWQ1NTIzYjdkNzljMTNmNmRiNGViYzkxZGQ0NzIwOWViNzU5YyJ9fX0="),
                "&6&lHeal: &c" + amount.getName(), Libs.getLocale().getList("gui.action.description"));
    }

}
