package com.github.jummes.supremeitem.manager;

import com.github.jummes.libs.model.ModelManager;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.savedplaceholder.SavedPlaceholder;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

@Getter
public class SavedPlaceholderManager extends ModelManager<SavedPlaceholder> {

    List<SavedPlaceholder> placeholders;

    public SavedPlaceholderManager(Class<SavedPlaceholder> classObject, String databaseType, JavaPlugin plugin) {
        super(classObject, databaseType, plugin, new HashMap<>());
        this.placeholders = database.loadObjects();
    }

    public SavedPlaceholder getByName(String name) {
        return placeholders.stream().filter(savedPlaceholder -> savedPlaceholder.getName().equals(name)).findFirst().
                orElse(null);
    }

    public String computePlaceholders(String input, Source source, Target target) {
        return placeholders.stream().reduce(input, (string, placeholder) -> string.replaceAll(
                "%" + placeholder.getName(), placeholder.getPlaceholder().computePlaceholder(target, source).
                        toString()), (s1, s2) -> s1);
    }
}
