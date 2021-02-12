package com.github.jummes.supremeitem.action.location;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.entity.selector.EntitySelector;
import com.github.jummes.supremeitem.value.NumericValue;
import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Enumerable.Child
@Enumerable.Displayable(condition = "protocolLibEnabled", name = "&c&lHighlight Block", description = "gui.action.location.highlight.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZjNmVjM2I3NTM1NGI0OTIyMmE4OWM2NjNjNGFjYWQ1MjY0ZmI5NzdjYWUyNmYwYjU0ODNhNTk5YzQ2NCJ9fX0=")
public class HighlightBlockAction extends PacketAction {

    private static final String COLOR_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmVjNTk2M2UxZjc4ZjJmMDU5NDNmNGRkMzIyMjQ2NjEzNzRjMjIwZWNmZGUxZTU0NzU0ZjVlZTFlNTU1ZGQifX19";
    private static final String TICKS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0=";

    @Serializable(headTexture = COLOR_HEAD, description = "gui.action.location.highlight.color", fromList = "getColors",
            fromListMapper = "colorsMapper")
    private ChatColor color;
    @Serializable(headTexture = TICKS_HEAD, description = "gui.action.location.highlight.ticks",
            additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Number(minValue = 0, scale = 1)
    private NumericValue ticks;

    public HighlightBlockAction() {
        this(TARGET_DEFAULT, Lists.newArrayList(), ChatColor.WHITE, new NumericValue(100));
    }

    public HighlightBlockAction(boolean target, List<EntitySelector> selectors, ChatColor color, NumericValue ticks) {
        super(target, selectors);
        this.color = color;
        this.ticks = ticks;
    }

    public HighlightBlockAction(Map<String, Object> map) {
        super(map);
        this.color = ChatColor.values()[(int) map.getOrDefault("color", 0)];
        this.ticks = (NumericValue) map.getOrDefault("ticks", new NumericValue(100));
    }

    public static boolean protocolLibEnabled(ModelPath path) {
        return SupremeItem.getInstance().getProtocolLibHook().isEnabled();
    }

    public static List<Object> getColors(ModelPath<?> path) {
        return Arrays.stream(ChatColor.values()).filter(ChatColor::isColor).collect(Collectors.toList());
    }

    public static Function<Object, ItemStack> colorsMapper() {
        return obj -> ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(COLOR_HEAD), ((ChatColor) obj).name(),
                Lists.newArrayList());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("ticks", ticks);
        map.put("color", color.ordinal());
        return map;
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
                sendHighlightBlockPacket(player, location, color, ticks.getRealValue(target, source).intValue()));
        return ActionResult.SUCCESS;
    }

    @Override
    public String getName() {
        return "&c&lHighlight block";
    }

    @Override
    public Action clone() {
        return new HighlightBlockAction(target, selectors.stream().map(EntitySelector::clone).collect(Collectors.toList()),
                color, ticks.clone());
    }
}
