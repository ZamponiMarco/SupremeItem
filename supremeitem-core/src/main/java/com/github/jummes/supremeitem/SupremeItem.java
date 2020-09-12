package com.github.jummes.supremeitem;

import com.github.jummes.libs.command.PluginCommandExecutor;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.localization.PluginLocale;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.entity.*;
import com.github.jummes.supremeitem.action.location.ParticleAction;
import com.github.jummes.supremeitem.action.location.SoundAction;
import com.github.jummes.supremeitem.action.meta.*;
import com.github.jummes.supremeitem.command.*;
import com.github.jummes.supremeitem.condition.Condition;
import com.github.jummes.supremeitem.condition.numeric.LessThanCondition;
import com.github.jummes.supremeitem.entity.Entity;
import com.github.jummes.supremeitem.entity.GenericEntity;
import com.github.jummes.supremeitem.entity.ItemEntity;
import com.github.jummes.supremeitem.entity.NoEntity;
import com.github.jummes.supremeitem.entity.selector.EntitySelector;
import com.github.jummes.supremeitem.entity.selector.SourceSelector;
import com.github.jummes.supremeitem.item.Item;
import com.github.jummes.supremeitem.listener.PlayerItemListener;
import com.github.jummes.supremeitem.manager.CooldownManager;
import com.github.jummes.supremeitem.manager.ItemManager;
import com.github.jummes.supremeitem.manager.SavedSkillManager;
import com.github.jummes.supremeitem.manager.TimerManager;
import com.github.jummes.supremeitem.placeholder.numeric.ConstantNumberPlaceholder;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import com.github.jummes.supremeitem.placeholder.Placeholder;
import com.github.jummes.supremeitem.placeholder.numeric.HealthPlaceholder;
import com.github.jummes.supremeitem.savedskill.SavedSkill;
import com.github.jummes.supremeitem.skill.*;
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
        ConfigurationSerialization.registerClass(RightClickSkill.class);
        ConfigurationSerialization.registerClass(LeftClickSkill.class);
        ConfigurationSerialization.registerClass(HitEntitySkill.class);
        ConfigurationSerialization.registerClass(DamageEntitySkill.class);
        ConfigurationSerialization.registerClass(TimerSkill.class);

        ConfigurationSerialization.registerClass(Action.class);
        ConfigurationSerialization.registerClass(DamageAction.class);
        ConfigurationSerialization.registerClass(EffectAction.class);
        ConfigurationSerialization.registerClass(HealAction.class);
        ConfigurationSerialization.registerClass(MessageAction.class);
        ConfigurationSerialization.registerClass(PushAction.class);
        ConfigurationSerialization.registerClass(PullAction.class);
        ConfigurationSerialization.registerClass(ParticleAction.class);
        ConfigurationSerialization.registerClass(ParticleAction.ParticleOptions.class);
        ConfigurationSerialization.registerClass(ParticleAction.DustOptionsData.class);
        ConfigurationSerialization.registerClass(ParticleAction.ItemStackData.class);
        ConfigurationSerialization.registerClass(ParticleAction.BlockDataData.class);
        ConfigurationSerialization.registerClass(SoundAction.class);
        ConfigurationSerialization.registerClass(DelayedAction.class);
        ConfigurationSerialization.registerClass(ProjectileAction.class);
        ConfigurationSerialization.registerClass(AreaEntitiesAction.class);
        ConfigurationSerialization.registerClass(TimerAction.class);
        ConfigurationSerialization.registerClass(CancelEventAction.class);

        ConfigurationSerialization.registerClass(Entity.class);
        ConfigurationSerialization.registerClass(GenericEntity.class);
        ConfigurationSerialization.registerClass(NoEntity.class);
        ConfigurationSerialization.registerClass(ItemEntity.class);


        ConfigurationSerialization.registerClass(EntitySelector.class);
        ConfigurationSerialization.registerClass(SourceSelector.class);

        ConfigurationSerialization.registerClass(SavedSkill.class);

        ConfigurationSerialization.registerClass(Condition.class);
        ConfigurationSerialization.registerClass(LessThanCondition.class);

        ConfigurationSerialization.registerClass(Placeholder.class);
        ConfigurationSerialization.registerClass(NumericPlaceholder.class);
        ConfigurationSerialization.registerClass(ConstantNumberPlaceholder.class);
        ConfigurationSerialization.registerClass(HealthPlaceholder.class);
    }

    private ItemManager itemManager;
    private CooldownManager cooldownManager;
    private SavedSkillManager savedSkillManager;

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
        new TimerManager();
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
