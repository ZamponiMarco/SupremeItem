package com.github.jummes.supremeitem.placeholder.material;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.placeholder.Placeholder;
import org.bukkit.Material;

@Enumerable.Parent(classArray = {BlockMaterialPlaceholder.class})
public abstract class MaterialPlaceholder extends Placeholder<Material> {
    public MaterialPlaceholder(boolean target) {
        super(target);
    }

    public abstract MaterialPlaceholder clone();
}
