package com.github.jummes.supremeitem.condition.bool;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.GUINameable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.condition.Condition;
import com.google.common.collect.Lists;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@GUINameable(GUIName = "getName")
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lAnd Condition", description = "gui.condition.and.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTJiOGIzMTVmODcxNmEzMWYzMmQ1NDM4NzRhMzdmOTRjZmY1ZGFmZDJhNTg2NTZmZjNhOWU4MjgwZWM0OWM3In19fQ")
public class AndCondition extends TrueFalseCondition {

    private static final List<Condition> CONDITIONS_DEFAULT = Lists.newArrayList();

    private static final String CONDITIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTJiOGIzMTVmODcxNmEzMWYzMmQ1NDM4NzRhMzdmOTRjZmY1ZGFmZDJhNTg2NTZmZjNhOWU4MjgwZWM0OWM3In19fQ==";

    @Serializable(headTexture = CONDITIONS_HEAD, description = "gui.condition.and.conditions")
    @Serializable.Optional(defaultValue = "CONDITIONS_DEFAULT")
    private List<Condition> conditions;

    public AndCondition() {
        this(NEGATE_DEFAULT, Lists.newArrayList());
    }

    public AndCondition(boolean negate, List<Condition> conditions) {
        super(negate);
        this.conditions = conditions;
    }

    public static AndCondition deserialize(Map<String, Object> map) {
        boolean negate = (boolean) map.getOrDefault("negate", NEGATE_DEFAULT);
        List<Condition> conditions = (List<Condition>) map.getOrDefault("conditions", Lists.newArrayList());
        return new AndCondition(negate, conditions);
    }

    @Override
    public boolean testCondition(Target target, Source source) {
        return conditions.stream().allMatch(condition -> condition.checkCondition(target, source));
    }

    @Override
    public String getName() {
        String[] s = conditions.stream().map(condition -> "&6&l(" + condition.getName() + "&6&l)").toArray(String[]::new);
        return String.join(" &6&land&c ", s);
    }

    @Override
    public Condition clone() {
        return new AndCondition(negate, conditions.stream().map(Condition::clone).collect(Collectors.toList()));
    }
}
