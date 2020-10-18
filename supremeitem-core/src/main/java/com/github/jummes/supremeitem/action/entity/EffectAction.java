package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericValue;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
@Enumerable.Displayable(name = "&c&lEffect", description = "gui.action.effect.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzJhN2RjYmY3ZWNhNmI2ZjYzODY1OTFkMjM3OTkxY2ExYjg4OGE0ZjBjNzUzZmY5YTMyMDJjZjBlOTIyMjllMyJ9fX0=")
@Enumerable.Child
@Getter
@Setter
public class EffectAction extends Action {

    private static final NumericValue DURATION_DEFAULT = new NumericValue(20);
    private static final NumericValue LEVEL_DEFAULT = new NumericValue(0);
    private static final boolean PARTICLES_DEFAULT = true;
    private static final boolean AMBIENT_DEFAULT = true;
    private static final boolean ICON_DEFAULT = true;

    private static final String TYPE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzJhN2RjYmY3ZWNhNmI2ZjYzODY1OTFkMjM3OTkxY2ExYjg4OGE0ZjBjNzUzZmY5YTMyMDJjZjBlOTIyMjllMyJ9fX0=";
    private static final String DURATION_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE2YWU1YjM0YzRmNzlhNWY5ZWQ2Y2NjMzNiYzk4MWZjNDBhY2YyYmZjZDk1MjI2NjRmZTFjNTI0ZDJlYjAifX19";
    private static final String LEVEL_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";
    private static final String PARTICLE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWY4NDczNWZjOWM3NjBlOTVlYWYxMGNlYzRmMTBlZGI1ZjM4MjJhNWZmOTU1MWVlYjUwOTUxMzVkMWZmYTMwMiJ9fX0=";
    private static final String AMBIENT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWY1NzE5MmIxOTRjNjU4YWFhODg4MTY4NDhjYmNlN2M3NDk0NjZhNzkyYjhhN2UxZDNmYWZhNDFjNDRmMzQxMiJ9fX0=";
    private static final String ICON_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDU5MWYwNGJiNmQ1MjQ5MTFhZGRhNzc1YWYyNDRmODZhOTVkYjE4N2UzMWI4YTNiMTAzYWQ4MGZjNWIyMjU2MCJ9fX0=";

    @Serializable(headTexture = TYPE_HEAD, stringValue = true, description = "gui.action.effect.type", fromList = "getPotionEffects", fromListMapper = "potionEffectsMapper")
    private PotionEffectType type;

    @Serializable(headTexture = DURATION_HEAD, description = "gui.action.effect.duration")
    @Serializable.Number(minValue = 0, scale = 1)
    @Serializable.Optional(defaultValue = "DURATION_DEFAULT")
    private NumericValue duration;

    @Serializable(headTexture = LEVEL_HEAD, description = "gui.action.effect.level")
    @Serializable.Number(minValue = 0, scale = 1)
    @Serializable.Optional(defaultValue = "LEVEL_DEFAULT")
    private NumericValue level;

    @Serializable(headTexture = PARTICLE_HEAD, description = "gui.action.effect.particles")
    @Serializable.Optional(defaultValue = "PARTICLES_DEFAULT")
    private boolean particles;

    @Serializable(headTexture = AMBIENT_HEAD, description = "gui.action.effect.ambient")
    @Serializable.Optional(defaultValue = "AMBIENT_DEFAULT")
    private boolean ambient;

    @Serializable(headTexture = ICON_HEAD, description = "gui.action.effect.icon")
    @Serializable.Optional(defaultValue = "ICON_DEFAULT")
    private boolean icon;

    public EffectAction() {
        this(PotionEffectType.INCREASE_DAMAGE, DURATION_DEFAULT.clone(), LEVEL_DEFAULT.clone(),
                PARTICLES_DEFAULT, AMBIENT_DEFAULT, ICON_DEFAULT);
    }

    public static EffectAction deserialize(Map<String, Object> map) {
        PotionEffectType type = PotionEffectType.getByName(((String) map.getOrDefault("type", "INCREASE_DAMAGE"))
                .replaceAll("[\\[\\]\\d, ]|PotionEffectType", ""));
        boolean particles = (boolean) map.getOrDefault("particles", PARTICLES_DEFAULT);
        boolean ambient = (boolean) map.getOrDefault("ambient", AMBIENT_DEFAULT);
        boolean icon = (boolean) map.getOrDefault("icon", ICON_DEFAULT);
        NumericValue duration;
        NumericValue level;
        try {
            duration = (NumericValue) map.getOrDefault("duration", DURATION_DEFAULT.clone());
            level = (NumericValue) map.getOrDefault("level", LEVEL_DEFAULT.clone());
        } catch (ClassCastException e) {
            duration = new NumericValue(((Number) map.getOrDefault("duration", DURATION_DEFAULT.getValue())));
            level = new NumericValue(((Number) map.getOrDefault("level", LEVEL_DEFAULT.getValue())));
        }
        return new EffectAction(type, duration, level, particles, ambient, icon);
    }

    public static List<Object> getPotionEffects(ModelPath<?> path) {
        return Lists.newArrayList(PotionEffectType.values());
    }

    public static Function<Object, ItemStack> potionEffectsMapper() {
        return obj -> {
            PotionEffectType type = (PotionEffectType) obj;
            return ItemUtils.getNamedItem(new ItemStack(Material.POTION), type.getName(), new ArrayList<>());
        };
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        ((EntityTarget) target).getTarget().addPotionEffect(new PotionEffect(type,
                (int) duration.getRealValue(target, source), (int) level.getRealValue(target, source), ambient,
                particles, icon));
        return ActionResult.SUCCESS;
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(EntityTarget.class);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(TYPE_HEAD),
                "&6&lEffect: &c" + WordUtils.capitalize(type.toString()), Libs.getLocale().getList("gui.action.description"));
    }

}
