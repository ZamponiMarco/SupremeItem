package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.savedskill.SavedSkill;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Enumerable.Child
@AllArgsConstructor
public class SkillAction extends MetaAction {

    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(headTexture = HEAD, fromList = "getSkills", fromListMapper = "skillsMapper")
    private String skillName;

    public SkillAction() {
        this("");
    }

    public static SkillAction deserialize(Map<String, Object> map) {
        String skillName = (String) map.getOrDefault("skillName", "");
        return new SkillAction(skillName);
    }

    public static List<Object> getSkills(ModelPath path) {
        return SupremeItem.getInstance().getSavedSkillManager().getSkills().stream().
                map(skill -> skill.getName()).collect(Collectors.toList());
    }

    public static Function<Object, ItemStack> skillsMapper() {
        return obj -> {
            String skill = (String) obj;
            return ItemUtils.getNamedItem(new ItemStack(Material.STONE), skill, new ArrayList<>());
        };
    }

    @Override
    protected void execute(Target target, Source source) {
        SavedSkill skill = SupremeItem.getInstance().getSavedSkillManager().getByName(skillName);
        if (skill != null) {
            skill.getActions().forEach(action -> action.executeAction(target, source));
        }
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(LocationTarget.class, EntityTarget.class);
    }
}
