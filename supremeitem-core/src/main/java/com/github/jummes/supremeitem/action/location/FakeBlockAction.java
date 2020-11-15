package com.github.jummes.supremeitem.action.location;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.MaterialValue;
import com.github.jummes.supremeitem.value.NumericValue;
import com.google.common.collect.Sets;
import lombok.Setter;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Enumerable.Child
@Setter
@Enumerable.Displayable(name = "&c&lFake block", description = "gui.action.fake-block.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYThjODU2MzY2YzY0Nzc0YWY2MjJkZjkwY2ViMTNjYzkxNjcyNzk0ZTc0OWE2MmJkMDFjYjg3MmRhNzE2ZCJ9fX0=")
public class FakeBlockAction extends LocationAction {

    private static final boolean ALLOW_MATERIALS_DEFAULT = false;

    private static final String MATERIAL_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2ZmOTgxN2Q3NjdkMmVkZTcxODFhMDU3YWEyNmYwOGY3ZWNmNDY1MWRlYzk3ZGU1YjU0ZWVkZTFkZDJiNDJjNyJ9fX0";
    private static final String EXCLUDED_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWZkMjQwMDAwMmFkOWZiYmJkMDA2Njk0MWViNWIxYTM4NGFiOWIwZTQ4YTE3OGVlOTZlNGQxMjlhNTIwODY1NCJ9fX0=";
    private static final String ALLOW_MATERIALS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2U2MTdiZWQ4ZTk3ZDQwODc5OTNlYzBjODk4Zjg3NzJjNDUyYjk5ZDhiNGI5YTQ1ZTNlYzQzNDkzMDQwOWVlOSJ9fX0";
    private static final String TICKS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0=";
    @Serializable(headTexture = ALLOW_MATERIALS_HEAD, description = "gui.action.fake-block.allow-materials")
    @Serializable.Optional(defaultValue = "NEGATE_DEFAULT")
    protected boolean allowMaterials;
    @Serializable(headTexture = MATERIAL_HEAD, stringValue = true, description = "gui.action.fake-block.material",
            additionalDescription = {"gui.additional-tooltips.value"})
    private MaterialValue material;
    @Serializable(headTexture = EXCLUDED_HEAD, description = "gui.action.fake-block.excluded-materials")
    private Set<Material> excludedMaterials;
    @Serializable(headTexture = TICKS_HEAD, description = "gui.action.fake-block.ticks",
            additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Number(minValue = 0, scale = 1)
    private NumericValue ticks;

    public FakeBlockAction() {
        this(TARGET_DEFAULT, new MaterialValue(Material.STONE), Sets.newHashSet(Material.AIR), ALLOW_MATERIALS_DEFAULT,
                new NumericValue(100));
    }

    public FakeBlockAction(boolean target, MaterialValue material, Set<Material> excludedMaterials, boolean negate,
                           NumericValue ticks) {
        super(target);
        this.material = material;
        this.excludedMaterials = excludedMaterials;
        this.allowMaterials = negate;
        this.ticks = ticks;
    }

    public static FakeBlockAction deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        Set<Material> excludedMaterials = ((List<String>) map.get("excludedMaterials")).stream().map(Material::valueOf).
                collect(Collectors.toSet());
        boolean negate = (boolean) map.getOrDefault("allowMaterials", ALLOW_MATERIALS_DEFAULT);
        MaterialValue material = (MaterialValue) map.get("material");
        NumericValue ticks = (NumericValue) map.get("ticks");
        return new FakeBlockAction(target, material, excludedMaterials, negate, ticks);
    }

    public static List<Object> materialList(ModelPath<?> path) {
        return ItemUtils.getMaterialList().stream().filter(m -> ((Material) m).isBlock()).collect(Collectors.toList());
    }

    public static Function<Object, ItemStack> materialListMapper() {
        return ItemUtils.getMaterialMapper();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("==", getClass().getName());
        map.put("material", material);
        map.put("excludedMaterials", excludedMaterials.stream().map(Material::name).collect(Collectors.toList()));
        map.put("allowMaterials", allowMaterials);
        map.put("ticks", ticks);
        return map;
    }

    @Override
    public Action.ActionResult execute(Target target, Source source) {
        Location location = getLocation(target, source);
        World world = location.getWorld();

        if (world == null) {
            return Action.ActionResult.FAILURE;
        }

        // We do this to always target the block hit and not the above one
        location.subtract(0, 0.01, 0);

        if (source.getCaster() instanceof Player &&
                !SupremeItem.getInstance().getWorldGuardHook().
                        canBuild((Player) source.getCaster(), location)) {
            return Action.ActionResult.FAILURE;
        }

        if (allowMaterials ^ excludedMaterials.contains(location.getBlock().getType())) {
            return Action.ActionResult.FAILURE;
        }

        int ticks = this.ticks.getRealValue(target, source).intValue();
        Material type = location.getBlock().getType();
        List<Player> players = location.getWorld().getNearbyEntities(location, Bukkit.getViewDistance() * 16, 50, Bukkit.getViewDistance() * 16,
                entity -> entity instanceof Player).stream().map(entity -> (Player) entity).collect(Collectors.toList());
        players.forEach(
                player -> player.sendBlockChange(location,
                        Bukkit.createBlockData(material.getRealValue(target, source)))
        );

        Bukkit.getScheduler().runTaskLater(SupremeItem.getInstance(), () -> {
            players.forEach(
                    player -> player.sendBlockChange(location,
                            Bukkit.createBlockData(type))
            );
        }, ticks);
        return Action.ActionResult.SUCCESS;
    }

    @Override
    public Action clone() {
        return new SetBlockAction(target, material, new HashSet<>(excludedMaterials), allowMaterials);
    }

    @Override
    public String getName() {
        return "&6&lFake Block: &c" + WordUtils.capitalize(material.getName());
    }
}
