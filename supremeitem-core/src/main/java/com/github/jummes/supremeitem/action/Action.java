package com.github.jummes.supremeitem.action;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.action.entity.EntityAction;
import com.github.jummes.supremeitem.action.item.ItemAction;
import com.github.jummes.supremeitem.action.location.LocationAction;
import com.github.jummes.supremeitem.action.meta.MetaAction;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.action.variable.VariableAction;
import com.github.jummes.supremeitem.savedskill.SavedSkill;
import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@Enumerable.Parent(classArray = {EntityAction.class,
        LocationAction.class, MetaAction.class, VariableAction.class, ItemAction.class})
public abstract class Action implements Model, Cloneable {

    protected static final boolean TARGET_DEFAULT = true;

    private static final String TARGET_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc4N2I3YWZiNWE1OTk1Mzk3NWJiYTI0NzM3NDliNjAxZDU0ZDZmOTNjZWFjN2EwMmFjNjlhYWU3ZjliOCJ9fX0==";

    @Serializable(displayItem = "targetItem", description = "gui.action.target")
    @Serializable.Optional(defaultValue = "TARGET_DEFAULT")
    protected boolean target;

    public Action(boolean target) {
        this.target = target;
    }

    public Action(Map<String, Object> map) {
        this.target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
    }

    /**
     * Executes the action.
     *
     * @param target The target of the action.
     * @param source The source of the action.
     * @param map
     * @return The ActionResult that describes how the action went.
     */
    public abstract ActionResult execute(Target target, Source source, Map<String, Object> map);

    public abstract String getName();

    protected Location getLocation(Target target, Source source) {
        return getLocation(target, source, false);
    }

    protected Location getLocation(Target target, Source source, boolean eyes) {
        if (this.target) {
            if (eyes && target instanceof EntityTarget) {
                return ((EntityTarget) target).getTarget().getEyeLocation();
            }
            return target.getLocation();
        }
        if (eyes && source instanceof EntitySource) {
            return source.getCaster().getEyeLocation();
        }
        return source.getLocation();
    }

    public ItemStack targetItem() {
        return Libs.getWrapper().skullFromValue(TARGET_HEAD);
    }

    public void changeSkillName(String oldName, String newName) {
    }

    public List<SavedSkill> getUsedSavedSkills() {
        return Lists.newArrayList();
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(getClass().getAnnotation(Enumerable.Displayable.class).
                headTexture()), getName(), Libs.getLocale().getList("gui.action.description"));
    }

    @Override
    public abstract Action clone();

    public enum ActionResult {
        /**
         * An action concluded with success.
         */
        SUCCESS,
        /**
         * An action that failed.
         */
        FAILURE
    }
}
