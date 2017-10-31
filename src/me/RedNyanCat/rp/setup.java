package me.RedNyanCat.rp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public class setup extends JavaPlugin implements Listener{

	public static setup plugin;
	public commands commands = new commands(this);

	public HashMap<UUID, PermissionAttachment> playerPermissions = new HashMap<>();
	public HashMap<UUID, String> rank = new HashMap<>();
	public HashMap<UUID, String> suffix = new HashMap<>();
	public HashMap<UUID, String> playerStatRank = new HashMap<>();
	public HashMap<UUID, String> playerStatSuffix = new HashMap<>();
	public HashMap<UUID, ranks> playerRank = new HashMap<>();

	public void npMsg(Player p){

		p.sendMessage(cc("&4&lOops! It looks like you do not have permission to do this!"));

	}

	public void onEnable(){

		plugin = this;
		this.getServer().getPluginManager().registerEvents(this, this);

		this.getCommand("RubyPermissions").setExecutor(commands);

		createConfig();

		new BukkitRunnable() {

			public void run(){

				if(!Bukkit.getServer().getOnlinePlayers().isEmpty()){

					for(Player p : Bukkit.getOnlinePlayers()){

						if(p.isOnline() && p != null){

							setupRank(p);
							defaultRankSetter(p);
							permissionSetter();
							suffixSetter();
							universalPermissions();

						}

					}

				}

			}

		}.runTaskTimer(this, 40L, 20L);

	}

	public void onDisable(){

		this.playerRank.clear();

	}

	public String cc(String msg){

		return ChatColor.translateAlternateColorCodes('&', msg);

	}

	private void createConfig() {

		try {

			if (!getDataFolder().exists()) {

				getDataFolder().mkdirs();

			}

			File file = new File(getDataFolder(), "config.yml");

			if (!file.exists()) {

				getLogger().info("Config.yml not found, creating!");
				saveDefaultConfig();

			} else {

				getLogger().info("Config.yml found, loading!");

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public void setupRank(Player p){

		this.playerRank.put(p.getUniqueId(), new ranks(this.rank.get(p.getUniqueId()), this.suffix.get(p.getUniqueId()), this.playerStatRank.get(p.getUniqueId()), this.playerStatSuffix.get(p.getUniqueId())));

	}

	public void changeSuffix(UUID uuid, String setSuffix, boolean announce, CommandSender sender){

		Player p = Bukkit.getPlayer(uuid);

		if((ranks)this.playerRank.get(uuid) != null){

			String r = this.playerStatSuffix.get(uuid);

			if(r != null){

				List<String> previousPlayers = getConfig().getStringList("Suffixes." + r.toString() + ".Players");

				previousPlayers.remove(p.getName());

				getConfig().set("Suffixes." + r.toString() + ".Players", previousPlayers);
				
				this.playerStatSuffix.remove(uuid);

				saveConfig();

			}

		} else {

			if(announce){

				sender.sendMessage(cc("&7The players suffix &4could not be changed to&7: &f") + setSuffix + cc("&7!"));

			}

			return;

		}

		if(setSuffix == null){

			if(announce){

				sender.sendMessage(cc("&7The players suffix &4could not be changed to&7: &f") + setSuffix + cc("&7!"));

			}

			return;

		}

		for(String groups : getConfig().getConfigurationSection("Suffixes").getKeys(false)){

			if(setSuffix.equals(groups)){

				List<String> players = getConfig().getStringList("Suffixes." + groups + ".Players");

				players.add(p.getName());

				getConfig().set("Suffixes." + groups + ".Players", players);
				saveConfig();

				if(setSuffix != null){
					if(p != null){
						if(p.isOnline()){

							this.suffix.remove(uuid);

							this.suffix.put(uuid, getConfig().getString("Suffixes." + groups + ".Suffix"));

							this.playerStatSuffix.put(uuid, setSuffix);

						}
					}

					if(announce){

						sender.sendMessage(cc("&7The players suffix &2has been changed to&7: &f") + setSuffix + cc("&7!"));

					}

				}

			}

		}

	}

	public void changeRank(UUID uuid, String setRank, boolean announce, CommandSender sender){

		Player p = Bukkit.getPlayer(uuid);

		if((ranks)this.playerRank.get(uuid) != null){

			String r = this.playerStatRank.get(p.getUniqueId());

			if(r != null){

				List<String> previousPlayers = getConfig().getStringList("Permissions." + r.toString() + ".Players");

				previousPlayers.remove(p.getName());

				getConfig().set("Permissions." + r.toString() + ".Players", previousPlayers);

				saveConfig();

				this.playerStatRank.remove(p.getUniqueId());

			}

		} else {

			if(announce){

				sender.sendMessage(cc("&7The players rank &4could not be changed to&7: &f") + setRank + cc("&7!"));

			}

			return;

		}

		if(setRank == null){

			if(announce){

				sender.sendMessage(cc("&7The players rank &4could not be changed to&7: &f") + setRank + cc("&7!"));

			}

			return;

		}

		for(String groups : getConfig().getConfigurationSection("Permissions").getKeys(false)){

			if(setRank.equals(groups)){

				if(setRank != null){

					List<String> players = getConfig().getStringList("Permissions." + groups + ".Players");

					players.add(p.getName());

					getConfig().set("Permissions." + groups + ".Players", players);
					saveConfig();

					if(p != null){
						if(p.isOnline()){

							this.rank.remove(uuid);

							this.rank.put(p.getUniqueId(), getConfig().getString("Permissions." + groups + ".Prefix"));

							this.playerStatRank.put(p.getUniqueId(), setRank);

						}

						for(String permissions : getConfig().getStringList("Permissions." + groups + ".Permissions")){

							PermissionAttachment attachment = p.addAttachment(this);

							this.playerPermissions.put(p.getUniqueId(), attachment);

							attachment.setPermission(permissions, true);

						}
					}

					if(announce){

						sender.sendMessage(cc("&7The players rank &2has been changed to&7: &f") + setRank + cc("&7!"));

					}

				}

			} 

		}

	}

	public void suffixSetter(){

		for(String groups : getConfig().getConfigurationSection("Suffixes").getKeys(false)){

			for(String players : getConfig().getStringList("Suffixes." + groups + ".Players")){

				Player p = Bukkit.getServer().getPlayer(players);

				if(p != null){
					if(p.isOnline()){

						this.suffix.put(p.getUniqueId(), getConfig().getString("Suffixes." + groups + ".Suffix"));
						this.playerStatSuffix.put(p.getUniqueId(), groups);

					}

				}

			}

		}

	}

	public void permissionSetter(){

		for(String groups : getConfig().getConfigurationSection("Permissions").getKeys(false)){

			for(String players : getConfig().getStringList("Permissions." + groups + ".Players")){

				Player p = Bukkit.getServer().getPlayer(players);

				if(p != null){
					if(p.isOnline()){

						this.rank.put(p.getUniqueId(), getConfig().getString("Permissions." + groups + ".Prefix"));
						this.playerStatRank.put(p.getUniqueId(), groups);

					}

					for(String permissions : getConfig().getStringList("Permissions." + groups + ".Permissions")){

						PermissionAttachment attachment = p.addAttachment(this);

						this.playerPermissions.put(p.getUniqueId(), attachment);

						attachment.setPermission(permissions, true);

					}
				}

			}

		}

	}

	public void defaultRankSetter(Player p){

		ranks rank = (ranks)this.playerRank.get(p.getUniqueId());

		if((rank == null) || (rank.getRank() == null)){

			for(String groups : getConfig().getConfigurationSection("Permissions").getKeys(false)){

				if(getConfig().getBoolean("Permissions." + groups + ".Default") == true){

					rank.setRank(getConfig().getString("Permissions." + groups + ".Prefix"));
					this.rank.put(p.getUniqueId(), rank.getRank());
					this.playerStatRank.put(p.getUniqueId(), groups);

					for(String permissions : getConfig().getStringList("Permissions." + groups + ".Permissions")){

						PermissionAttachment attachment = p.addAttachment(this);

						this.playerPermissions.put(p.getUniqueId(), attachment);

						attachment.setPermission(permissions, true);

					}

				}

			}

		}

	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event){

		Player p = event.getPlayer();

		ranks rank = (ranks)this.playerRank.get(p.getUniqueId());

		String msg = event.getMessage();

		if((rank == null) || (rank.getRank() == null)){

			event.setFormat(cc(p.getName() + "&f : " + msg));

			return;

		}

		if((rank.getSuffix() == null) || rank.getSuffix() == ""){

			event.setFormat(cc(rank.getRank() + " " + p.getName() + "&f : " + msg));

			//p.setDisplayName(cc(rank.getRank() +" " +  p.getName() + "&f"));

		} else {

			event.setFormat(cc(rank.getRank() + " " + p.getName() + " " + rank.getSuffix() + "&f : " + msg));

			//p.setDisplayName(cc(rank.getRank() + " " + p.getName() + " " + rank.getSuffix() + "&f"));

		}

	}

	public void createRank(String name, String prefix, boolean Default){

		if((name != null) && (prefix != null)){

			getConfig().set("Permissions." + name + "", null);

			saveConfig();

			List<String> pEmpty = new ArrayList<>();

			pEmpty.clear();

			getConfig().set("Permissions." + name + ".Default", Default);
			getConfig().set("Permissions." + name + ".Permissions", pEmpty);
			getConfig().set("Permissions." + name + ".Prefix", prefix);
			getConfig().set("Permissions." + name + ".Players", pEmpty);

			saveConfig();

		}

	}

	public void createSuffix(String name, String suffix){

		if((name != null) && (suffix != null)){

			getConfig().set("Permissions." + name + "", null);

			saveConfig();

			List<String> pEmpty = new ArrayList<>();

			pEmpty.clear();

			getConfig().set("Suffixes." + name + ".Players", pEmpty);
			getConfig().set("Suffixes." + name + ".Suffix", suffix);

			saveConfig();

		}

	}

	private void universalPermissions(){

		for(String permissions : getConfig().getStringList("Universal.Permissions")){

			if(getConfig().getBoolean("Universal.enabled")){

				for(Player p : Bukkit.getServer().getOnlinePlayers()){

					PermissionAttachment attachment = p.addAttachment(this);

					this.playerPermissions.put(p.getUniqueId(), attachment);

					attachment.setPermission(permissions, true);

				}

			}

		}

	}

	public void addPermission(String name, String permission){
		
		List<String> permissions = getConfig().getStringList("Permissions." + name + ".Permissions");
		
		permissions.add(permission);
		
		getConfig().set("Permissions." + name + ".Permissions", permissions);
		
		saveConfig();
		
	}
	
}




