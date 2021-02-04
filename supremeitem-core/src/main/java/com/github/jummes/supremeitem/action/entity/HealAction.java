package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.NumericValue;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

@Enumerable.Displayable(name = "&c&lHeal", description = "gui.action.entity.heal.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjEyNjZiNzQ4MjQyMTE1YjMwMzcwOGQ1OWNlOWQ1NTIzYjdkNzljMTNmNmRiNGViYzkxZGQ0NzIwOWViNzU5YyJ9fX0=")
@Enumerable.Child
@Getter
@Setter
public class HealAction extends EntityAction {

    private static final NumericValue AMOUNT_DEFAULT = new NumericValue(1);

    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(headTexture = HEAD, description = "gui.action.entity.heal.amount", additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Number(minValue = 0)
    @Serializable.Optional(defaultValue = "AMOUNT_DEFAULT")
    private NumericValue amount;

    public HealAction() {
        this(TARGET_DEFAULT, AMOUNT_DEFAULT.clone());
    }

    public HealAction(boolean target, NumericValue amount) {
        super(target);
        this.amount = amount;
    }

    public HealAction(Map<String, Object> map) {
        super(map);
        this.amount = (NumericValue) map.getOrDefault("amount", AMOUNT_DEFAULT.clone());
    }

    @Override
    public ActionResult execute(Target target, Source source, Map<String, Object> map) {
        LivingEntity e = getEntity(target, source);

        if (e == null) {
            return ActionResult.FAILURE;
        }

        healEntity(e, target, source);
        return ActionResult.SUCCESS;
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
    public Action clone() {
        return new HealAction(target, amount.clone());
    }

    @Override
    public String getName() {
        return "&6&lHeal: &c" + amount.getName();
    }

}
