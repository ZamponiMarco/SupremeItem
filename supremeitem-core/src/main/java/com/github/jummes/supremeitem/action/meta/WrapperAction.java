package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.supremeitem.action.Action;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public abstract class WrapperAction extends MetaAction {

    public static BiMap<Class<? extends WrapperAction>, Integer> WRAPPERS_MAP = ImmutableBiMap.
            <Class<? extends WrapperAction>, Integer>builder().put(DelayedAction.class, 0).put(TimerAction.class, 1).
            put(ConditionAction.class, 2).build();

    public WrapperAction(boolean target) {
        super(target);
    }

    public WrapperAction(Map<String, Object> map) {
        super(map);
    }

    public abstract List<Action> getWrappedActions();

    protected List<String> modifiedLore() {
        List<String> lore = Libs.getLocale().getList("gui.action.description");
        int i = WRAPPERS_MAP.get(getClass());
        lore.set(i + 3, MessageUtils.color(String.format("&6&l- [%d] &eto unwrap actions.", i + 1)));
        return lore;
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(getClass().
                getAnnotation(Enumerable.Displayable.class).headTexture()), getName(), modifiedLore());
    }
}
