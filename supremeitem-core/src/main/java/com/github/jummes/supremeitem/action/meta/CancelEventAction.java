package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.google.common.collect.Lists;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@Enumerable.Child
public class CancelEventAction extends MetaAction {
    
    public CancelEventAction() {
        
    }

    public static CancelEventAction deserialize(Map<String, Object> map) {
        return new CancelEventAction();
    }
    
    @Override
    protected ActionResult execute(Target target, Source source) {
        return ActionResult.CANCELLED;
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(LocationTarget.class, EntityTarget.class);
    }

    @Override
    public ItemStack getGUIItem() {
        return super.getGUIItem();
    }
}
