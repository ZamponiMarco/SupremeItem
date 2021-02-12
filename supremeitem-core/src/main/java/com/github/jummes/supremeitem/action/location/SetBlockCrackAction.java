package com.github.jummes.supremeitem.action.location;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.entity.selector.EntitySelector;
import com.github.jummes.supremeitem.value.NumericValue;
import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Enumerable.Child
@Enumerable.Displayable(condition = "protocolLibEnabled", name = "&c&lCrack Block Action", description = "gui.action.location.crack.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzlhNDZiMmFiMzJmMjE2ZTJkOTIyYzcyMzdiYTIzMTlmOTFiNzFmYTI0ZmU0NTFhZDJjYTgxNDIzZWEzYzgifX19")
public class SetBlockCrackAction extends PacketAction {

    private static final String CRACK_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzlhNDZiMmFiMzJmMjE2ZTJkOTIyYzcyMzdiYTIzMTlmOTFiNzFmYTI0ZmU0NTFhZDJjYTgxNDIzZWEzYzgifX19";

    @Serializable(headTexture = CRACK_HEAD, description = "gui.action.location.crack.stage",
            additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Number(minValue = 0, maxValue = 9, scale = 1)
    private NumericValue destroyStage;

    public SetBlockCrackAction() {
        this(TARGET_DEFAULT, Lists.newArrayList(), new NumericValue(0));
    }

    public SetBlockCrackAction(boolean target, List<EntitySelector> selectors, NumericValue destroyStage) {
        super(target, selectors);
        this.destroyStage = destroyStage;
    }

    public SetBlockCrackAction(Map<String, Object> map) {
        super(map);
        this.destroyStage = (NumericValue) map.getOrDefault("destroyStage", new NumericValue(0));
    }

    public static boolean protocolLibEnabled(ModelPath path) {
        return SupremeItem.getInstance().getProtocolLibHook().isEnabled();
    }

    @Override
    public ActionResult execute(Target target, Source source, Map<String, Object> map) {
        Location location = getLocation(target, source);

        if (location == null || !SupremeItem.getInstance().getProtocolLibHook().isEnabled()) {
            return ActionResult.FAILURE;
        }

        if (source.getCaster() instanceof Player &&
                !SupremeItem.getInstance().getWorldGuardHook().
                        canBuild((Player) source.getCaster(), location)) {
            return ActionResult.FAILURE;
        }
        selectedPlayers(location, target, source).forEach(player -> SupremeItem.getInstance().getProtocolLibHook().
                sendSetBlockCrackPacket(player, location, destroyStage.getRealValue(target, source).intValue()));
        return ActionResult.SUCCESS;
    }

    @Override
    public String getName() {
        return "&6&lSet Block Destroy Stage: &c" + destroyStage.getName();
    }

    @Override
    public Action clone() {
        return new SetBlockCrackAction(target, selectors.stream().map(EntitySelector::clone).collect(Collectors.toList()),
                destroyStage.clone());
    }

}
