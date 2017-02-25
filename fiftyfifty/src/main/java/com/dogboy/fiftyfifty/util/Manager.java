package com.dogboy.fiftyfifty.util;

/**
 * all rights reserved Copyright (c) 2016 dogboy602k
 */

import com.dogboy.fiftyfifty.main.FiftyFifty;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Manager implements Listener {
    private Economy economy = null;
    private List<BetRequest> bet;
    private FiftyFifty plugin;
    private HashMap<UUID, UUID> requests;
    private Player p;
    private int a=0;

    public Manager(FiftyFifty plugin) {
        this.bet = new ArrayList<BetRequest> ();
        this.plugin = plugin;
        this.requests = new HashMap<UUID, UUID> ();
        ;
    }

    public boolean hasSentRequest(UUID requestSender, UUID accepter) {

        for (BetRequest betRequest : bet) {
            if (betRequest.getSender () == requestSender && betRequest.getRecipient () == accepter) {
                return true;
            }
        }
        return false;
    }

    public boolean hasEnoughMoney(Player player, double amount) {
        if (plugin.getEconomy ().getBalance ( player.getName () ) >= amount) {
            return true;
        }
        return false;
    }

    public void sendRequest(Player sender, Player recipient, int betAmount) {

        UUID senderUUID = sender.getUniqueId ();
        UUID recipientUUID = recipient.getUniqueId ();
        String recipientName = recipient.getName ();
        String senderName = sender.getName ();

        if (hasSentRequest ( senderUUID, recipientUUID )) {
            Util.sendMsg ( sender, "You have send a requests to that player!" );
            return;
        }

        if (!(sender instanceof Player)) {
            Util.sendMsg ( sender, ChatColor.RED + "Error: Player only" );
            return;
        }
        if (!hasEnoughMoney ( sender, betAmount ) || !hasEnoughMoney ( recipient, betAmount ) && !hasEnoughMoney ( recipient, betAmount ) || !hasEnoughMoney ( sender, betAmount )) {

            Util.sendMsg ( sender, ChatColor.RED + " Error not enough cash" );
            return;

        }

        if (senderName.equals ( recipientName )) {
            Util.sendMsg ( sender, ChatColor.RED + "You may not bet against  your self" );
            return;
        }
        Util.sendMsg ( sender, ChatColor.GOLD + "You have send a betting request to " + ChatColor.AQUA + recipientName + ChatColor.GREEN + " $" + betAmount );
        Util.sendMsg ( recipient, ChatColor.GOLD + "You have received a betting request from  " + ChatColor.AQUA
                + senderName + ChatColor.GOLD + " For " + ChatColor.GREEN + " $" + betAmount + ChatColor.GOLD + " use /fifty accept or deny <player> to accept the request or deny." );


        this.bet.add ( new BetRequest ( senderUUID, recipientUUID, betAmount ) );

        //Sendconsoledebug
    }

    public void removeBetRequest(BetRequest betRequest) {

        this.bet.remove ( betRequest );
    }

    public BetRequest getBetRequest(UUID sender, UUID target) {
        for (BetRequest betRequest : bet) {
            if (betRequest.getSender () == sender && betRequest.getRecipient () == target) {
                return betRequest;
            }
        }
        return null;
    }

    @SuppressWarnings("deprecation")

    public void denyRequest(Player denyer, Player requestSender) {

        UUID requestSenderUUID = requestSender.getUniqueId ();

        UUID accepterUUID = denyer.getUniqueId ();
        String rName = requestSender.getName ();
        String dName = denyer.getName ();

        if (hasSentRequest ( requestSenderUUID, accepterUUID )) {
            BetRequest request = this.getBetRequest ( requestSenderUUID, accepterUUID );
            int betAmount = request.getBet ();
            this.removeBetRequest ( request );
            Util.sendMsg ( denyer, "You have denied the request sent by: " + ChatColor.AQUA + rName );
            Util.sendMsg ( requestSender, "Your request has been denied by " + ChatColor.AQUA + dName );
            return;
        } else {
            Util.sendMsg ( denyer, ChatColor.RED + "You do no have a request" );
            return;
        }

    }

    public void acceptRequest(final Player accepter, final Player requestSender) {
        UUID requestSenderUUID = requestSender.getUniqueId ();

        UUID accepterUUID = accepter.getUniqueId ();
        String rName = requestSender.getName ();
        String aName = accepter.getName ();


        if (hasSentRequest ( requestSenderUUID, accepterUUID )) {
            BetRequest request = this.getBetRequest ( requestSenderUUID, accepterUUID );
            int betAmount = request.getBet ();
            this.removeBetRequest ( request );

            String hort = pickColor();

            if (hort.equals("tails")) {
                roulleteT ( requestSender );
                roulleteT ( accepter );
                new BukkitRunnable()
                {
                    @Override
                    public void run() {
                        firework ( accepter );
                        Util.sendMsg ( accepter, "You have won!" );
                        Util.sendMsg ( requestSender, "You have lost!" );
                        Util.sendMsg ( accepter, ChatColor.GREEN + "$" + ChatColor.GREEN + betAmount + ChatColor.GOLD + " Has been added to your balance!" );
                        Util.sendMsg ( requestSender, ChatColor.GREEN + "$" + ChatColor.GREEN + betAmount + ChatColor.GOLD + " Has been deducted to your balance!" );
                        plugin.getEconomy ().depositPlayer ( aName, betAmount );
                        plugin.getEconomy ().withdrawPlayer ( rName, betAmount );
                        this.cancel ();
                    }
                }.runTaskTimer ( plugin, 300, 5 );

                return;

            }
            else if(hort.equals("heads")){
                roulleteH ( requestSender );
                roulleteH ( accepter );


                new BukkitRunnable()
                {
                    @Override
                    public void run() {
                        firework ( requestSender );
                        Util.sendMsg ( requestSender, "You have won!" );
                        Util.sendMsg ( accepter, "You have lost!" );
                        Util.sendMsg ( requestSender, ChatColor.GREEN + "$" + ChatColor.GREEN + betAmount + ChatColor.GOLD + " Has been added to your balance!" );
                        Util.sendMsg ( accepter, ChatColor.GREEN + "$" + ChatColor.GREEN + betAmount + ChatColor.GOLD + " Has been deducted to your balance!" );
                        plugin.getEconomy ().depositPlayer ( rName, betAmount );
                        plugin.getEconomy ().withdrawPlayer ( aName, betAmount );
                        this.cancel ();
                    }
                }.runTaskTimer ( plugin, 300, 5 );



                return;

            }


        }
        else {
            Util.sendMsg ( accepter, "You do not have a request from that player" );
        }
    }

    public void firework(Player player) {
        Firework fw = (Firework) player.getWorld ().spawnEntity ( player.getLocation (), EntityType.FIREWORK );
        FireworkMeta fwmeta = fw.getFireworkMeta ();
        FireworkEffect.Builder builder = FireworkEffect.builder ();
        builder.withTrail ();
        builder.withFlicker ();
        builder.withFade ( org.bukkit.Color.RED );
        builder.withColor ( org.bukkit.Color.WHITE );
        builder.withColor ( org.bukkit.Color.BLUE );
        builder.with ( FireworkEffect.Type.BALL_LARGE );
        fwmeta.addEffect ( builder.build () );
        fwmeta.setPower ( 1 );
        fw.setFireworkMeta ( fwmeta );
    }

    public ItemStack randomHeadGen(){
        ItemStack H = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta Heads = (SkullMeta) H.getItemMeta();
        Heads.setDisplayName(ChatColor.GREEN + "Heads ");
        Heads.setOwner("ryan_");
        H.setItemMeta(Heads);

        ItemStack T = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta Tails = (SkullMeta) T.getItemMeta();
        Tails.setDisplayName(ChatColor.GREEN + "Tails");
        Tails.setOwner("Pr0SkyNesis");
        T.setItemMeta(Tails);

        Random random = new Random();
        int number = random.nextInt(2 - 1 + 1) + 1;
        switch (number) {
            case 2:
                return H;
            default:
                return T;

        }
    }

    public int roulleteT(Player player){

        ItemStack T = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta Tails = (SkullMeta) T.getItemMeta();
        Tails.setDisplayName(ChatColor.GREEN + "Tails");
        Tails.setOwner("Pr0SkyNesis");
        T.setItemMeta(Tails);        ItemStack mid = new ItemStack( Material.BEACON);
        ItemStack blank = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0);

        Inventory loanInv = Bukkit.createInventory(null, 27, (ChatColor.GOLD + "Fifty Fifty"));
        loanInv.setItem(0, blank );
        loanInv.setItem(1, blank );
        loanInv.setItem(2, blank );
        loanInv.setItem(3, blank );
        loanInv.setItem(5, blank );
        loanInv.setItem(6, blank );
        loanInv.setItem(7, blank );
        loanInv.setItem(8, blank );
        loanInv.setItem(18, blank );
        loanInv.setItem(19, blank );
        loanInv.setItem(20, blank );
        loanInv.setItem(21, blank );
        loanInv.setItem(23, blank );
        loanInv.setItem(24, blank );
        loanInv.setItem(25, blank );
        loanInv.setItem(26, blank );
        loanInv.setItem(4, mid );
        loanInv.setItem(22, mid );
        ItemStack[] heads = new ItemStack[54];
        for (int i = 0; i < 54; i++) {
            heads[i] = randomHeadGen();
        }
        heads[48]= T;
        for (int i = 9; i < 18; i++) {
            loanInv.setItem(i, heads[i]);
        }
        new BukkitRunnable()
        {
            int c=0;
            @Override
            public void run() {
                loanInv.setItem(9, heads[(c+1)]);
                loanInv.setItem(10, heads[(c+2)]);
                loanInv.setItem(11, heads[(c+3)]);
                loanInv.setItem(12, heads[(c+4)]);
                loanInv.setItem(13, heads[(c+5)]);
                loanInv.setItem(14, heads[(c+6)]);
                loanInv.setItem(14, heads[(c+7)]);
                loanInv.setItem(15, heads[(c+8)]);
                loanInv.setItem(16, heads[(c+9)]);
                loanInv.setItem(17, heads[(c+10)]);
                c++;
                if(c==44){
                    this.cancel();
                }
            }
        }.runTaskTimer ( plugin, 20, 5 );
        player.openInventory(loanInv);
        return a++;


    }

    public int roulleteH(Player player){

        ItemStack H = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta Heads = (SkullMeta) H.getItemMeta();
        Heads.setDisplayName(ChatColor.GREEN + "Heads ");
        Heads.setOwner("ryan_");
        H.setItemMeta(Heads);        ItemStack mid = new ItemStack( Material.BEACON);
        ItemStack blank = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0);

        Inventory loanInv = Bukkit.createInventory(null, 27, (ChatColor.GOLD + "Fifty Fifty"));
        loanInv.setItem(0, blank );
        loanInv.setItem(1, blank );
        loanInv.setItem(2, blank );
        loanInv.setItem(3, blank );
        loanInv.setItem(5, blank );
        loanInv.setItem(6, blank );
        loanInv.setItem(7, blank );
        loanInv.setItem(8, blank );
        loanInv.setItem(18, blank );
        loanInv.setItem(19, blank );
        loanInv.setItem(20, blank );
        loanInv.setItem(21, blank );
        loanInv.setItem(23, blank );
        loanInv.setItem(24, blank );
        loanInv.setItem(25, blank );
        loanInv.setItem(26, blank );
        loanInv.setItem(4, mid );
        loanInv.setItem(22, mid );
        ItemStack[] colors = new ItemStack[54];
        for (int i = 0; i < 54; i++) {
            colors[i] = randomHeadGen();
        }
        colors[48]= H;
        for (int i = 9; i < 18; i++) {
            loanInv.setItem(i, colors[i]);
        }
        new BukkitRunnable()
        {
            int c=0;
            @Override
            public void run() {
                loanInv.setItem(9, colors[(c+1)]);
                loanInv.setItem(10, colors[(c+2)]);
                loanInv.setItem(11, colors[(c+3)]);
                loanInv.setItem(12, colors[(c+4)]);
                loanInv.setItem(13, colors[(c+5)]);
                loanInv.setItem(14, colors[(c+6)]);
                loanInv.setItem(14, colors[(c+7)]);
                loanInv.setItem(15, colors[(c+8)]);
                loanInv.setItem(16, colors[(c+9)]);
                loanInv.setItem(17, colors[(c+10)]);
                c++;
                if(c==44){
                    this.cancel();
                }
            }
        }.runTaskTimer ( plugin, 20, 5 );
        player.openInventory(loanInv);
        return a++;


    }

    public String pickColor() {
        Random random = new Random();
        int number = random.nextInt(100 - 1 + 1) + 1;

        if (number >= 1 && number <= 50) {
            return "heads";
            }
        else
            return "tails";
    }


    @EventHandler
    public void onClick(InventoryClickEvent e) {
                if (e.getInventory().getTitle().equals(ChatColor.GOLD + "Fifty Fifty")) {
                    for (int i = 0; i < 54; i++) {
                        if(e.getRawSlot()  == i) {
                            e.setCancelled(true);
                        }
                    }
                }

    }

}

