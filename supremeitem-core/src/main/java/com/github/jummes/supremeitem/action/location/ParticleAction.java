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
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Enumerable.Displayable(name = "&c&lParticle", description = "gui.action.particle.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDQ2MWQ5ZDA2YzBiZjRhN2FmNGIxNmZkMTI4MzFlMmJlMGNmNDJlNmU1NWU5YzBkMzExYTJhODk2NWEyM2IzNCJ9fX0=")
@Enumerable.Child
public class ParticleAction extends Action {

    private static final String TYPE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWY4NDczNWZjOWM3NjBlOTVlYWYxMGNlYzRmMTBlZGI1ZjM4MjJhNWZmOTU1MWVlYjUwOTUxMzVkMWZmYTMwMiJ9fX0=";
    private static final String COUNT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";
    private static final String OFFSET_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDI4OTIyMTdjZThmYTg0MTI4YWJlMjY0YjVlNzFkN2VlN2U2YTlkNTgyMzgyNThlZjdkMmVmZGMzNDcifX19";
    private static final String SPEED_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNmYzUyMjY0ZDhhZDllNjU0ZjQxNWJlZjAxYTIzOTQ3ZWRiY2NjY2Y2NDkzNzMyODliZWE0ZDE0OTU0MWY3MCJ9fX0=";
    private static final String FORCE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkMWFiYTczZjYzOWY0YmM0MmJkNDgxOTZjNzE1MTk3YmUyNzEyYzNiOTYyYzk3ZWJmOWU5ZWQ4ZWZhMDI1In19fQ==";


    @Serializable(headTexture = TYPE_HEAD, stringValue = true, description = "gui.action.particle.type", fromListMapper = "particlesMapper", fromList = "getParticles")
    private Particle type;
    @Serializable(headTexture = COUNT_HEAD, description = "gui.action.particle.count")
    @Serializable.Number(minValue = 0)
    private int count;
    @Serializable(headTexture = OFFSET_HEAD, description = "gui.action.particle.offset")
    @Serializable.Number(minValue = 0)
    private double offset;
    @Serializable(headTexture = SPEED_HEAD, description = "gui.action.particle.speed")
    @Serializable.Number(minValue = 0)
    private double speed;
    @Serializable(headTexture = FORCE_HEAD, description = "gui.action.particle.force")
    private boolean force;
    @Serializable(displayItem = "getDataObject", description = "gui.action.particle.data")
    private ParticleOptions data;

    public ParticleAction() {
        this(Particle.FIREWORKS_SPARK, 1, 0, 0, false, null);
    }

    public static ParticleAction deserialize(Map<String, Object> map) {
        Particle type = Particle.valueOf((String) map.getOrDefault("type", "FIREWORKS_SPARK"));
        int count = (int) map.getOrDefault("count", 1);
        double offset = (double) map.getOrDefault("offset", 0);
        double speed = (double) map.getOrDefault("speed", 0);
        boolean force = (boolean) map.getOrDefault("force", false);
        ParticleOptions data = (ParticleOptions) map.getOrDefault("data", null);
        return new ParticleAction(type, count, offset, speed, force, data);
    }

    public static List<Object> getParticles(ModelPath path) {
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
        if (target instanceof LocationTarget) {
            ((LocationTarget) target).getTarget().getWorld().spawnParticle(type, ((LocationTarget) target).getTarget(),
                    count, offset, offset, offset, speed, data == null ? null : data.buildData(), force);
        } else if (target instanceof EntityTarget) {
            ((EntityTarget) target).getTarget().getWorld().spawnParticle(type, ((EntityTarget) target).getTarget().
                    getEyeLocation(), count, offset, offset, offset, speed, data == null ? null : data.buildData(), force);
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
                "&6&lParticle: &c" + WordUtils.capitalize(type.toString()), Libs.getLocale().getList("gui.action.description"));
    }

    public ItemStack getDataObject() {
        if (ParticleOptions.getParticleOptionsMap().containsKey(type.getDataType())) {
            return new ItemStack(Material.DIAMOND);
        }
        return null;
    }

    @Override
    public void onModify() {
        if (data == null || !data.getClass().equals(ParticleOptions.getParticleOptionsMap().get(type.getDataType()))) {
            data = ParticleOptions.buildOptions(type.getDataType());
        }
    }

    public static abstract class ParticleOptions implements Model {

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

    }

    @AllArgsConstructor
    @Enumerable.Child
    public static class DustOptionsData extends ParticleOptions {

        private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

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

        public Color hex2Rgb(String colorStr) {
            return Color.fromRGB(
                    Integer.valueOf(colorStr.substring(0, 2), 16),
                    Integer.valueOf(colorStr.substring(2, 4), 16),
                    Integer.valueOf(colorStr.substring(4, 6), 16));
        }
    }

    @AllArgsConstructor
    @Enumerable.Child
    public static class ItemStackData extends ParticleOptions {

        private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

        @Serializable(headTexture = HEAD)
        private ItemStackWrapper item;

        public ItemStackData() {
            this(new ItemStackWrapper());
        }

        public static ItemStackData deserialize(Map<String, Object> map) {
            ItemStackWrapper item = (ItemStackWrapper) map.get("item");
            return new ItemStackData(item);
        }

        @Override
        protected Object buildData() {
            return item.getWrapped();
        }
    }

    @AllArgsConstructor
    @Enumerable.Child
    public static class BlockDataData extends ParticleOptions {

        private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

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
            return Arrays.stream(Material.values()).filter(m -> !m.name().toLowerCase().contains("legacy") && m.isBlock() && m.isItem()).collect(Collectors.toList());
        }

        public static Function<Object, ItemStack> materialListMapper() {
            return obj -> {
                Material m = (Material) obj;
                return new ItemStack(m);
            };
        }

        @Override
        protected Object buildData() {
            return Bukkit.createBlockData(material);
        }
    }
}
