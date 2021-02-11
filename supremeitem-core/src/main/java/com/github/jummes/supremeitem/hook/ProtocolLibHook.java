package com.github.jummes.supremeitem.hook;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

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

    @SneakyThrows
    public void sendSetBlockCrackPacket(Player p, Location l, int crack) {
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
        packet.getIntegers().write(0, new Random().nextInt());
        packet.getIntegers().write(1, crack);
        packet.getBlockPositionModifier().write(0, new BlockPosition(l.getBlock().getLocation().toVector()));
        protocolManager.sendServerPacket(p, packet);
    }

    @Override
    public boolean isEnabled() {
        return protocolManager != null;
    }
}
