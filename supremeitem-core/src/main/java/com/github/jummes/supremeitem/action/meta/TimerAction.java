package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.CustomClickable;
import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.gui.PluginInventoryHolder;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.savedskill.SavedSkill;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lExecute actions on a timer", description = "gui.action.timer.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGJjYjIzMGE0MTBlOTNiN2Q0YjVjMjg5NjMxZDYxNGI5MDQ1Mzg0M2Q2ZWQwM2RhZjVlNDAxNWEyZmUxZjU2YiJ9fX0=")
@CustomClickable(customCollectionClickConsumer = "getCustomConsumer")
public class TimerAction extends MetaAction {

    private static final int NUMBER_KEY = 1;

    private static final int TIMER_DEFAULT = 5;
    private static final int REPETITIONS_DEFAULT = 10;

    private static final String ACTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";
    private static final String TIMER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0=";
    private static final String REPETITIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmM4ZGVmNjdhMTI2MjJlYWQxZGVjZDNkODkzNjQyNTdiNTMxODk2ZDg3ZTQ2OTgxMzEzMWNhMjM1YjVjNyJ9fX0=";

    @Serializable(headTexture = TIMER_HEAD, description = "gui.action.timer.timer")
    @Serializable.Number(minValue = 0)
    @Serializable.Optional(defaultValue = "TIMER_DEFAULT")
    private int timer;

    @Serializable(headTexture = REPETITIONS_HEAD, description = "gui.action.timer.repetitions")
    @Serializable.Number(minValue = 0)
    @Serializable.Optional(defaultValue = "REPETITIONS_DEFAULT")
    private int repetitions;

    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.action.timer.actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> actions;

    public TimerAction() {
        this(TARGET_DEFAULT, TIMER_DEFAULT, REPETITIONS_DEFAULT, Lists.newArrayList());
    }

    public TimerAction(boolean target, int timer, int repetitions, List<Action> actions) {
        super(target);
        this.timer = timer;
        this.repetitions = repetitions;
        this.actions = actions;
    }

    public static TimerAction deserialize(Map<String, Object> map) {
        int timer = (int) map.getOrDefault("timer", TIMER_DEFAULT);
        int repetitions = (int) map.getOrDefault("repetitions", REPETITIONS_DEFAULT);
        List<Action> actions = (List<Action>) map.getOrDefault("actions", Lists.newArrayList());
        return new TimerAction(TARGET_DEFAULT, timer, repetitions, actions);
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        BukkitRunnable runnable = new BukkitRunnable() {
            private int counter = 1;

            @Override
            public void run() {
                if (counter >= repetitions) {
                    this.cancel();
                }
                actions.forEach(action -> action.execute(target, source));
                counter++;
            }
        };
        runnable.runTaskTimer(SupremeItem.getInstance(), 0, timer);
        return ActionResult.SUCCESS;
    }

    public void getCustomConsumer(JavaPlugin plugin, PluginInventoryHolder parent, ModelPath<?> path, Field field,
                                  InventoryClickEvent e) throws IllegalAccessException {
        getExtractConsumer(plugin, parent, path, field, e, this.actions, NUMBER_KEY);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(getClass().
                getAnnotation(Enumerable.Displayable.class).headTexture()), getName(), modifiedLore(NUMBER_KEY));
    }

    @Override
    public Action clone() {
        return new TimerAction(TARGET_DEFAULT, timer, repetitions, actions.stream().map(Action::clone).collect(Collectors.toList()));
    }

    @Override
    public ItemStack targetItem() {
        return null;
    }

    @Override
    public String getName() {
        return "&6&lInterval: &c" + timer + " &6&lRepetitions: &c" + repetitions;
    }

    @Override
    public Set<SavedSkill> getUsedSavedSkills() {
        Set<SavedSkill> skills = new HashSet<>();
        SavedSkill.addSkillsFromActionsList(skills, actions);
        return skills;
    }
}
