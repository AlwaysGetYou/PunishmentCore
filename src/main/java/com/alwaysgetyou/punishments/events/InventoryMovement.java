package com.alwaysgetyou.punishments.events;

import com.alwaysgetyou.punishments.commands.Check;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryMovement implements Listener {
    @EventHandler
    public void onMove(InventoryClickEvent event) {
        Check c = new Check();
        if (event.getInventory().getTitle().equalsIgnoreCase(Check.inv_name))
            event.setCancelled(true);
    }
}

