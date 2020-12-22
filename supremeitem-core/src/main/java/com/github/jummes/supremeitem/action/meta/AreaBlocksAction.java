package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.area.Area;
import com.github.jummes.supremeitem.area.SphericArea;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lApply actions to blocks in Area", description = "gui.action.area-blocks.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZjZDQxNGIwNWE1MzJjNjA5YzJhYTQ4ZDZjMDYyYzI5MmQ1MzNkZmFmNGQ3MzJhYmU5YWY1NzQxNTg5ZSJ9fX0=")
public class AreaBlocksAction extends MetaAction {

    private static final String ACTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";
    private static final String SHAPE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2IyYjVkNDhlNTc1Nzc1NjNhY2EzMTczNTUxOWNiNjIyMjE5YmMwNThiMWYzNDY0OGI2N2I4ZTcxYmMwZmEifX19";

    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.action.area-blocks.actions")
    private List<Action> actions;
    @Serializable(headTexture = SHAPE_HEAD, description = "gui.action.area-blocks.area", additionalDescription = {"gui.additional-tooltips.recreate"})
    private Area area;

    public AreaBlocksAction() {
        this(TARGET_DEFAULT, Lists.newArrayList(), new SphericArea());
    }

    public AreaBlocksAction(boolean target, List<Action> actions, Area area) {
        super(target);
        this.actions = actions;
        this.area = area;
    }

    public AreaBlocksAction(Map<String, Object> map) {
        super(map);
        this.actions = (List<Action>) map.get("actions");
        this.actions.removeIf(Objects::isNull);
        this.area = (Area) map.get("area");
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        area.getBlocks(getLocation(target, source), target, source).forEach(block -> actions.forEach(action -> action.
                execute(new LocationTarget(block), source)));
        return ActionResult.SUCCESS;
    }

    private Location getLocation(Target target, Source source) {
        if (this.target) {
            return target.getLocation();
        }
        return source.getLocation();
    }

    @Override
    public Action clone() {
        return new AreaBlocksAction(target, actions, area);
    }

    @Override
    public String getName() {
        return "&6&lBlocks in area: &c" + area.getName();
    }

}
