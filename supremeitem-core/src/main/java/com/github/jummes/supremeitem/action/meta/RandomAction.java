package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.entity.DamageAction;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.NumericValue;
import com.google.common.collect.Lists;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lRandom Action", description = "gui.action.meta.random.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDFhMmMwODg2MzdmZWU5YWUzYTM2ZGQ0OTZlODc2ZTY1N2Y1MDlkZTU1OTcyZGQxN2MxODc2N2VhZTFmM2U5In19fQ==")
public class RandomAction extends MetaAction {

    private static final NumericValue VALUE_DEFAULT = new NumericValue(1);

    private static final String ACTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";
    private static final String ROLLS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.action.meta.random.actions")
    private List<RandomActionEntry> actions;
    @Serializable(headTexture = ROLLS_HEAD, description = "gui.action.meta.random.rolls",
            additionalDescription = "gui.additional-tooltips.value")
    @Serializable.Number(minValue = 1, scale = 1)
    private NumericValue rolls;


    public RandomAction() {
        this(TARGET_DEFAULT, Lists.newArrayList(), VALUE_DEFAULT.clone());
    }

    public RandomAction(boolean target, List<RandomActionEntry> actions, NumericValue rolls) {
        super(target);
        this.actions = actions;
        this.rolls = rolls;
    }

    public RandomAction(Map<String, Object> map) {
        super(map);
        this.actions = (List<RandomActionEntry>) map.get("actions");
        this.rolls = (NumericValue) map.get("rolls");
    }

    @Override
    public ActionResult execute(Target target, Source source, Map<String, Object> map) {
        TreeMap<Integer, Action> utilityMap = new TreeMap<>();
        AtomicInteger integer = new AtomicInteger(0);
        actions.forEach(action -> utilityMap.put(integer.getAndAccumulate(action.weight.getRealValue(target, source).
                intValue(), Integer::sum), action.action));
        int rolls = this.rolls.getRealValue(target, source).intValue();
        Random random = new Random();
        int number = random.nextInt(integer.get());
        IntStream.range(0, rolls).forEach(i ->
                utilityMap.floorEntry(number).getValue().execute(target, source, map));
        return ActionResult.SUCCESS;
    }

    @Override
    public String getName() {
        return "&6&lRandom action";
    }

    @Override
    public ItemStack targetItem() {
        return null;
    }

    @Override
    public Action clone() {
        return new RandomAction(target, actions.stream().map(RandomActionEntry::clone).collect(Collectors.toList()), rolls.clone());
    }

    public static class RandomActionEntry implements Model, Cloneable {

        private static final String WEIGHT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQ4NjEyMjExYmFhNzUyNjY3NGVmODEwYWYzNDFjNDhmZTFhOTAzM2FiYjg1NzI2MzlkYTIxNTc4NDJlM2U0MiJ9fX0=";

        @Serializable(displayItem = "getActionItem", description = "gui.action.meta.random.action",
                additionalDescription = "gui.additional-tooltips.recreate")
        private Action action;
        @Serializable(headTexture = WEIGHT_HEAD, description = "gui.action.meta.random.weight",
                additionalDescription = "gui.additional-tooltips.value")
        @Serializable.Number(minValue = 1, scale = 1)
        private NumericValue weight;

        public RandomActionEntry() {
            this(new DamageAction(), VALUE_DEFAULT.clone());
        }

        public RandomActionEntry(Action action, NumericValue weight) {
            this.action = action;
            this.weight = weight;
        }

        public RandomActionEntry(Map<String, Object> map) {
            this.action = (Action) map.get("action");
            this.weight = (NumericValue) map.get("weight");
        }

        @Override
        public RandomActionEntry clone() {
            return new RandomActionEntry(action.clone(), weight.clone());
        }

        @Override
        public ItemStack getGUIItem() {
            List<String> lore = Libs.getLocale().getList("gui.action.meta.random.entry").stream().map(MessageUtils::color).
                    collect(Collectors.toList());
            lore.add(0, MessageUtils.color("&6&lAction &c» " + action.getName()));
            return ItemUtils.getNamedItem(action.getGUIItem(), "&6&lPool Entry &c» &6&lweight: &c" + weight.getName(),
                    lore);
        }

        public ItemStack getActionItem() {
            return action.getGUIItem();
        }
    }
}
