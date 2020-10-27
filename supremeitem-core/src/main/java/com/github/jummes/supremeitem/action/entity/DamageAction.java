package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.NumericValue;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;
import java.util.Map;

@Enumerable.Displayable(name = "&c&lDamage", description = "gui.action.damage.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWVlMTE4ZWRkYWVlMGRmYjJjYmMyYzNkNTljMTNhNDFhN2Q2OGNjZTk0NWU0MjE2N2FhMWRjYjhkMDY3MDUxNyJ9fX0=")
@Enumerable.Child
@Getter
@Setter
public class DamageAction extends EntityAction {

    private static final NumericValue AMOUNT_DEFAULT = new NumericValue(1);

    private static final String AMOUNT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(headTexture = AMOUNT_HEAD, description = "gui.action.damage.amount", additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Number(minValue = 0)
    @Serializable.Optional(defaultValue = "AMOUNT_DEFAULT")
    private NumericValue amount;

    public DamageAction() {
        this(TARGET_DEFAULT, AMOUNT_DEFAULT.clone());
    }

    public DamageAction(boolean target, NumericValue amount) {
        super(target);
        this.amount = amount;
    }

    public static DamageAction deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        NumericValue amount;
        try {
            amount = (NumericValue) map.getOrDefault("amount", AMOUNT_DEFAULT.clone());
        } catch (ClassCastException e) {
            amount = new NumericValue(((Number) map.getOrDefault("amount", AMOUNT_DEFAULT.getValue())));
        }
        return new DamageAction(target, amount);
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        LivingEntity e = getEntity(target, source);

        e.setMetadata("siattack", new FixedMetadataValue(SupremeItem.getInstance(), true));
        e.damage(amount.getRealValue(target, source), source.getCaster());
        return ActionResult.SUCCESS;
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(EntityTarget.class);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWVlMTE4ZWRkYWVlMGRmYjJjYmMyYzNkNTljMTNhNDFhN2Q2OGNjZTk0NWU0MjE2N2FhMWRjYjhkMDY3MDUxNyJ9fX0="),
                "&6&lDamage: &c" + amount.getName(), Libs.getLocale().getList("gui.action.description"));
    }

    @Override
    public Action clone() {
        return new DamageAction(target, amount.clone());
    }

}
