package com.github.jummes.supremeitem.database;

import com.github.jummes.libs.database.Database;
import com.github.jummes.supremeitem.util.CompressUtils;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

public class CompressedYamlDatabase<T extends NamedModel> extends Database<T> {

    private static final String FILE_SUFFIX = ".yml";

    private String name;
    private File dataFile;
    private YamlConfiguration yamlConfiguration;

    public CompressedYamlDatabase(@NonNull Class<T> classObject, @NonNull JavaPlugin plugin) {
        super(classObject, plugin);
    }

    @Override
    protected void openConnection() {
        this.name = classObject.getSimpleName().toLowerCase();

        this.dataFile = new File(plugin.getDataFolder(), name.concat(FILE_SUFFIX));

        if (!this.dataFile.exists()) {
            plugin.saveResource(classObject.getSimpleName().toLowerCase().concat(FILE_SUFFIX), false);
        }

        this.yamlConfiguration = new YamlConfiguration();
    }

    @Override
    protected void closeConnection() {
        try {
            yamlConfiguration.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    @Override
    public List<T> loadObjects() {
        loadConfiguration();

        List<T> list = new ArrayList<>();

        if (yamlConfiguration.isList(this.name)) {
            list.addAll((Collection<? extends T>) this.yamlConfiguration.getList(this.name));
            list.forEach(this::saveObject);
            yamlConfiguration.set(this.name, null);
            yamlConfiguration.save(dataFile);
        }

        yamlConfiguration.getKeys(false).forEach(key -> {
                    String string = yamlConfiguration.getString(key);
                    T obj = (T) NamedModel.fromSerializedString(new String(CompressUtils.
                            decompress(Base64.getDecoder().decode(string))));
                    if (obj != null) {
                        list.add(obj);
                    }
                }
        );
        return list;
    }

    @SneakyThrows
    @Override
    public void saveObject(@NonNull T t) {
        if (!t.getName().equals(t.getOldName())) {
            yamlConfiguration.set(t.getOldName(), null);
        }
        yamlConfiguration.set(t.getName(), new String(Base64.getEncoder().encode(CompressUtils.compress(t.toSerializedString().getBytes()))));
        yamlConfiguration.save(dataFile);

        t.setOldName(t.getName());
    }

    @SneakyThrows
    @Override
    public void deleteObject(@NonNull T t) {
        yamlConfiguration.set(t.name, null);
        yamlConfiguration.save(dataFile);
    }

    @SneakyThrows
    private void loadConfiguration() {
        this.yamlConfiguration.load(dataFile);
    }
}
