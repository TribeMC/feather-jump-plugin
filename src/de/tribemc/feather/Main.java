package de.tribemc.feather;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Main extends JavaPlugin implements Listener {

	private LinkedHashMap<UUID, Long> uses;
	private long cooldown;

	@Override
	public void onEnable() {
		ConsoleCommandSender cs = getServer().getConsoleSender();
		cs.sendMessage("§f[FeatherJump] §9Feder Jump Plugin");
		cs.sendMessage("§f[FeatherJump] §9erstellt von §6V3lop5");
		getServer().getPluginManager().registerEvents(this, this);
		this.uses = new LinkedHashMap<>();
		this.uses = new LinkedHashMap<>();
		this.cooldown = 5000;
		cs.sendMessage("§f[FeatherJump] §9wurde §ageladen§9!");
		super.onEnable();
	}

	@Override
	public void onDisable() {
		ConsoleCommandSender cs = getServer().getConsoleSender();
		cs.sendMessage("§f[FeatherJump] §9Plugin wird entladen");
		super.onDisable();
	}

	public void setUse(UUID id) {
		if (this.uses.containsKey(id))
			removeUse(id);
		this.uses.put(id, System.currentTimeMillis());
	}

	public long lastUse(UUID id) {
		if (this.uses.containsKey(id))
			return this.uses.get(id);
		return 0;
	}

	public void removeUse(UUID id) {
		this.uses.remove(id);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		UUID id = e.getPlayer().getUniqueId();
		if (this.uses.containsKey(id))
			removeUse(id);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction().equals(org.bukkit.event.block.Action.PHYSICAL))
			return;
		if (!e.hasItem())
			return;
		if (!e.getItem().getType().equals(Material.FEATHER))
			return;
		UUID id = e.getPlayer().getUniqueId();
		long next = lastUse(id) + cooldown;
		if (next > System.currentTimeMillis()) {
			next -= System.currentTimeMillis();
			e.getPlayer().sendMessage(
					"§7Du musst noch §c" + ((int) (next / 1000))
							+ " Sekunden §7warten!");
			return;
		}
		setUse(id);
		e.getPlayer().setVelocity(new Vector(0, 1.1, 0));
		e.getPlayer().playSound(e.getPlayer().getLocation(),
				Sound.ENDERDRAGON_WINGS, 1F, 1F);
	}
}
