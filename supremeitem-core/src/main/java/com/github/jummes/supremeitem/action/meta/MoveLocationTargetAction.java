package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.math.Vector;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Setter
@Getter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lMove target location", description = "gui.action.move-location.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNmYzUyMjY0ZDhhZDllNjU0ZjQxNWJlZjAxYTIzOTQ3ZWRiY2NjY2Y2NDkzNzMyODliZWE0ZDE0OTU0MWY3MCJ9fX0=")
public class MoveLocationTargetAction extends WrapperAction {

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

    public MoveLocationTargetAction(Map<String, Object> map) {
        super(map);
        this.actions = (List<Action>) map.getOrDefault("actions", Lists.newArrayList());
        this.actions.removeIf(Objects::isNull);
        this.vector = (Vector) map.getOrDefault("vector", new Vector());
    }

    @Override
    public List<Action> getWrappedActions() {
        return actions;
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        if (actions.stream().anyMatch(action -> action.execute(new LocationTarget(getLocation(target, source).
                clone().add(vector.computeVector(target, source))), source).equals(ActionResult.CANCELLED))) {
            return ActionResult.CANCELLED;
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public Action clone() {
        return new MoveLocationTargetAction(target, actions.stream().map(Action::clone).collect(Collectors.toList()), vector.clone());
    }

    @Override
    public String getName() {
        return "&6&lMove Location: &c" + vector.toString();
    }

    @Override
    public void changeSkillName(String oldName, String newName) {
        actions.forEach(action -> action.changeSkillName(oldName, newName));
    }
}
