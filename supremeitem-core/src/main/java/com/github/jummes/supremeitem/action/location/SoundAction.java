package com.github.jummes.supremeitem.action.location;


import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.NumericValue;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Getter
@Setter
@Enumerable.Displayable(name = "&c&lSound", description = "gui.action.location.sound.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWIxZTIwNDEwYmI2YzdlNjk2OGFmY2QzZWM4NTU1MjBjMzdhNDBkNTRhNTRlOGRhZmMyZTZiNmYyZjlhMTkxNSJ9fX0=")
@Enumerable.Child
public class SoundAction extends LocationAction {

    private static final NumericValue PITCH_DEFAULT = new NumericValue(1f);
    private static final NumericValue VOLUME_DEFAULT = new NumericValue(10f);

    private static final String TYPE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkxOWZiZDFkZjQ4YzMyMmMxMzA1YmIxZjhlYWI5Njc0YzIxODQ0YTA0OTNhNTUzNWQ5NGNhYmExZWNhM2MxZCJ9fX0=";
    private static final String CATEGORY_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmQzYjJlM2U5OTU0ZjgyMmI0M2ZlNWY5MTUwOTllMGE2Y2FhYTgxZjc5MTIyMmI1ODAzZDQxNDVhODUxNzAifX19";
    private static final String PITCH_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmQzYjJlM2U5OTU0ZjgyMmI0M2ZlNWY5MTUwOTllMGE2Y2FhYTgxZjc5MTIyMmI1ODAzZDQxNDVhODUxNzAifX19";
    private static final String VOLUME_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGYyMTAwNzM3NGQ0ODBkZTFkNzg1Y2E4Njc2NDE3NTY1MGUwMDc3MzU0MDQyN2FhZWYxYzBjYzE3MGRmM2ExNCJ9fX0=";

    @Serializable(headTexture = TYPE_HEAD, stringValue = true, description = "gui.action.location.sound.type", fromList = "getSounds", fromListMapper = "soundsMapper")
    private Sound type;

    @Serializable(headTexture = CATEGORY_HEAD, stringValue = true, description = "gui.action.location.sound.category", fromList = "getSoundCategories", fromListMapper = "soundCategoriesMapper")
    private SoundCategory category;

    @Serializable(headTexture = PITCH_HEAD, description = "gui.action.location.sound.pitch", additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Number(minValue = 0, maxValue = 2)
    @Serializable.Optional(defaultValue = "PITCH_DEFAULT")
    private NumericValue pitch;

    @Serializable(headTexture = VOLUME_HEAD, description = "gui.action.location.sound.volume", additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Number(minValue = 0)
    @Serializable.Optional(defaultValue = "VOLUME_DEFAULT")
    private NumericValue volume;

    public SoundAction() {
        this(TARGET_DEFAULT, Sound.BLOCK_ANVIL_BREAK, SoundCategory.MASTER, PITCH_DEFAULT.clone(), VOLUME_DEFAULT.clone());
    }

    public SoundAction(boolean target, Sound type, SoundCategory category, NumericValue pitch, NumericValue volume) {
        super(target);
        this.type = type;
        this.category = category;
        this.pitch = pitch;
        this.volume = volume;
    }

    public SoundAction(Map<String, Object> map) {
        super(map);
        this.type = Sound.valueOf((String) map.getOrDefault("type", "BLOCK_ANVIL_BREAK"));
        this.category = SoundCategory.valueOf((String) map.getOrDefault("category", "MASTER"));

        this.pitch = (NumericValue) map.getOrDefault("pitch", PITCH_DEFAULT.clone());
        this.volume = (NumericValue) map.getOrDefault("volume", VOLUME_DEFAULT.clone());
    }

    public static List<Object> getSounds(ModelPath<?> path) {
        return Lists.newArrayList(Sound.values());
    }

    public static Function<Object, ItemStack> soundsMapper() {
        return obj -> {
            Sound type = (Sound) obj;
            return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(TYPE_HEAD),
                    type.name(), new ArrayList<>());
        };
    }

    public static List<Object> getSoundCategories(ModelPath<?> path) {
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
        double volume = this.volume.getRealValue(target, source);
        double pitch = this.pitch.getRealValue(target, source);
        Location location = getLocation(target, source);
        World world = location.getWorld();
        if (world == null) {
            return ActionResult.FAILURE;
        }
        location.getWorld().playSound(location, type, category, (float) volume, (float) pitch);
        return ActionResult.SUCCESS;
    }

    @Override
    public Action clone() {
        return new SoundAction(target, type, category, pitch.clone(), volume.clone());
    }

    @Override
    public String getName() {
        return "&6&lSound: &c" + WordUtils.capitalize(type.toString());
    }

}
