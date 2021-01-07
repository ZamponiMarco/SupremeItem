package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.savedskill.SavedSkill;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Enumerable.Child
@Getter
@Setter
@Enumerable.Displayable(name = "&c&lExecute a saved skill", description = "gui.action.meta.skill.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM1NGM4YmE3NDIwNzlhZWQ1YWNmYmYwN2M0MjhiNDA2YmMwOTJkYjhhYmM2ZjE3ZjcwNTkwOTliMDQ5NTliZCJ9fX0=")
public class SkillAction extends MetaAction {

    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0=";

    @Serializable(headTexture = HEAD, fromList = "getSkills", fromListMapper = "skillsMapper", description = "gui.action.meta.skill.name")
    private String skillName;

    public SkillAction() {
        this(TARGET_DEFAULT, "");
    }

    public SkillAction(boolean target, String skillName) {
        super(target);
        this.skillName = skillName;
    }

    public SkillAction(Map<String, Object> map) {
        super(map);
        this.skillName = (String) map.getOrDefault("skillName", "");
    }

    public static List<Object> getSkills(ModelPath<?> path) {
        return SupremeItem.getInstance().getSavedSkillManager().getSkills().stream().
                map(SavedSkill::getName).collect(Collectors.toList());
    }

    public static Function<Object, ItemStack> skillsMapper() {
        return obj -> {
            String skill = (String) obj;
            return ItemUtils.getNamedItem(SupremeItem.getInstance().getSavedSkillManager().getByName(skill).getItem().
                    getWrapped().clone(), "&6&l" + skill, Lists.newArrayList());
        };
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        SavedSkill skill = SupremeItem.getInstance().getSavedSkillManager().getByName(skillName);
        if (skill != null) {
            return skill.getActions().stream().filter(action -> action.execute(target, source).
                    equals(ActionResult.CANCELLED)).count() > 0 ? ActionResult.CANCELLED : ActionResult.SUCCESS;
        }
        return ActionResult.FAILURE;
    }

    @Override
    public Action clone() {
        return new SkillAction(TARGET_DEFAULT, skillName);
    }

    @Override
    public ItemStack targetItem() {
        return null;
    }

    @Override
    public String getName() {
        return "&6&lSkill name: &c" + skillName;
    }

    @Override
    public ItemStack getGUIItem() {
        ItemStack item = super.getGUIItem();
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        lore.add(3 + WrapperAction.WRAPPERS_MAP.size(), MessageUtils.color("&6&l- [9] &eto open the skill GUI"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void changeSkillName(String oldName, String newName) {
        if (this.skillName.equals(oldName)) {
            this.skillName = newName;
        }
    }
}
