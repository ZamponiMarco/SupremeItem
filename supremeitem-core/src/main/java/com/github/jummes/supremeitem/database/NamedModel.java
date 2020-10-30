package com.github.jummes.supremeitem.database;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;

@Getter
@Setter
public abstract class NamedModel implements Model {

    private static final String NAME_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0=";
    @Serializable(headTexture = NAME_HEAD, description = "gui.name")
    protected String name;
    private String oldName;

    public NamedModel(String name) {
        this.name = name;
        this.oldName = name;
    }

    @SneakyThrows
    public static NamedModel fromSerializedString(String string) {
        YamlConfiguration config = new YamlConfiguration();
        config.loadFromString(string);
        return (NamedModel) config.get("model");
    }

    public String toSerializedString() {
        YamlConfiguration config = new YamlConfiguration();
        config.set("model", this);
        return config.saveToString();
    }
}