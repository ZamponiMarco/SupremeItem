package com.github.jummes.supremeitem.action;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.entity.EntityAction;
import com.github.jummes.supremeitem.action.location.LocationAction;
import com.github.jummes.supremeitem.action.meta.MetaAction;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.action.variable.VariableAction;
import com.github.jummes.supremeitem.savedskill.SavedSkill;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

@Enumerable.Parent(classArray = {EntityAction.class,
        LocationAction.class, MetaAction.class, VariableAction.class})
public abstract class Action implements Model, Cloneable {

    protected static final boolean TARGET_DEFAULT = true;

    private static final String TARGET_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc4N2I3YWZiNWE1OTk1Mzk3NWJiYTI0NzM3NDliNjAxZDU0ZDZmOTNjZWFjN2EwMmFjNjlhYWU3ZjliOCJ9fX0==";

    protected transient SupremeItem plugin;

    @Serializable(displayItem = "targetItem", description = "gui.action.target")
    @Serializable.Optional(defaultValue = "TARGET_DEFAULT")
    protected boolean target;

    public Action(boolean target) {
        this.plugin = SupremeItem.getInstance();
        this.target = target;
    }

    /**
     * Executes the action.
     *
     * @param target The target of the action.
     * @param source The source of the action.
     * @return The ActionResult that describes how the action went.
     */
    public abstract ActionResult execute(Target target, Source source);

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(getClass().getAnnotation(Enumerable.Displayable.class).
                headTexture()), getName(), Libs.getLocale().getList("gui.action.description"));
    }

    @Override
    public abstract Action clone();

    public ItemStack targetItem() {
        return Libs.getWrapper().skullFromValue(TARGET_HEAD);
    }

    public abstract String getName();

    public Set<SavedSkill> getUsedSavedSkills() {
        return new HashSet<>();
    }

    public enum ActionResult {
        /**
         * An action concluded with success.
         */
        SUCCESS,
        /**
         * An action that has been cancelled.
         */
        CANCELLED,
        /**
         * An action that failed.
         */
        FAILURE
    }
}
