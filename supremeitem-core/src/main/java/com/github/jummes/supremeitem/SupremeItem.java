package com.github.jummes.supremeitem;

import com.github.jummes.libs.command.PluginCommandExecutor;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.localization.PluginLocale;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.location.ParticleAction;
import com.github.jummes.supremeitem.command.*;
import com.github.jummes.supremeitem.condition.Condition;
import com.github.jummes.supremeitem.condition.bool.BooleanCondition;
import com.github.jummes.supremeitem.entity.Entity;
import com.github.jummes.supremeitem.entity.selector.EntitySelector;
import com.github.jummes.supremeitem.item.Item;
import com.github.jummes.supremeitem.listener.PlayerItemListener;
import com.github.jummes.supremeitem.manager.*;
import com.github.jummes.supremeitem.placeholder.Placeholder;
import com.github.jummes.supremeitem.placeholder.numeric.NumericValue;
import com.github.jummes.supremeitem.savedskill.SavedSkill;
import com.github.jummes.supremeitem.skill.Skill;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.util.Objects;

@Getter
public class SupremeItem extends JavaPlugin {

    private static final String CONFIG_VERSION = "0.1";

    static {
        Libs.registerSerializables();

        ConfigurationSerialization.registerClass(Item.class);

        ConfigurationSerialization.registerClass(Skill.class);

        ConfigurationSerialization.registerClass(Action.class);

        ConfigurationSerialization.registerClass(Entity.class);

        ConfigurationSerialization.registerClass(EntitySelector.class);

        ConfigurationSerialization.registerClass(SavedSkill.class);

        ConfigurationSerialization.registerClass(Condition.class);
        ConfigurationSerialization.registerClass(BooleanCondition.class,
                "com.github.jummes.supremeitem.condition.BooleanCondition");

        ConfigurationSerialization.registerClass(Placeholder.class);

        ConfigurationSerialization.registerClass(NumericValue.class);

        ConfigurationSerialization.registerClass(ParticleAction.BlockDataData.class);
        ConfigurationSerialization.registerClass(ParticleAction.DustOptionsData.class);
        ConfigurationSerialization.registerClass(ParticleAction.ItemStackData.class);
    }

    private ItemManager itemManager;
    private CooldownManager cooldownManager;
    private SavedSkillManager savedSkillManager;
    private TimerManager timerManager;
    private VariableManager variableManager;

    public static SupremeItem getInstance() {
        return getPlugin(SupremeItem.class);
    }

    @Override
    public void onEnable() {
        setUpFolder();
        setUpData();
        setUpCommands();
        setUpListeners();
    }

    private void setUpFolder() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        File configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            saveDefaultConfig();
        }

        if (!Objects.equals(getConfig().getString("version"), CONFIG_VERSION)) {
            getLogger().info("config.yml has changed. Old config is stored inside config-"
                    + getConfig().getString("version") + ".yml");
            File outputFile = new File(getDataFolder(), "config-" + getConfig().getString("version") + ".yml");
            FileUtil.copy(configFile, outputFile);
            configFile.delete();
            saveDefaultConfig();
        }
    }

    private void setUpData() {
        PluginLocale locale = new PluginLocale(this, Lists.newArrayList("en-US"), "en-US");
        Libs.initializeLibrary(this, locale);
        itemManager = new ItemManager(Item.class, "yaml", this);
        cooldownManager = new CooldownManager();
        savedSkillManager = new SavedSkillManager(SavedSkill.class, "yaml", this);
        timerManager = new TimerManager();
        variableManager = new VariableManager();
    }

    private void setUpCommands() {
        PluginCommandExecutor ex = new PluginCommandExecutor(HelpCommand.class, "help");
        ex.registerCommand("skill", SkillsListCommand.class);
        ex.registerCommand("list", ItemListCommand.class);
        ex.registerCommand("get", ItemGetCommand.class);
        ex.registerCommand("give", ItemGiveCommand.class);
        getCommand("si").setExecutor(ex);
    }

    private void setUpListeners() {
        getServer().getPluginManager().registerEvents(new PlayerItemListener(), this);
    }
}
