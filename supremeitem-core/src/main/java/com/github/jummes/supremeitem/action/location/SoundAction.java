package com.github.jummes.supremeitem.action.location;


import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
@Enumerable.Displayable(name = "&c&lSound", description = "gui.action.sound.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWIxZTIwNDEwYmI2YzdlNjk2OGFmY2QzZWM4NTU1MjBjMzdhNDBkNTRhNTRlOGRhZmMyZTZiNmYyZjlhMTkxNSJ9fX0=")
@Enumerable.Child
public class SoundAction extends Action {

    private static final String TYPE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkxOWZiZDFkZjQ4YzMyMmMxMzA1YmIxZjhlYWI5Njc0YzIxODQ0YTA0OTNhNTUzNWQ5NGNhYmExZWNhM2MxZCJ9fX0=";
    private static final String CATEGORY_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmQzYjJlM2U5OTU0ZjgyMmI0M2ZlNWY5MTUwOTllMGE2Y2FhYTgxZjc5MTIyMmI1ODAzZDQxNDVhODUxNzAifX19";
    private static final String PITCH_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmQzYjJlM2U5OTU0ZjgyMmI0M2ZlNWY5MTUwOTllMGE2Y2FhYTgxZjc5MTIyMmI1ODAzZDQxNDVhODUxNzAifX19";
    private static final String VOLUME_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGYyMTAwNzM3NGQ0ODBkZTFkNzg1Y2E4Njc2NDE3NTY1MGUwMDc3MzU0MDQyN2FhZWYxYzBjYzE3MGRmM2ExNCJ9fX0=";

    @Serializable(headTexture = TYPE_HEAD, stringValue = true, description = "gui.action.sound.type", fromList = "getSounds", fromListMapper = "soundsMapper")
    private Sound type;
    @Serializable(headTexture = CATEGORY_HEAD, stringValue = true, description = "gui.action.sound.category", fromList = "getSoundCategories", fromListMapper = "soundCategoriesMapper")
    private SoundCategory category;
    @Serializable(headTexture = PITCH_HEAD, description = "gui.action.sound.pitch")
    @Serializable.Number(minValue = 0, maxValue = 2)
    private double pitch;
    @Serializable(headTexture = VOLUME_HEAD, description = "gui.action.sound.volume")
    @Serializable.Number(minValue = 0)
    private double volume;

    public SoundAction() {
        this(Sound.BLOCK_ANVIL_BREAK, SoundCategory.MASTER, 1f, 10f);
    }

    public static SoundAction deserialize(Map<String, Object> map) {
        Sound type = Sound.valueOf((String) map.getOrDefault("type", "BLOCK_ANVIL_BREAK"));
        SoundCategory category = SoundCategory.valueOf((String) map.getOrDefault("category", "MASTER"));
        double pitch = (double) map.getOrDefault("pitch", 1f);
        double volume = (double) map.getOrDefault("volume", 10f);
        return new SoundAction(type, category, pitch, volume);
    }

    public static List<Object> getSounds(ModelPath path) {
        return Lists.newArrayList(Sound.values());
    }

    public static Function<Object, ItemStack> soundsMapper() {
        return obj -> {
            Sound type = (Sound) obj;
            return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(TYPE_HEAD),
                    type.name(), new ArrayList<>());
        };
    }

    public static List<Object> getSoundCategories(ModelPath path) {
        return Lists.newArrayList(SoundCategory.values());
    }

    public static Function<Object, ItemStack> soundCategoriesMapper() {
        return obj -> {
            SoundCategory type = (SoundCategory) obj;
            return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2EzMjA4OGU4NGM4ODExOWYwZTBkMmM5ZmZhMzlkYmVmZGZlOWQzYTYyM2MzNGVkMjkwZTFmNWEzZWMyODMzZiJ9fX0="),
                    type.name(), new ArrayList<>());
        };
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        if (target instanceof LocationTarget) {
            ((LocationTarget) target).getTarget().getWorld().playSound(((LocationTarget) target).getTarget(), type,
                    category, (float) volume, (float) pitch);
        } else if (target instanceof EntityTarget) {
            ((EntityTarget) target).getTarget().getWorld().playSound(((EntityTarget) target).getTarget().getEyeLocation(),
                    type, category, (float) volume, (float) pitch);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(LocationTarget.class, EntityTarget.class);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(TYPE_HEAD),
                "&6&lSound: &c" + WordUtils.capitalize(type.toString()), Libs.getLocale().getList("gui.action.description"));
    }

}
