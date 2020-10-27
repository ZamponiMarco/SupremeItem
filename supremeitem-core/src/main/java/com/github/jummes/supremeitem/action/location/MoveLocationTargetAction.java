package com.github.jummes.supremeitem.action.location;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.math.Vector;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@Getter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lMove target location", description = "gui.action.move-location.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNmYzUyMjY0ZDhhZDllNjU0ZjQxNWJlZjAxYTIzOTQ3ZWRiY2NjY2Y2NDkzNzMyODliZWE0ZDE0OTU0MWY3MCJ9fX0=")
public class MoveLocationTargetAction extends LocationAction {

    private static final String ACTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";
    private static final String VECTOR_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNmYzUyMjY0ZDhhZDllNjU0ZjQxNWJlZjAxYTIzOTQ3ZWRiY2NjY2Y2NDkzNzMyODliZWE0ZDE0OTU0MWY3MCJ9fX0";

    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.action.move-location.actions")
    private List<Action> actions;
    @Serializable(headTexture = VECTOR_HEAD, description = "gui.action.move-location.vector")
    private Vector vector;

    public MoveLocationTargetAction() {
        this(TARGET_DEFAULT, Lists.newArrayList(), new Vector());
    }

    public MoveLocationTargetAction(boolean target, List<Action> actions, Vector vector) {
        super(target);
        this.actions = actions;
        this.vector = vector;
    }

    public static MoveLocationTargetAction deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        List<Action> actions = (List<Action>) map.get("actions");
        Vector vector = (Vector) map.get("vector");
        return new MoveLocationTargetAction(target, actions, vector);
    }

    @Override
    protected ActionResult execute(Target target, Source source) {
        if (actions.stream().anyMatch(action -> action.executeAction(new LocationTarget(getLocation(target, source).add(
                vector.computeVector(target, source))), source).equals(ActionResult.CANCELLED))) {
            return ActionResult.CANCELLED;
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(LocationTarget.class, EntityTarget.class);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(VECTOR_HEAD),
                "&6&lMove Location: &c" + vector.toString(), Libs.getLocale().getList("gui.action.description"));
    }

    @Override
    public Action clone() {
        return new MoveLocationTargetAction(target, actions.stream().map(Action::clone).collect(Collectors.toList()), vector.clone());
    }
}