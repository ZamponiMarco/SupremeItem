package com.github.jummes.supremeitem.entity.selector;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import org.bukkit.entity.LivingEntity;

import java.util.Map;
import java.util.function.Predicate;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lFilter the skill source", description = "gui.entity.selector.source.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM2Mzc5NjFmODQ1MWE1M2I2N2QyNTMxMmQzNTBjNjIwZjMyYjVmNjA4YmQ2YWRlMDY2MzdiZTE3MTJmMzY0ZSJ9fX0=")
public class SourceSelector extends EntitySelector {

    public SourceSelector() {
    }

    public SourceSelector(Map<String, Object> map) {
        super(map);
    }

    public SourceSelector(boolean deny) {
        super(deny);
    }

    @Override
    public Predicate<LivingEntity> getAbstractFilter(Source source, Target target) {
        return entity -> source.getCaster().equals(entity);
    }

    @Override
    public EntitySelector clone() {
        return new SourceSelector(deny);
    }

    @Override
    protected String getAbstractName() {
        return "&cSource &6&lselector";
    }
}
