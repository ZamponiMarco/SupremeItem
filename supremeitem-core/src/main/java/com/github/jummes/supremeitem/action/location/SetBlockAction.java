package com.github.jummes.supremeitem.action.location;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Enumerable.Child
@Setter
public class SetBlockAction extends LocationAction {

    private static final String TYPE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWY4NDczNWZjOWM3NjBlOTVlYWYxMGNlYzRmMTBlZGI1ZjM4MjJhNWZmOTU1MWVlYjUwOTUxMzVkMWZmYTMwMiJ9fX0=";

    @Serializable(headTexture = TYPE_HEAD, stringValue = true, description = "gui.action.particle.type", fromListMapper = "materialListMapper", fromList = "materialList")
    private Material material;
    @Serializable(headTexture = TYPE_HEAD)
    private Set<Material> excludedMaterials;

    public SetBlockAction() {
        this(Material.STONE, Sets.newHashSet(Material.AIR));
    }

    public static SetBlockAction deserialize(Map<String, Object> map) {
        Material material = Material.getMaterial((String) map.get("material"));
        Set<Material> excludedMaterials = new HashSet<>((List<Material>) map.get("excludedMaterials"));
        return new SetBlockAction(material, excludedMaterials);
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
        map.put("material", material.name());
        map.put("excludedMaterials", new ArrayList<>(excludedMaterials));
        return map;
    }

    @Override
    protected ActionResult execute(Target target, Source source) {
        Location location = null;
        if (target instanceof EntityTarget) {
            location = ((EntityTarget) target).getTarget().getLocation();
        } else if (target instanceof LocationTarget) {
            location = ((LocationTarget) target).getTarget();
        }

        if (location == null) {
            return ActionResult.FAILURE;
        }

        // We do this to always target the block hit and not the above one
        location.subtract(0, 0.01, 0);

        if (source.getCaster() instanceof Player &&
                !SupremeItem.getInstance().getWorldGuardHook().
                        canBuild((Player) source.getCaster(), location)) {
            return ActionResult.FAILURE;
        }

        if (excludedMaterials.contains(location.getBlock().getType())) {
            return ActionResult.FAILURE;
        }

        location.getBlock().setType(material);
        return ActionResult.SUCCESS;
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(LocationTarget.class, EntityTarget.class);
    }
}
