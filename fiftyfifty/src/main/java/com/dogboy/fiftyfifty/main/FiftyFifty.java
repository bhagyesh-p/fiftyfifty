package com.dogboy.fiftyfifty.main;

import com.dogboy.fiftyfifty.commands.Fifty;
import com.dogboy.fiftyfifty.util.Manager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by dogboy602k on 1/28/2016.
 * Copyright (c) 2016 dogboy602k
 * Copyright © 2016 dogboy602k

 All rights reserved. No part of this publication may be reproduced, distributed, or transmitted in any form or by
 any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written
 permission of the publisher, except in the case of brief quotations embodied in critical reviews and certain other
 noncommercial uses permitted by copyright law. For permission requests, write to the publisher, addressed
 “Attention: Permissions Coordinator,” at the address below.

 https://www.spigotmc.org/members/dogboy60.32564/

 Printed in the United States of America

 Version:1.0
 */
public class FiftyFifty extends JavaPlugin {

    private Manager manager;
    private Economy economy = null;

    @Override
    public void onEnable() {

        setupEconomy ();
        this.manager = new Manager(this);
        getCommand("fifty").setExecutor(new Fifty(this));

        Bukkit.getPluginManager().registerEvents(  manager, this);

    }

    @Override
    public void onDisable() {

    }

    public Manager getManager() {
        return manager;
}

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);

    }


    public Economy getEconomy() {
        return economy;
    }

}



