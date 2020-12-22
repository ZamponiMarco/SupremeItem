package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.NumericValue;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Map;

@Enumerable.Displayable(name = "&c&lDamage", description = "gui.action.entity.damage.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWVlMTE4ZWRkYWVlMGRmYjJjYmMyYzNkNTljMTNhNDFhN2Q2OGNjZTk0NWU0MjE2N2FhMWRjYjhkMDY3MDUxNyJ9fX0=")
@Enumerable.Child
@Getter
@Setter
public class DamageAction extends EntityAction {

    private static final boolean ABSOLUTE_DEFAULT = false;
    private static final NumericValue AMOUNT_DEFAULT = new NumericValue(1);

    private static final String AMOUNT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";
    private static final String ABSOLUTE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzdlNmM0MGY2OGI3NzVmMmVmY2Q3YmQ5OTE2YjMyNzg2OWRjZjI3ZTI0Yzg1NWQwYTE4ZTA3YWMwNGZlMSJ9fX0=";

    @Serializable(headTexture = AMOUNT_HEAD, description = "gui.action.entity.damage.amount", additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Number(minValue = 0)
    @Serializable.Optional(defaultValue = "AMOUNT_DEFAULT")
    private NumericValue amount;
    @Serializable(headTexture = AMOUNT_HEAD, description = "gui.action.entity.damage.absolute")
    @Serializable.Optional(defaultValue = "ABSOLUTE_DEFAULT")
    private boolean absolute;

    public DamageAction() {
        this(TARGET_DEFAULT, AMOUNT_DEFAULT.clone(), ABSOLUTE_DEFAULT);
    }

    public DamageAction(boolean target, NumericValue amount, boolean absolute) {
        super(target);
        this.amount = amount;
        this.absolute = absolute;
    }

    public DamageAction(Map<String, Object> map) {
        super(map);
        this.absolute = (boolean) map.getOrDefault("absolute", ABSOLUTE_DEFAULT);
        this.amount = (NumericValue) map.getOrDefault("amount", AMOUNT_DEFAULT.clone());
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        LivingEntity e = getEntity(target, source);

        if (e == null) {
            return ActionResult.FAILURE;
        }

        e.setMetadata("siattack", new FixedMetadataValue(SupremeItem.getInstance(), true));
        if (!absolute) {
            e.damage(amount.getRealValue(target, source), source.getCaster());
        } else {
            e.setHealth(e.getHealth() - amount.getRealValue(target, source));
            e.damage(0);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public Action clone() {
        return new DamageAction(target, amount.clone(), absolute);
    }

    @Override
    public String getName() {
        return "&6&lDamage: &c" + amount.getName();
    }

}
