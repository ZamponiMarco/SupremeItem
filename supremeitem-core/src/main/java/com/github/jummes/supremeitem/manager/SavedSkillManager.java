package com.github.jummes.supremeitem.manager;

import com.github.jummes.libs.model.ModelManager;
import com.github.jummes.supremeitem.savedskill.SavedSkill;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public class SavedSkillManager extends ModelManager<SavedSkill> {

    private final List<SavedSkill> skills;

    public SavedSkillManager(Class<SavedSkill> classObject, String databaseType, JavaPlugin plugin) {
        super(classObject, databaseType, plugin);
        this.skills = database.loadObjects();
    }

    public SavedSkill getByName(String name) {
        return skills.stream().filter(skill -> skill.getName().equals(name)).findFirst().orElse(null);
    }

    public void addSkill(SavedSkill skill) {
        skills.add(skill);
        saveModel(skill);
    }
}
