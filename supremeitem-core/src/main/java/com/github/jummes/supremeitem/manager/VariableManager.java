package com.github.jummes.supremeitem.manager;

import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class VariableManager {

    private final Map<LivingEntity, Map<String, Double>> numericVariables;

    public VariableManager() {
        this.numericVariables = new HashMap<>();
    }

    public Double getNumericVariable(LivingEntity e, String name) {
        if (!numericVariables.containsKey(e)) {
            return 0.0;
        }
        return numericVariables.get(e).getOrDefault(name, 0.0);
    }

    public void setNumericVariable(LivingEntity e, String name, double value) {
        if (!numericVariables.containsKey(e)) {
            numericVariables.put(e, new HashMap<>());
        }
        numericVariables.get(e).put(name, value);
    }

}
