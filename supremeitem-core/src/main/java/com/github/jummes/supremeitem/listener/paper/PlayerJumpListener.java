package com.github.jummes.supremeitem.listener.paper;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.item.Item;
import com.github.jummes.supremeitem.skill.EntityJumpSkill;
import com.github.jummes.supremeitem.skill.Skill;
import com.github.jummes.supremeitem.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

public class PlayerJumpListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJump(PlayerJumpEvent e) {
        if (e.getPlayer().getMetadata("jump").stream().anyMatch(metadataValue ->
                Objects.equals(metadataValue.getOwningPlugin(), SupremeItem.getInstance()))) {
            e.setCancelled(true);
        }

        Player player = e.getPlayer();
        boolean cancelled = executeJumpSkill(player);
        if (cancelled) {
            e.setCancelled(true);
        }
    }

    private boolean executeJumpSkill(Player player) {
        AtomicBoolean toReturn = new AtomicBoolean(false);
        List<ItemStack> items = Utils.getEntityItems(player);
        IntStream.range(0, items.size()).filter(i -> Item.isSupremeItem(items.get(i))).forEach(i -> {
            UUID id = UUID.fromString(Libs.getWrapper().getTagItem(items.get(i), "supreme-item"));
            Item supremeItem = SupremeItem.getInstance().getItemManager().getById(id);
            if (supremeItem != null) {
                supremeItem.getSkillSet().stream().filter(skill -> skill instanceof EntityJumpSkill &&
                        skill.getAllowedSlots().contains(EquipmentSlot.values()[i])).findFirst().
                        ifPresent(skill -> {
                            if (((EntityJumpSkill) skill).executeSkill(player, id, items.get(i)).
                                    equals(Skill.SkillResult.CANCELLED)) {
                                toReturn.set(true);
                            }
                        });
            }
        });
        return toReturn.get();
    }

}
