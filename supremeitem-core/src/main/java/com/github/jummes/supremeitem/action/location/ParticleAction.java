package com.github.jummes.supremeitem.action.location;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.model.wrapper.ItemStackWrapper;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.NumericValue;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@Enumerable.Displayable(name = "&c&lParticle", description = "gui.action.particle.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDQ2MWQ5ZDA2YzBiZjRhN2FmNGIxNmZkMTI4MzFlMmJlMGNmNDJlNmU1NWU5YzBkMzExYTJhODk2NWEyM2IzNCJ9fX0=")
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

    @Serializable(headTexture = TYPE_HEAD, stringValue = true, description = "gui.action.particle.type", fromListMapper = "particlesMapper", fromList = "getParticles")
    private Particle type;

    @Serializable(headTexture = COUNT_HEAD, description = "gui.action.particle.count", additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Number(minValue = 0, scale = 1)
    @Serializable.Optional(defaultValue = "COUNT_DEFAULT")
    private NumericValue count;

    @Serializable(headTexture = OFFSET_HEAD, description = "gui.action.particle.offset", additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Number(minValue = 0)
    @Serializable.Optional(defaultValue = "OFFSET_DEFAULT")
    private NumericValue offset;

    @Serializable(headTexture = SPEED_HEAD, description = "gui.action.particle.speed", additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Number(minValue = 0)
    @Serializable.Optional(defaultValue = "SPEED_DEFAULT")
    private NumericValue speed;

    @Serializable(headTexture = FORCE_HEAD, description = "gui.action.particle.force")
    @Serializable.Optional(defaultValue = "FORCE_DEFAULT")
    private boolean force;

    @Serializable(displayItem = "getDataObject", description = "gui.action.particle.data")
    private ParticleOptions data;

    public ParticleAction() {
        this(TARGET_DEFAULT, Particle.FIREWORKS_SPARK, COUNT_DEFAULT.clone(), OFFSET_DEFAULT.clone(),
                SPEED_DEFAULT.clone(), FORCE_DEFAULT, null);
    }

    public ParticleAction(boolean target, Particle type, NumericValue count, NumericValue offset, NumericValue speed, boolean force, ParticleOptions data) {
        super(target);
        this.type = type;
        this.count = count;
        this.offset = offset;
        this.speed = speed;
        this.force = force;
        this.data = data;
    }

    public static ParticleAction deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        Particle type = Particle.valueOf((String) map.getOrDefault("type", "FIREWORKS_SPARK"));
        boolean force = (boolean) map.getOrDefault("force", FORCE_DEFAULT);
        ParticleOptions data = (ParticleOptions) map.get("data");

        NumericValue count;
        NumericValue offset;
        NumericValue speed;
        try {
            count = (NumericValue) map.getOrDefault("count", COUNT_DEFAULT.clone());
            offset = (NumericValue) map.getOrDefault("offset", OFFSET_DEFAULT.clone());
            speed = (NumericValue) map.getOrDefault("speed", SPEED_DEFAULT.clone());
        } catch (ClassCastException e) {
            count = new NumericValue(((Number) map.getOrDefault("count", COUNT_DEFAULT.getValue())));
            offset = new NumericValue(((Number) map.getOrDefault("offset", OFFSET_DEFAULT.getValue())));
            speed = new NumericValue(((Number) map.getOrDefault("speed", SPEED_DEFAULT.getValue())));
        }
        return new ParticleAction(target, type, count, offset, speed, force, data);
    }

    public static List<Object> getParticles(ModelPath<?> path) {
        return Arrays.stream(Particle.values()).filter(particle -> !particle.name().toLowerCase().contains("legacy")).
                collect(Collectors.toList());
    }

    public static Function<Object, ItemStack> particlesMapper() {
        return obj -> {
            Particle type = (Particle) obj;
            return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDQ2MWQ5ZDA2YzBiZjRhN2FmNGIxNmZkMTI4MzFlMmJlMGNmNDJlNmU1NWU5YzBkMzExYTJhODk2NWEyM2IzNCJ9fX0="),
                    type.name(), new ArrayList<>());
        };
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        int count = this.count.getRealValue(target, source).intValue();
        double offset = this.offset.getRealValue(target, source);
        double speed = this.speed.getRealValue(target, source);
        Location l = getLocation(target, source);
        World world = l.getWorld();
        if (world != null) {
            world.spawnParticle(type, l, count, offset, offset, offset, speed, data == null ? null : data.buildData(), force);
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAILURE;
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(LocationTarget.class, EntityTarget.class);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(TYPE_HEAD),
                "&6&lParticle: &c" + WordUtils.capitalize(type.toString()), Libs.getLocale().getList("gui.action.description"));
    }

    @Override
    public Action clone() {
        return new ParticleAction(target, type, count.clone(), offset.clone(), speed.clone(), force, data == null ? null : data.clone());
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

    public static abstract class ParticleOptions implements Model, Cloneable {

        @SneakyThrows
        protected static ParticleOptions buildOptions(Class<?> clazz) {
            Class<? extends ParticleOptions> optionsClass = getParticleOptionsMap().get(clazz);
            if (optionsClass != null) {
                return optionsClass.getConstructor().newInstance();
            }
            return null;
        }

        protected static Map<Class<?>, Class<? extends ParticleOptions>> getParticleOptionsMap() {
            Map<Class<?>, Class<? extends ParticleOptions>> map = new HashMap<>();
            map.put(Particle.DustOptions.class, DustOptionsData.class);
            map.put(ItemStack.class, ItemStackData.class);
            map.put(BlockData.class, BlockDataData.class);
            return map;
        }

        protected abstract Object buildData();

        protected abstract ParticleOptions clone();

    }

    @AllArgsConstructor
    @Enumerable.Child
    @Getter
    @Setter
    public static class DustOptionsData extends ParticleOptions {

        private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmIyYzI2NzhlOTM2NDA5ODhlNjg1YWM4OTM0N2VmYTQ1MjQxMTljOWQ4ZjcyNzhjZTAxODFiYzNlNGZiMmIwOSJ9fX0=";

        @Serializable(headTexture = HEAD)
        private String hexColor;
        @Serializable(headTexture = HEAD)
        @Serializable.Number(minValue = 0)
        private double size;

        public DustOptionsData() {
            this("ffbb00", 2.0);
        }

        public static DustOptionsData deserialize(Map<String, Object> map) {
            String hexColor = (String) map.get("hexColor");
            double size = (double) map.get("size");
            return new DustOptionsData(hexColor, size);
        }

        @Override
        protected Object buildData() {
            return new Particle.DustOptions(hex2Rgb(hexColor), (float) size);
        }

        @Override
        protected ParticleOptions clone() {
            return new DustOptionsData(hexColor, size);
        }

        public Color hex2Rgb(String colorStr) {
            return Color.fromRGB(
                    Integer.valueOf(colorStr.substring(0, 2), 16),
                    Integer.valueOf(colorStr.substring(2, 4), 16),
                    Integer.valueOf(colorStr.substring(4, 6), 16));
        }
    }

    @AllArgsConstructor
    @Enumerable.Child
    @Getter
    @Setter
    public static class ItemStackData extends ParticleOptions {

        private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTJiMzViZGE1ZWJkZjEzNWY0ZTcxY2U0OTcyNmZiZWM1NzM5ZjBhZGVkZjAxYzUxOWUyYWVhN2Y1MTk1MWVhMiJ9fX0=";

        @Serializable(headTexture = HEAD, stringValue = true, fromListMapper = "materialListMapper", fromList = "materialList")
        private Material item;

        public ItemStackData() {
            this(Material.IRON_AXE);
        }

        public static ItemStackData deserialize(Map<String, Object> map) {
            Material item;
            try {
                item = Material.valueOf((String) map.get("item"));
            } catch (Exception e) {
                item = ((ItemStackWrapper) map.get("item")).getWrapped().getType();
            }
            return new ItemStackData(item);
        }

        public static List<Object> materialList(ModelPath<?> path) {
            return ItemUtils.getMaterialList();
        }

        public static Function<Object, ItemStack> materialListMapper() {
            return ItemUtils.getMaterialMapper();
        }

        @Override
        protected Object buildData() {
            return new ItemStack(item);
        }

        @Override
        protected ParticleOptions clone() {
            return new ItemStackData(item);
        }
    }

    @AllArgsConstructor
    @Enumerable.Child
    @Getter
    @Setter
    public static class BlockDataData extends ParticleOptions {

        private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTU0ODE4MjMzYzgxMTg3M2U4NWY1YTRlYTQ0MjliNzVmMjNiNmFlMGVhNmY1ZmMwZjdiYjQyMGQ3YzQ3MSJ9fX0=";

        @Serializable(headTexture = HEAD, stringValue = true, fromListMapper = "materialListMapper", fromList = "materialList")
        private Material material;

        public BlockDataData() {
            this(Material.STONE);
        }

        public static BlockDataData deserialize(Map<String, Object> map) {
            Material material = Material.valueOf((String) map.get("material"));
            return new BlockDataData(material);
        }

        public static List<Object> materialList(ModelPath<?> path) {
            return ItemUtils.getMaterialList().stream().filter(m -> ((Material) m).isBlock()).collect(Collectors.toList());
        }

        public static Function<Object, ItemStack> materialListMapper() {
            return ItemUtils.getMaterialMapper();
        }

        @Override
        protected Object buildData() {
            return Bukkit.createBlockData(material);
        }

        @Override
        protected ParticleOptions clone() {
            return new BlockDataData(material);
        }
    }
}
