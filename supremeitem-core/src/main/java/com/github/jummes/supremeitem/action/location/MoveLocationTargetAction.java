package com.github.jummes.supremeitem.action.location;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericValue;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Setter
@Getter
@Enumerable.Child
public class MoveLocationTargetAction extends LocationAction {

    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWY4NDczNWZjOWM3NjBlOTVlYWYxMGNlYzRmMTBlZGI1ZjM4MjJhNWZmOTU1MWVlYjUwOTUxMzVkMWZmYTMwMiJ9fX0=";

    // TODO Heads and descriptions
    @Serializable(headTexture = HEAD)
    private List<Action> actions;
    @Serializable(headTexture = HEAD)
    private NumericValue movementX;
    @Serializable(headTexture = HEAD)
    private NumericValue movementY;
    @Serializable(headTexture = HEAD)
    private NumericValue movementZ;

    public MoveLocationTargetAction() {
        this(Lists.newArrayList(), new NumericValue(), new NumericValue(), new NumericValue());
    }

    public static MoveLocationTargetAction deserialize(Map<String, Object> map) {
        List<Action> actions = (List<Action>) map.get("actions");
        NumericValue movementX = (NumericValue) map.get("movementX");
        NumericValue movementY = (NumericValue) map.get("movementY");
        NumericValue movementZ = (NumericValue) map.get("movementZ");
        return new MoveLocationTargetAction(actions, movementX, movementY, movementZ);
    }

    @Override
    protected ActionResult execute(Target target, Source source) {
        if (actions.stream().anyMatch(action -> action.executeAction(new LocationTarget(target.getLocation().add(movementX.getRealValue(target, source),
                movementY.getRealValue(target, source), movementZ.getRealValue(target, source))), source).equals(ActionResult.CANCELLED))) {
            return ActionResult.CANCELLED;
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(LocationTarget.class, EntityTarget.class);
    }

    @Override
    public Action clone() {
        return new MoveLocationTargetAction(actions.stream().map(Action::clone).collect(Collectors.toList()), movementX.clone(),
                movementY.clone(), movementZ.clone());
    }
}
