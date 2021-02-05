package com.github.jummes.supremeitem.hook;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ProtocolLibHook implements ExternalHook {

    private ProtocolManager protocolManager;

    public ProtocolLibHook() {
        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            this.protocolManager = ProtocolLibrary.getProtocolManager();
        }
    }

    @SneakyThrows
    public void sendSetSlotPacket(Player p, int slot, ItemStack item) {
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SET_SLOT);
        packet.getIntegers().write(0, 0);
        packet.getIntegers().write(1, slot);
        packet.getItemModifier().write(0, item);
        protocolManager.sendServerPacket(p, packet);
    }

    @Override
    public boolean isEnabled() {
        return protocolManager != null;
    }
}
