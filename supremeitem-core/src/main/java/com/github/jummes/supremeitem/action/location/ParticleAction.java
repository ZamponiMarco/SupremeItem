package com.github.jummes.supremeitem.action.location;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.model.util.particle.options.ParticleOptions;
import com.github.jummes.libs.util.MapperUtils;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.NumericValue;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Getter
@Setter
@Enumerable.Displayable(name = "&c&lParticle", description = "gui.action.location.particle.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDQ2MWQ5ZDA2YzBiZjRhN2FmNGIxNmZkMTI4MzFlMmJlMGNmNDJlNmU1NWU5YzBkMzExYTJhODk2NWEyM2IzNCJ9fX0=")
@Enumerable.Child
public class ParticleAction extends LocationAction {

    private static final NumericValue COUNT_DEFAULT = new NumericValue(1);
    private static final NumericValue OFFSET_DEFAULT = new NumericValue(0);
    private static final NumericValue SPEED_DEFAULT = new NumericValue(0);
    private static final boolean FORCE_DEFAULT = false;

    private static final String TYPE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWY4NDczNWZjOWM3NjBlOTVlYWYxMGNlYzRmMTBlZGI1ZjM4MjJhNWZmOTU1MWVlYjUwOTUxMzVkMWZmYTMwMiJ9fX0=";
    private static final String COUNT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";
    private static final String OFFSET_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDI4OTIyMTdjZThmYTg0MTI4YWJlMjY0YjVlNzFkN2VlN2U2YTlkNTgyMzgyNThlZjdkMmVmZGMzNDcifX19";
    private static final String SPEED_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNmYzUyMjY0ZDhhZDllNjU0ZjQxNWJlZjAxYTIzOTQ3ZWRiY2NjY2Y2NDkzNzMyODliZWE0ZDE0OTU0MWY3MCJ9fX0=";
    private static final String FORCE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkMWFiYTczZjYzOWY0YmM0MmJkNDgxOTZjNzE1MTk3YmUyNzEyYzNiOTYyYzk3ZWJmOWU5ZWQ4ZWZhMDI1In19fQ==";

    @Serializable(headTexture = TYPE_HEAD, stringValue = true, description = "gui.action.location.particle.type", fromListMapper = "particlesMapper", fromList = "getParticles")
    private Particle type;

    @Serializable(headTexture = COUNT_HEAD, description = "gui.action.location.particle.count", additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Number(minValue = 0, scale = 1)
    @Serializable.Optional(defaultValue = "COUNT_DEFAULT")
    private NumericValue count;

    @Serializable(headTexture = OFFSET_HEAD, description = "gui.action.location.particle.offset", additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Number(minValue = 0)
    @Serializable.Optional(defaultValue = "OFFSET_DEFAULT")
    private NumericValue offset;

    @Serializable(headTexture = SPEED_HEAD, description = "gui.action.location.particle.speed", additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Number(minValue = 0)
    @Serializable.Optional(defaultValue = "SPEED_DEFAULT")
    private NumericValue speed;

    @Serializable(headTexture = FORCE_HEAD, description = "gui.action.location.particle.force")
    @Serializable.Optional(defaultValue = "FORCE_DEFAULT")
    private boolean force;

    @Serializable(displayItem = "getDataObject", description = "gui.action.location.particle.data")
    private ParticleOptions data;

    public ParticleAction() {
        this(TARGET_DEFAULT, Particle.FIREWORKS_SPARK, COUNT_DEFAULT.clone(), OFFSET_DEFAULT.clone(),
                SPEED_DEFAULT.clone(), FORCE_DEFAULT, null);
    }

    public ParticleAction(boolean target, Particle type, NumericValue count, NumericValue offset, NumericValue speed,
                          boolean force, ParticleOptions data) {
        super(target);
        this.type = type;
        this.count = count;
        this.offset = offset;
        this.speed = speed;
        this.force = force;
        this.data = data;
    }

    public ParticleAction(Map<String, Object> map) {
        super(map);
        this.type = Particle.valueOf((String) map.getOrDefault("type", "FIREWORKS_SPARK"));
        this.force = (boolean) map.getOrDefault("force", FORCE_DEFAULT);
        this.count = (NumericValue) map.getOrDefault("count", COUNT_DEFAULT.clone());
        this.offset = (NumericValue) map.getOrDefault("offset", OFFSET_DEFAULT.clone());
        this.speed = (NumericValue) map.getOrDefault("speed", SPEED_DEFAULT.clone());
        this.data = (ParticleOptions) map.get("data");
    }

    public static List<Object> getParticles(ModelPath<?> path) {
        return MapperUtils.getParticles();
    }

    public static Function<Object, ItemStack> particlesMapper() {
        return MapperUtils.particlesMapper();
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        int count = this.count.getRealValue(target, source).intValue();
        double offset = this.offset.getRealValue(target, source);
        double speed = this.speed.getRealValue(target, source);
        Location l = getLocation(target, source, true).clone();
        World world = l.getWorld();
        if (world != null) {
            world.spawnParticle(type, l, count, offset, offset, offset, speed, data == null ? null : data.buildData(), force);
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAILURE;
    }

    @Override
    public Action clone() {
        return new ParticleAction(target, type, count.clone(), offset.clone(), speed.clone(), force, data == null ? null : data.clone());
    }

    @Override
    public String getName() {
        return "&6&lParticle: &c" + WordUtils.capitalize(type.toString());
    }

    public ItemStack getDataObject() {
        if (ParticleOptions.getParticleOptionsMap().containsKey(type.getDataType())) {
            return new ItemStack(Material.DIAMOND);
        }
        return null;
    }

    @Override
    public void onModify(Field field) {
        if (data == null || !data.getClass().equals(ParticleOptions.getParticleOptionsMap().get(type.getDataType()))) {
            data = ParticleOptions.buildOptions(type.getDataType());
        }
    }
}
