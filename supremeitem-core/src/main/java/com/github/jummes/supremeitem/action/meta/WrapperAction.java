package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.savedskill.SavedSkill;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Sets;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Enumerable.Parent(classArray = {DelayedAction.class, TimerAction.class, ConditionAction.class, AreaBlocksAction.class,
        AreaEntitiesAction.class, MoveLocationTargetAction.class, RepeatUntilAction.class, ChangeSourceAction.class})
@Enumerable.Displayable(name = "&9&lAction &6» &cWrapper", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTYzMzBhNGEyMmZmNTU4NzFmYzhjNjE4ZTQyMWEzNzczM2FjMWRjYWI5YzhlMWE0YmI3M2FlNjQ1YTRhNGUifX19")
public abstract class WrapperAction extends MetaAction {

    // From 0 to 6
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
        if (WRAPPERS_MAP.containsKey(getClass())) {
            int i = WRAPPERS_MAP.get(getClass());
            lore.set(i + 3, MessageUtils.color(String.format("&6&l- [%d] &eto unwrap actions.", i + 1)));
        }
        return lore;
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(getClass().
                getAnnotation(Enumerable.Displayable.class).headTexture()), getName(), modifiedLore());
    }

    @Override
    public void changeSkillName(String oldName, String newName) {
        getWrappedActions().forEach(action -> action.changeSkillName(oldName, newName));
    }

    @Override
    public Set<SavedSkill> getUsedSavedSkills() {
        return getWrappedActions().stream().reduce(Sets.newHashSet(), (list, action) -> {
            list.addAll(action.getUsedSavedSkills());
            return list;
        }, (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        });
    }
}
