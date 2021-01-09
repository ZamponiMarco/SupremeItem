package com.github.jummes.supremeitem.hook;

import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.entity.BlockEventAction;
import com.github.jummes.supremeitem.listener.paper.PlayerJumpListener;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;

@Getter
public class PaperHook implements ExternalHook {

    private boolean isJumpEventEnabled;

    public PaperHook() {
        PluginManager pluginManager = SupremeItem.getInstance().getServer().getPluginManager();
        try {
            Class.forName("com.destroystokyo.paper.event.player.PlayerJumpEvent");

            pluginManager.registerEvents(new PlayerJumpListener(), SupremeItem.getInstance());
            BlockEventAction.getEventsList(null).add("jump");
            isJumpEventEnabled = true;
        } catch (ClassNotFoundException ignored) {
        }
    }

    @Override
    public boolean isEnabled() {
        return isJumpEventEnabled;
    }
}
