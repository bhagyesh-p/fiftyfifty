package com.dogboy.fiftyfifty.util;

import com.dogboy.fiftyfifty.main.FiftyFifty;
import net.md_5.bungee.api.ChatColor;

/**
 * Created by dogboy on 7/22/2016.
 */
public class MessageMessanger {
    private FiftyFifty plugin;


    public MessageMessanger(FiftyFifty plugin) {
        this.plugin = plugin;

    }

    public String SendToSelf() {
        String toSender = (ChatColor.RED + "You may not bet against  your self");
        return toSender;
    }
}
