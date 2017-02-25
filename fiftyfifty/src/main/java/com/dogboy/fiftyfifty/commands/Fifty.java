package com.dogboy.fiftyfifty.commands;
/**
 * all rights reserved Copyright (c) 2016 dogboy602k
 */

import com.dogboy.fiftyfifty.main.FiftyFifty;
import com.dogboy.fiftyfifty.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by dogboy on 1/28/2016.
 */
public class Fifty implements CommandExecutor {

    private FiftyFifty plugin;

    public Fifty(FiftyFifty plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(args.length ==2  && args[1].equalsIgnoreCase("info") ) {
            Util.sendMsg (sender, ChatColor.GREEN+ " * Copyright (c) 2016 dogboy602k\n" +
                    " * Copyright © 2016 dogboy602k\n" +
                    "\n" +
                    " All rights reserved. No part of this publication may be reproduced, distributed, or transmitted in any form or by\n" +
                    " any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written\n" +
                    " permission of the publisher, except in the case of brief quotations embodied in critical reviews and certain other\n" +
                    " noncommercial uses permitted by copyright law. For permission requests, write to the publisher, addressed\n" +
                    " “Attention: Permissions Coordinator,” at the address below.\n" +
                    "\n " +
                    ChatColor.AQUA+"\nCreator of the plugins page: "+
                    ChatColor.GOLD+" https://www.spigotmc.org/members/dogboy60.32564/\n" +
                    "\n " +
                    "Version:1.2"  );
            return true;
        }

        if (args.length == 0 || args.length == 1 || args.length > 3) {
            Util.sendMsg((Player)sender, ChatColor.AQUA + "Usage /Fifty " + ChatColor.GOLD + "<username>" + ChatColor.GREEN + " <bet amount>");
            Util.sendMsg((Player)sender, ChatColor.AQUA + "Usage /Fifty accept" + ChatColor.GOLD + " <username>");
            Util.sendMsg((Player)sender, ChatColor.AQUA + "Usage /Fifty deny" + ChatColor.GOLD + " <username> ");
            Util.sendMsg((Player)sender, ChatColor.AQUA + "Usage /Fifty " + ChatColor.GOLD + " <username> " + ChatColor.AQUA + "info ");
            return true;
        }

        if(!(sender instanceof Player))  {
            Util.sendMsg(sender, ChatColor.RED + "Error: You cannot use this command as console!");
            return true;
        }
        Player commandUser = (Player) sender;

        if(args.length == 2 && args[0].equalsIgnoreCase("deny")) {
            String playerName = args[1];

            Player player = Bukkit.getPlayer(playerName);

            if(player != null) {
                plugin.getManager().denyRequest (commandUser, player);
            } else {
                Util.sendMsg((Player)sender, ChatColor.RED + "The player "+ ChatColor.GOLD + playerName + ChatColor.RED+ " is not online!" );
            }

            return true;
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("accept")) {
            String playerName = args[1];

            Player player = Bukkit.getPlayer(playerName);

            if(player != null) {


                plugin.getManager().acceptRequest(commandUser, player);
            } else {
                Util.sendMsg((Player)sender, ChatColor.RED + "The player "+ ChatColor.GOLD + playerName + ChatColor.RED+ " is not online!" );
            }

            return true;
        }


        if(args.length == 2) {
            String playerName = args[0];
            int betAmount = 0;
            Player player = Bukkit.getPlayer(playerName);


            try {
                betAmount = Integer.valueOf ( args[1]);
            } catch (NumberFormatException e) {
                Util.sendMsg ( sender ,ChatColor.RED+ "Error:  You must enter a number for this value" );
                return true;
            }
            if(betAmount <= 0 )
            {

                Util.sendMsg ( sender, ChatColor.RED+(" ERROR:  you must input a bet amount greater than 0") );
                return true;
            }

            if(player != null) {

                plugin.getManager().sendRequest(commandUser, player, betAmount);
                return true;
            } else {
                Util.sendMsg((Player)sender, ChatColor.RED + "The player "+ ChatColor.GOLD + playerName + ChatColor.RED+ " is not online!" );
                return true;
            }

        }

        Util.sendMsg( sender, ChatColor.RED + "Error: Unknown Command!" );
        return true;

    }

}

