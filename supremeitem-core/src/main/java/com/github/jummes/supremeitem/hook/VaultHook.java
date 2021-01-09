package com.github.jummes.supremeitem.hook;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook implements ExternalHook {

    private Economy economy;

    public VaultHook() {
        if (!Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            return;
        }

        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().
                getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }

        this.economy = rsp.getProvider();
    }

    public boolean hasMoney(Player player, double money) {
        return economy.has(player, money);
    }

    public void giveMoney(Player player, double money) {
        economy.depositPlayer(player, money);
    }

    public void takeMoney(Player player, double money) {
        economy.withdrawPlayer(player, money);
    }

    public double getBalance(Player player) {
        return economy.getBalance(player);
    }

    @Override
    public boolean isEnabled() {
        return economy != null;
    }
}
