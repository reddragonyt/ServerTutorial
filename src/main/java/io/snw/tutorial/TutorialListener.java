package io.snw.tutorial;

import io.snw.tutorial.enums.ViewType;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class TutorialListener implements Listener {


    ServerTutorial plugin;


    public TutorialListener(ServerTutorial plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (plugin.isInTutorial(name)) {
            if (plugin.getCurrentTutorial(name).getViewType() == ViewType.CLICK) {
                if (event.getAction() != Action.PHYSICAL) {
                    if (player.getItemInHand().getType() == this.plugin.getCurrentTutorial(name).getItem()) {
                        if (this.plugin.getCurrentTutorial(name).getTotalViews() == this.plugin.getCurrentView(name)) {
                            plugin.getEndTutorial().endTutorial(player);
                        } else {
                            this.plugin.incrementCurrentView(name);
                            plugin.getTutorialUtils().textUtils(player);
                            player.teleport(plugin.getTutorialView(name).getLocation());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (plugin.isInTutorial(player.getName())) {
            player.teleport(plugin.getTutorialView(player.getName()).getLocation());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (plugin.isInTutorial(event.getPlayer().getName())) {
            plugin.removeFromTutorial(event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (plugin.isInTutorial(event.getPlayer().getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (plugin.isInTutorial(event.getPlayer().getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        if (plugin.isInTutorial(event.getPlayer().getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(PlayerDropItemEvent event) {
        if (plugin.isInTutorial(event.getPlayer().getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWhee(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (plugin.isInTutorial(((Player) event.getEntity()).getName())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (String name : plugin.getAllInTutorial()) {
            Player tut = plugin.getServer().getPlayerExact(name);
            tut.hidePlayer(player);
            player.hidePlayer(tut);
        }
    }
}
