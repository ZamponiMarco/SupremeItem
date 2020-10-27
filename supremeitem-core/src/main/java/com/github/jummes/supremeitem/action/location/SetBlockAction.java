package com.github.jummes.supremeitem.action.location;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.google.common.collect.Sets;
import lombok.Setter;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Enumerable.Child
@Setter
@Enumerable.Displayable(name = "&c&lSet block", description = "gui.action.set-block.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2ZmOTgxN2Q3NjdkMmVkZTcxODFhMDU3YWEyNmYwOGY3ZWNmNDY1MWRlYzk3ZGU1YjU0ZWVkZTFkZDJiNDJjNyJ9fX0=")
public class SetBlockAction extends LocationAction {

    private static final String MATERIAL_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2ZmOTgxN2Q3NjdkMmVkZTcxODFhMDU3YWEyNmYwOGY3ZWNmNDY1MWRlYzk3ZGU1YjU0ZWVkZTFkZDJiNDJjNyJ9fX0";
    private static final String EXCLUDED_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWZkMjQwMDAwMmFkOWZiYmJkMDA2Njk0MWViNWIxYTM4NGFiOWIwZTQ4YTE3OGVlOTZlNGQxMjlhNTIwODY1NCJ9fX0=";

    @Serializable(headTexture = MATERIAL_HEAD, stringValue = true, description = "gui.action.set-block.material", fromListMapper = "materialListMapper", fromList = "materialList")
    private Material material;
    @Serializable(headTexture = EXCLUDED_HEAD, description = "gui.action.set-block.excluded-materials")
    private Set<Material> excludedMaterials;

    public SetBlockAction() {
        this(TARGET_DEFAULT, Material.STONE, Sets.newHashSet(Material.AIR));
    }

    public SetBlockAction(boolean target, Material material, Set<Material> excludedMaterials) {
        super(target);
        this.material = material;
        this.excludedMaterials = excludedMaterials;
    }

    public static SetBlockAction deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        Material material = Material.getMaterial((String) map.get("material"));
        Set<Material> excludedMaterials = ((List<String>) map.get("excludedMaterials")).stream().map(Material::valueOf).collect(Collectors.toSet());
        return new SetBlockAction(target, material, excludedMaterials);
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
        map.put("excludedMaterials", excludedMaterials.stream().map(Material::name).collect(Collectors.toList()));
        return map;
    }

    @Override
    protected ActionResult execute(Target target, Source source) {
        Location location = getLocation(target, source);

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
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(MATERIAL_HEAD),
                "&6&lSet Block: &c" + WordUtils.capitalize(material.name()), Libs.getLocale().getList("gui.action.description"));
    }

    @Override
    public Action clone() {
        return new SetBlockAction(target, material, new HashSet<>(excludedMaterials));
    }
}
