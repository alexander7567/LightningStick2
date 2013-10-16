package com.alexriggs.bukkit.lightningstick2;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class LightningStick2 extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		getLogger().info("LightningStick enabled. Visit https://alexriggs.com/viewforum.php?f=150 for support!");
		getServer().getPluginManager().registerEvents(this, this);
		this.saveDefaultConfig();
		URL url;
		try {
			url = new URL("http://alexriggs.com/test.php");
			URLConnection connection = url.openConnection();
			connection.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;

			while((inputLine = in.readLine()) != null)
			{
				System.out.println(inputLine);
			}

			in.close();
		} catch (MalformedURLException e) {
			getLogger().info("MalformedURLException: " + e);
			e.printStackTrace();
		} catch (IOException e) {
			getLogger().info("IOException: " + e);
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("lightningstick")){
			if (args.length == 0) {
				sender.sendMessage("You must enter a command.");
			return true;
			} else {
				if (args[0].equalsIgnoreCase("reload")) {
					this.reloadConfig();
					sender.sendMessage("Configuration file has been reloaded.");
				} else {
					sender.sendMessage("Command not found.");
				}
			}
		}
		return false; 
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerUse(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (p.hasPermission("lightningstick.use")) {

			if (event.getAction().equals(Action.LEFT_CLICK_AIR)
					|| event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				if (p.getItemInHand().getType() == Material.STICK) {
					if (this.getConfig().getBoolean("effectOnly")) {
						LightningStrike lightning = p.getWorld().strikeLightningEffect(p.getTargetBlock(null, 500).getLocation());
					} else {
						LightningStrike lightning = p.getWorld().strikeLightning(p.getTargetBlock(null, 500).getLocation());
					}

				}
			} else if (event.getAction().equals(Action.RIGHT_CLICK_AIR)
					|| event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				Random generator = new Random();
				if (p.getItemInHand().getType() == Material.STICK) {
					int i = 0;
					while (i < this.getConfig().getInt("strikesOnRightClick")) {
						Location loc = p.getTargetBlock(null, 500).getLocation();
						loc.setX(loc.getX() + generator.nextInt(10) - 5);
						loc.setZ(loc.getZ() + generator.nextInt(10) - 5);
						if (this.getConfig().getBoolean("effectOnly")) {
							LightningStrike lightning = p.getWorld().strikeLightningEffect(loc);
						} else {
							LightningStrike lightning = p.getWorld().strikeLightning(loc);
						}
						i++;
					}
				}
			}
		}
	}
}

