package com.github.jummes.supremeitem.database;

import com.github.jummes.libs.database.Database;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.util.CompressUtils;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

public class CompressedYamlDatabase<T extends NamedModel> extends Database<T> {

    private static final String FILE_SUFFIX = ".yml";

    private String name;
    private File dataFile;
    private YamlConfiguration yamlConfiguration;

    private List<String> usedNames;

    public CompressedYamlDatabase(@NonNull Class<T> classObject, @NonNull JavaPlugin plugin) {
        super(classObject, plugin);
        this.usedNames = new ArrayList<>();
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
            handleOldConfig(list);
        } else {
            yamlConfiguration.getKeys(false).forEach(key -> {
                        String string = yamlConfiguration.getString(key);
                        T obj = (T) NamedModel.fromSerializedString(new String(CompressUtils.
                                decompress(Base64.getDecoder().decode(string)), Charset.defaultCharset()));
                        if (obj != null) {
                            list.add(obj);
                        }
                    }
            );
        }
        return list;
    }

    private void handleOldConfig(List<T> list) throws IOException {
        File backupFolder = new File(SupremeItem.getInstance().getDataFolder(), "backup");
        if (!backupFolder.exists()) {
            backupFolder.mkdir();
        }
        File backupFile = new File(backupFolder, dataFile.getName());
        if (!backupFile.exists()) {
            backupFile.createNewFile();
        }
        FileUtil.copy(dataFile, backupFile);
        list.addAll((Collection<? extends T>) this.yamlConfiguration.getList(this.name));
        list.forEach(t -> {
            validateDeserializedName(t);
            saveObject(t);
        });
        yamlConfiguration.set(this.name, null);
        yamlConfiguration.save(dataFile);
    }

    @SneakyThrows
    @Override
    public void saveObject(@NonNull T t) {
        if (!t.getName().equals(t.getOldName())) {
            yamlConfiguration.set(t.getOldName(), null);
            usedNames.remove(t.getOldName());
        }
        yamlConfiguration.set(t.getName(), new String(Base64.getEncoder().encode(CompressUtils.
                compress(t.toSerializedString().getBytes())), Charset.defaultCharset()));
        yamlConfiguration.save(dataFile);

        usedNames.add(t.getName());
        t.setOldName(t.getName());
    }

    private void validateDeserializedName(@NonNull T t) {
        String newName;
        while (usedNames.contains(t.getName())) {
            newName = t.getName() + "-copy";
            t.setName(newName);
            t.setOldName(newName);
        }
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
