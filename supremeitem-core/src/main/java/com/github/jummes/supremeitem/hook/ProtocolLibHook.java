package com.github.jummes.supremeitem.hook;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.github.jummes.supremeitem.SupremeItem;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;

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
    public void sendSetBlockCrackPacket(Player p, Location l, int crack, int ticks) {
        int randomEid = new Random().nextInt();
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
        packet.getIntegers().write(0, randomEid);
        packet.getIntegers().write(1, crack);
        packet.getBlockPositionModifier().write(0, new BlockPosition(l.getBlock().getLocation().toVector()));

        PacketContainer packetDestroy = protocolManager.createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
        packetDestroy.getIntegers().write(0, randomEid);
        packetDestroy.getIntegers().write(1, -1);
        packetDestroy.getBlockPositionModifier().write(0, new BlockPosition(l.getBlock().getLocation().toVector()));

        protocolManager.sendServerPacket(p, packet);

        Bukkit.getScheduler().runTaskLater(SupremeItem.getInstance(), () -> {
            try {
                protocolManager.sendServerPacket(p, packetDestroy);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }, ticks);
    }

    @SneakyThrows
    public void sendHighlightBlockPacket(Player p, Location l, ChatColor color, int ticks) {
        int eid = new Random().nextInt();
        UUID id = UUID.randomUUID();

        PacketContainer spawnEntityPacket = protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
        spawnEntityPacket.getIntegers().write(0, eid).write(1, 70);
        spawnEntityPacket.getUUIDs().write(0, id);
        spawnEntityPacket.getDoubles().write(0, l.getBlock().getX() + .5).write(1,
                (double) l.getBlock().getY()).write(2, l.getBlock().getZ() + .5);

        PacketContainer glowPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        glowPacket.getIntegers().write(0, eid);
        WrappedDataWatcher dataWatcher = new WrappedDataWatcher();
        WrappedDataWatcher.WrappedDataWatcherObject isGlowing = new WrappedDataWatcher.
                WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class));
        dataWatcher.setObject(isGlowing, (byte) (0x60));
        glowPacket.getWatchableCollectionModifier().write(0, dataWatcher.getWatchableObjects());

        PacketContainer destroyEntityPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        destroyEntityPacket.getIntegerArrays().write(0, new int[]{eid});

        String team = RandomStringUtils.random(16);

        PacketContainer createTeamPacket = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
        createTeamPacket.getStrings().write(0, team);
        createTeamPacket.getIntegers().write(0, 0);
        createTeamPacket.getEnumModifier(ChatColor.class, MinecraftReflection.getMinecraftClass("EnumChatFormat")).
                write(0, color);
        createTeamPacket.getSpecificModifier(Collection.class).write(0, Lists.newArrayList(id.toString()));

        PacketContainer destroyTeamPacket = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
        destroyTeamPacket.getStrings().write(0, team);
        destroyTeamPacket.getIntegers().write(0, 1);

        protocolManager.sendServerPacket(p, spawnEntityPacket);
        protocolManager.sendServerPacket(p, glowPacket);
        protocolManager.sendServerPacket(p, createTeamPacket);

        Bukkit.getScheduler().runTaskLater(SupremeItem.getInstance(), () -> {
            try {
                protocolManager.sendServerPacket(p, destroyEntityPacket);
                protocolManager.sendServerPacket(p, destroyTeamPacket);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }, ticks);
    }

    @Override
    public boolean isEnabled() {
        return protocolManager != null;
    }
}
