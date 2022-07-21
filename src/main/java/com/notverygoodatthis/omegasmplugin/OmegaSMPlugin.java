package com.notverygoodatthis.omegasmplugin;

              //*************************************\\
             //***********Omega SMP plugin************\\
            //*******Made by NotVeryGoodAtThis*********\\
           //**Give credit if you use my code anywhere**\\
          //***See credits.txt to see the tools I used***\\
         //The comments should make the code easy to read.\\
        //***Have fun! Tweak the code however you'd like***\\
       //Once you're happy, run the project in IntelliJ IDEA\\
      //It will create a JAR file in the folder named target!\\
     //*******************************************************\\
    //*****I'll try to make all of my plugins open-source.*****\\
   //****I just like the idea of people taking a deeper look****\\
  //*************************************************************\\
 //*****I hope you have a lot of fun working with the code :)*****\\
//*****************************************************************\\

import dev.dbassett.skullcreator.SkullCreator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Random;

public final class OmegaSMPlugin extends JavaPlugin implements Listener {
    //Constants that are used in other classes
    public static int MAX_LIVES = 5;
    public static String LIFE_ITEM_NAME = "§a§lLife";
    public static String RESURRECTION_FRAGMENT_NAME = "§b§lRessurection fragment";
    public static String REVIVAL_ITEM_NAME = "§4§oRevive item";
    public static String SPEED_ITEM_NAME = "§f§lSpeed";
    ArrayList<String> availableItems = new ArrayList<>();

    void printWelcome() {
        //Prints a welcome message with the version of the plugin
        PluginDescriptionFile file = this.getDescription();
        getLogger().info("|------------------------------");
        getLogger().info("| Enabling the Omega SMP plugin");
        getLogger().info("| Version " + file.getVersion());
        getLogger().info("|------------------------------");
    }

    @Override
    public void onEnable() {
        printWelcome();

        //Registers all of the recipes and commands
        registerRecipes();
        registerCommands();

        //Registers the event listeners
        Bukkit.getPluginManager().registerEvents(this, this);

        this.saveDefaultConfig();
        FileConfiguration config = this.getConfig();
        //Loads the available items into an Arraylist
        availableItems = (ArrayList<String>) config.getList("available-items");
    }

    void registerRecipes() {
        Bukkit.addRecipe(revivalItemRecipe());
        Bukkit.addRecipe(speedRecipe());
    }

    void registerCommands() {
        getCommand("omegarevive").setExecutor(new RevivalCommand());
        getCommand("omegarevive").setTabCompleter(new RevivalCommand());
        getCommand("deposit").setExecutor(new DepositCommand());
        getCommand("omegareset").setExecutor(new OmegaResetCommand());
        getCommand("omegaset").setExecutor(new OmegaSetCommand());
        getCommand("omegakit").setExecutor(new OmegaKitCommand());
    }

    //Getter for life items
    public static ItemStack getLife(int amount) {
        ItemStack life = new ItemStack(Material.POPPED_CHORUS_FRUIT, amount);
        ItemMeta meta = life.getItemMeta();
        meta.displayName(Component.text(LIFE_ITEM_NAME));
        life.setItemMeta(meta);
        return life;
    }

    //Getter for resurrection fragments
    public static ItemStack getRessurectionFragment(int amount) {
        ItemStack ressurectionItem = new ItemStack(Material.COCOA_BEANS, amount);
        ItemMeta meta = ressurectionItem.getItemMeta();
        meta.setDisplayName(RESURRECTION_FRAGMENT_NAME);
        ressurectionItem.setItemMeta(meta);
        return ressurectionItem;
    }

    //Getter for revival items
    public static ItemStack getRevivalItem(int amount) {
        ItemStack revivalItem = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQ2MDdhZThhNmY5Mzc0MmU4ZWIxNmEwZjg2MjY1OWUzMDg3NjEwMTlhMzk3NzIyYzFhZmU4NGIxNzlkMWZhMiJ9fX0=");
        revivalItem.setAmount(amount);
        ItemMeta meta = revivalItem.getItemMeta();
        meta.displayName(Component.text(REVIVAL_ITEM_NAME));
        revivalItem.setItemMeta(meta);
        return revivalItem;
    }

    //Getter for speed items... Don't question it
    public static ItemStack getSpeed(int amount) {
        ItemStack speed = new ItemStack(Material.SUGAR, amount);
        ItemMeta meta = speed.getItemMeta();
        meta.setDisplayName(SPEED_ITEM_NAME);
        speed.setItemMeta(meta);
        return speed;
    }

    //Getter for legacy revival heads
    public static ItemStack getLegacyRevival(int amount) {
        ItemStack head = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQ2MDdhZThhNmY5Mzc0MmU4ZWIxNmEwZjg2MjY1OWUzMDg3NjEwMTlhMzk3NzIyYzFhZmU4NGIxNzlkMWZhMiJ9fX0=");
        ItemMeta meta = head.getItemMeta();
        meta.setDisplayName(REVIVAL_ITEM_NAME);
        head.setItemMeta(meta);
        return head;
    }

    //Updates a specific player's name in the tab list based on how many lives they have
    public static void updateTablistForPlayer(Player player) {
        int playerLives = MAX_LIVES - player.getStatistic(Statistic.DEATHS);
        String name = "[" + playerLives + "] " + player.getName();
        player.playerListName(Component.text(name));
    }

    //Listener for any player's death
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getPlayer();
        Entity killer = player.getKiller();
        if(killer instanceof Player) {
            //If the killer is a player, we log the dead player's death statistic
            getLogger().info(String.valueOf(player.getStatistic(Statistic.DEATHS)));
            //Then we update the dead player's tablist after one second. It didn't work instantly for some reason ¯\_(`-`)_/¯
            Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                @Override
                public void run() {
                    updateTablistForPlayer(player);
                }
            }, 20L);
            //If a player's deaths is equal or more than the max lives that means that they are completely dead, wiped off the server
            if(player.getStatistic(Statistic.DEATHS) >= MAX_LIVES) {
                //So we first drop a resurrection fragment on the ground
                player.getWorld().dropItemNaturally(player.getLocation(), getRessurectionFragment(1));
                //Then we dispatch a message alerting all of the players that someone's dead
                getServer().broadcast(Component.text(player.getName() + " has lost all of their lives. They will be banned until someone revives them."));
                String banMsg = "You have lost your last life to " + killer.getName() + ". Thank you for playing on Omega SMP.";
                //We ban the player, letting them know who took their last life.
                Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), banMsg, null, "Omega SMP plugin");
                player.kick(Component.text(banMsg));
                //Picks a random item from the availableItems list
                Random random = new Random();
                int randIndex = random.nextInt(availableItems.size());
                ItemStack randItem = new ItemStack(Material.matchMaterial(availableItems.get(randIndex)));
                //Very long switch statement to apply enchantments to all of the items
                switch (randItem.getType()) {
                    case NETHERITE_CHESTPLATE:
                    case NETHERITE_LEGGINGS:
                        randItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 6);
                        randItem.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
                        randItem.addUnsafeEnchantment(Enchantment.MENDING, 2);
                        break;
                    case NETHERITE_SWORD:
                        randItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
                        randItem.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 5);
                        randItem.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 5);
                        randItem.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
                        randItem.addUnsafeEnchantment(Enchantment.SWEEPING_EDGE, 4);
                        randItem.addUnsafeEnchantment(Enchantment.MENDING, 2);
                        break;
                    case NETHERITE_AXE:
                        randItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
                        randItem.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);
                        randItem.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 5);
                        randItem.addUnsafeEnchantment(Enchantment.MENDING, 2);
                        break;
                    case NETHERITE_HELMET:
                        randItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 6);
                        randItem.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
                        randItem.addUnsafeEnchantment(Enchantment.MENDING, 2);
                        randItem.addUnsafeEnchantment(Enchantment.OXYGEN, 5);
                        randItem.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);
                        break;
                    case NETHERITE_BOOTS:
                        randItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 6);
                        randItem.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
                        randItem.addUnsafeEnchantment(Enchantment.MENDING, 2);
                        randItem.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 6);
                        randItem.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 5);
                        break;

                }
                //Drops the OP item
                player.getWorld().dropItemNaturally(player.getLocation(), randItem);
            }
        } else {
            //If the player wasn't killed by another player, we simply reset their death statistic
            player.setStatistic(Statistic.DEATHS, player.getStatistic(Statistic.DEATHS) - 1);
        }
    }


    //Buffed creeper drop rates. You can remove this if you don't want it for personal use
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if(e.getEntity() instanceof Creeper) {
            e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), new ItemStack(Material.GUNPOWDER, 5));
        }
    }

    //PlayerInteractEvent for life items and speed
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = e.getPlayer();
            if(player.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
                //We store the currently held item in a variable
                ItemStack itemInHand = e.getPlayer().getInventory().getItemInMainHand();
                //If the item we're holding has a display name, we store it in a TextComponent
                TextComponent textComponent = (TextComponent) itemInHand.getItemMeta().displayName();
                //We compare the held item to a life
                if(itemInHand.getType() == getLife(1).getType() && textComponent.content().equals(LIFE_ITEM_NAME)) {
                    //If it's a life, we store the current player lives in a variable
                    int playerLives = MAX_LIVES - e.getPlayer().getStatistic(Statistic.DEATHS);
                    //Then we examine if the player already has max lives
                    if(playerLives != 5) {
                        //If they do, then we subtract one death from their statistics
                        e.getPlayer().setStatistic(Statistic.DEATHS, e.getPlayer().getStatistic(Statistic.DEATHS) - 1);
                        playerLives++;
                        //then we send a message to the player telling them that they've consumed a life item and telling them their current life count
                        e.getPlayer().sendMessage(ChatColor.AQUA + "§lYou have consumed a life item. You now have " + playerLives + "§l lives.");
                        //we remove the used life item
                        e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
                        //And we update the tablist name
                        updateTablistForPlayer(e.getPlayer());
                    } else {
                        //if the player has the max amount of lives then we tell them that they do and do nothing
                        e.getPlayer().sendMessage(ChatColor.AQUA + "You already have the maximum amount of lives");
                    }
                }

                //Speed item logic
                if(itemInHand.getType() == getSpeed(1).getType() && itemInHand.getItemMeta().getDisplayName().equals(SPEED_ITEM_NAME)) {
                    itemInHand.setAmount(itemInHand.getAmount() - 1);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 12000, 3));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 12000, 2));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 3000, 1));
                }
            }
        }
    }

    @Override
    public void onDisable() {
        PluginDescriptionFile file = this.getDescription();
        getLogger().info("|-------------------------------");
        getLogger().info("| Disabling the Omega SMP plugin");
        getLogger().info("| Version " + file.getVersion());
        getLogger().info("|-------------------------------");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        updateTablistForPlayer(player);
    }

    public ShapedRecipe revivalItemRecipe() {
        ItemStack revivalItem = getRevivalItem(1);
        NamespacedKey key = new NamespacedKey(this, "player_head");
        ShapedRecipe recipe = new ShapedRecipe(key, revivalItem);
        recipe.shape("TDT", "DRD", "TDT");
        recipe.setIngredient('T', Material.TOTEM_OF_UNDYING);
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        recipe.setIngredient('R', new RecipeChoice.ExactChoice(getRessurectionFragment(1)));
        return recipe;
    }

    public ShapedRecipe speedRecipe() {
        ItemStack speed = getSpeed(1);
        NamespacedKey key = new NamespacedKey(this, "sugar");
        ShapedRecipe recipe = new ShapedRecipe(key, speed);
        recipe.shape("DDD", "DSD", "DDD");
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        recipe.setIngredient('S', Material.SUGAR);
        return recipe;
    }
}
