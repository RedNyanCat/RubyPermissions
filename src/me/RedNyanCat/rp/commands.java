package me.RedNyanCat.rp;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class commands implements CommandExecutor , Listener {

	private setup setup;

	public commands(setup setup){

		this.setup = setup;

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {

		if(cmd.getName().equalsIgnoreCase("RubyPermissions")){

			if(sender.hasPermission("RubyPermissions.change")){

				if(args.length >= 2){

					if(args[0].equalsIgnoreCase("addPerm")){

						if(sender.hasPermission("RubyPermissions.addPerm")){

							String permission = args[2];

							setup.addPermission(args[1], permission);

							return true;

						} else {

							if(sender instanceof Player){

								setup.npMsg((Player)sender);

							}

							return true;

						}

					}

				} else if(args.length == 3){

					if(args[0].equalsIgnoreCase("setRank") || args[0].equalsIgnoreCase("setSuffix")){

						String target = args[1];
						String to = args[2];

						Player p = Bukkit.getPlayer(target);

						try{

							if(p.isOnline()){

								if(args[0].equalsIgnoreCase("setRank")){

									setup.changeRank(p.getUniqueId(), to, true, sender);

								}

								else if(args[0].equalsIgnoreCase("setSuffix")){

									setup.changeSuffix(p.getUniqueId(), to, true, sender);

								}

							}

							return true;

						} catch(NullPointerException exception){

							sender.sendMessage(setup.cc("&4&lOops! Something went wrong! &7Try again and report this to the developer! Ignore this otherwise!"));

							return true;

						}

					}

				} else if(args.length >= 3){

					String name = args[1];
					String tag = args[2];

					if(args[0].equalsIgnoreCase("createRank")){

						boolean Default = Boolean.valueOf(args[3]);

						if(sender.hasPermission("RubyPermissions.createRank")){

							setup.createRank(name, tag, Default);

						} else {

							if(sender instanceof Player){

								setup.npMsg((Player)sender);

							}

						}

						return true;

					} 

					if(args[0].equalsIgnoreCase("createSuffix")){

						if(sender.hasPermission("RubyPermissions.createSuffix")){

							setup.createSuffix(name, tag);

						} else {

							if(sender instanceof Player){

								setup.npMsg((Player)sender);

							}

						}

						return true;

					}

				} else {

					sender.sendMessage(setup.cc("&4&lOops! You need to atleast have two arguments or you typed it wrong! &7Try again!"));

				}

			} else {

				if(sender instanceof Player){

					setup.npMsg((Player)sender);

				}

				return true;

			}

		} 

		return false;

	}

}
