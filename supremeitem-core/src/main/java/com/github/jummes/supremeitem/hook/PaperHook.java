package com.github.jummes.supremeitem.hook;

import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.entity.BlockEventAction;
import com.github.jummes.supremeitem.listener.paper.PlayerChangeArmorListener;
import com.github.jummes.supremeitem.listener.paper.PlayerCrossBowLoadListener;
import com.github.jummes.supremeitem.listener.paper.PlayerJumpListener;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

@Getter
public class PaperHook implements ExternalHook {

    private boolean isJumpEventEnabled;
    private boolean isCrossbowLoadEventEnabled;
    private boolean isSendActionBarEnabled;
    private boolean isEquipChangeEnabled;

    public PaperHook() {
        PluginManager pluginManager = SupremeItem.getInstance().getServer().getPluginManager();
        try {
            Class.forName("com.destroystokyo.paper.event.player.PlayerJumpEvent");

            pluginManager.registerEvents(new PlayerJumpListener(), SupremeItem.getInstance());
            BlockEventAction.getEventsList(null).add("jump");
            isJumpEventEnabled = true;
        } catch (ClassNotFoundException ignored) {
        }
        try {
            Class.forName("io.papermc.paper.event.entity.EntityLoadCrossbowEvent");

            pluginManager.registerEvents(new PlayerCrossBowLoadListener(), SupremeItem.getInstance());
            isCrossbowLoadEventEnabled = true;
        } catch (ClassNotFoundException ignored) {
        }
        try {
            Player.class.getMethod("sendActionBar", String.class);
            isSendActionBarEnabled = true;
        } catch (NoSuchMethodException ignored) {
        }
        try {
            Class.forName("com.destroystokyo.paper.event.player.PlayerArmorChangeEvent");

            pluginManager.registerEvents(new PlayerChangeArmorListener(), SupremeItem.getInstance());
            isEquipChangeEnabled = true;
        } catch (ClassNotFoundException ignored) {
        }
    }

    @Override
    public boolean isEnabled() {
        return isJumpEventEnabled || isCrossbowLoadEventEnabled;
    }
}
