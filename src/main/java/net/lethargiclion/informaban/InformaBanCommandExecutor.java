package net.lethargiclion.informaban;

/*
    This file is part of InformaBan.

    InformaBan is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    InformaBan is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with InformaBan.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;

import net.lethargiclion.informaban.enforcement.Kick;
import net.lethargiclion.informaban.persistence.Event;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InformaBanCommandExecutor implements CommandExecutor {

    private InformaBan plugin;

    public InformaBanCommandExecutor(InformaBan plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("kick")) return commandKick(sender, args);
        return false;
    }
    
    private boolean commandKick(CommandSender sender, String[] args ) {
        if(args.length == 1) sender.sendMessage(plugin.messages.getString("command.kick.reasonRequired"));
        if(args.length > 1) {
            Player victim = sender.getServer().getPlayer(args[1]);
            if(victim != null) {
                // Set up kick message
                String banReason = StringUtils.join(Arrays.copyOfRange(args, 2, args.length), ' ');

                plugin.getLogger().info(new MessageFormat(plugin.messages.getString("command.kick.consoleLog"), InformaBan.getLocale()).format(new Object[]{sender.getName(), victim.getName()}));
                
                // Do the kick and record it
                Kick k = new Kick();
                k.enforce(plugin.messages, victim, sender, banReason);
                plugin.getDatabase().insert(k);
            }
            else sender.sendMessage(plugin.messages.getString("error.playerNotFound"));
            return true;
        }
        return false;
    }
}
