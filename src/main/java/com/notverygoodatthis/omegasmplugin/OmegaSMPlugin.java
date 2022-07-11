package com.notverygoodatthis.omegasmplugin;

import dev.dbassett.skullcreator.SkullCreator;
import net.kyori.adventure.text.Component;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Random;

public final class OmegaSMPlugin extends JavaPlugin implements Listener {
    public static int MAX_LIVES = 5;
    public static String LIFE_ITEM_NAME = "§a§lLife";
    public static String RESURRECTION_FRAGMENT_NAME = "§b§lRessurection fragment";
    public static String REVIVAL_ITEM_NAME = "§4§oRevive item";
    ArrayList<String> availableItems = new ArrayList<>();

    void printWelcome() {
        PluginDescriptionFile file = this.getDescription();
        getLogger().info("|------------------------------");
        getLogger().info("| Enabling the Omega SMP plugin");
        getLogger().info("| Version " + file.getVersion());
        getLogger().info("|------------------------------");
    }

    @Override
    public void onEnable() {
        printWelcome();
        
        registerRecipes();
        registerCommands();

        Bukkit.getPluginManager().registerEvents(this, this);

        this.saveDefaultConfig();
        FileConfiguration config = this.getConfig();
        availableItems = (ArrayList<String>) config.getList("available-items");
    }

    void registerRecipes() {
        Bukkit.addRecipe(revivalItemRecipe());
    }

    void registerCommands() {
        getCommand("omegarevive").setExecutor(new RevivalCommand());
        getCommand("omegarevive").setTabCompleter(new RevivalCommand());
        getCommand("deposit").setExecutor(new DepositCommand());
        getCommand("omegareset").setExecutor(new OmegaResetCommand());
        getCommand("omegaset").setExecutor(new OmegaSetCommand());
        getCommand("omegakit").setExecutor(new OmegaKitCommand());
    }

    public static ItemStack getLife(int amount) {
        ItemStack life = new ItemStack(Material.POPPED_CHORUS_FRUIT, amount);
        ItemMeta meta = life.getItemMeta();
        meta.displayName(Component.text(LIFE_ITEM_NAME));
        life.setItemMeta(meta);
        return life;
    }

    public static ItemStack getRessurectionFragment(int amount) {
        ItemStack ressurectionItem = new ItemStack(Material.COCOA_BEANS, amount);
        ItemMeta meta = ressurectionItem.getItemMeta();
        meta.displayName(Component.text(RESURRECTION_FRAGMENT_NAME));
        ressurectionItem.setItemMeta(meta);
        return ressurectionItem;
    }

    public static ItemStack getRevivalItem(int amount) {
        ItemStack revivalItem = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQ2MDdhZThhNmY5Mzc0MmU4ZWIxNmEwZjg2MjY1OWUzMDg3NjEwMTlhMzk3NzIyYzFhZmU4NGIxNzlkMWZhMiJ9fX0=");
        ItemMeta meta = revivalItem.getItemMeta();
        meta.displayName(Component.text(REVIVAL_ITEM_NAME));
        revivalItem.setItemMeta(meta);
        return revivalItem;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getPlayer();
        Entity killer = player.getKiller();
        if(killer instanceof Player) {
            if(player.getStatistic(Statistic.DEATHS) > MAX_LIVES) {
                player.getWorld().dropItemNaturally(player.getLocation(), getRessurectionFragment(1));
                getServer().broadcast(Component.text(player.getName() + " has lost all of their lives. They will be banned until someone revives them."));
                String banMsg = "You have lost your last life to " + killer.getName() + ". Thank you for playing on Omega SMP.";
                Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), banMsg, null, "Omega SMP plugin");
                player.kick(Component.text(banMsg));
                Random random = new Random();
                int randIndex = random.nextInt();
                ItemStack randItem = new ItemStack(Material.matchMaterial(availableItems.get(randIndex)));
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
                player.getWorld().dropItemNaturally(player.getLocation(), randItem);
            }
        } else {
            player.setStatistic(Statistic.DEATHS, player.getStatistic(Statistic.DEATHS) - 1);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if(e.getEntity() instanceof Creeper) {
            e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), new ItemStack(Material.GUNPOWDER, 5));
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack itemInHand = e.getPlayer().getInventory().getItemInMainHand();
            if(itemInHand.getType() == getLife(1).getType() && itemInHand.getItemMeta().displayName().equals(LIFE_ITEM_NAME)) {
                e.getPlayer().setStatistic(Statistic.DEATHS, e.getPlayer().getStatistic(Statistic.DEATHS) - 1);
                int playerLives = MAX_LIVES - e.getPlayer().getStatistic(Statistic.DEATHS);
                e.getPlayer().sendMessage(ChatColor.AQUA + "§lYou have consumed a life item. You now have " + playerLives + "§l lives.");
                e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
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
}
