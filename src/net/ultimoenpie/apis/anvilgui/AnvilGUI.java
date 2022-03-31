package net.ultimoenpie.apis.anvilgui;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public class AnvilGUI {

	private static VersionWrapper WRAPPER = new VersionMatcher().match();

	private final Plugin plugin;
	private final Player player;
	private String inventoryTitle;
	private ItemStack insert;
	private final boolean preventClose;
	private final Consumer<Player> closeListener;
	private final BiFunction<Player, String, Response> completeFunction;
	private int containerId;
	private Inventory inventory;
	private final ListenUp listener = new ListenUp();
	private boolean open;
	
	private AnvilGUI(
			Plugin plugin,
			Player player,
			String inventoryTitle,
			String itemText,
			ItemStack insert,
			boolean preventClose,
			Consumer<Player> closeListener,
			BiFunction<Player, String, Response> completeFunction
	) {
		this.plugin = plugin;
		this.player = player;
		this.inventoryTitle = inventoryTitle;
		this.insert = insert;
		this.preventClose = preventClose;
		this.closeListener = closeListener;
		this.completeFunction = completeFunction;

		if(itemText != null) {
			if(insert == null) {
				this.insert = new ItemStack(Material.PAPER);
			}

			ItemMeta paperMeta = this.insert.getItemMeta();
			paperMeta.setDisplayName(itemText);
			this.insert.setItemMeta(paperMeta);
		}

		openInventory();
	}

	private void openInventory() {
		WRAPPER.handleInventoryCloseEvent(player);
		WRAPPER.setActiveContainerDefault(player);
		Bukkit.getPluginManager().registerEvents(listener, plugin);
		final Object container = WRAPPER.newContainerAnvil(player, inventoryTitle);
		inventory = WRAPPER.toBukkitInventory(container);
		inventory.setItem(Slot.INPUT_LEFT, this.insert);
		containerId = WRAPPER.getNextContainerId(player, container);
		WRAPPER.sendPacketOpenWindow(player, containerId, inventoryTitle);
		WRAPPER.setActiveContainer(player, container);
		WRAPPER.setActiveContainerId(container, containerId);
		WRAPPER.addActiveContainerSlotListener(container, player);
		open = true;
	}

	public void closeInventory() {
		if (!open) {
			return;
		}
		open = false;
		WRAPPER.handleInventoryCloseEvent(player);
		WRAPPER.setActiveContainerDefault(player);
		WRAPPER.sendPacketCloseWindow(player, containerId);
		HandlerList.unregisterAll(listener);
		if(closeListener != null) {
			closeListener.accept(player);
		}
	}

	public Inventory getInventory() {
		return inventory;
	}

	private class ListenUp implements Listener {

		@EventHandler
		public void onInventoryClick(InventoryClickEvent event) {
			if (
				event.getInventory().equals(inventory) && (
					(event.getRawSlot() < 3) ||
				   	(event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) ||
				   	((event.getRawSlot() < 3) && ((event.getAction().equals(InventoryAction.PLACE_ALL)) || (event.getAction().equals(InventoryAction.PLACE_ONE)) || (event.getAction().equals(InventoryAction.PLACE_SOME)) || (event.getCursor() != null)))
				)
			){
				event.setCancelled(true);
				final Player clicker = (Player) event.getWhoClicked();
				if (event.getRawSlot() == Slot.OUTPUT) {
					final ItemStack clicked = inventory.getItem(Slot.OUTPUT);
					if (clicked == null || clicked.getType() == Material.AIR) return;

					final Response response = completeFunction.apply(clicker, clicked.hasItemMeta() ? clicked.getItemMeta().getDisplayName() : "");
					if(response.getText() != null) {
						final ItemMeta meta = clicked.getItemMeta();
						meta.setDisplayName(response.getText());
						clicked.setItemMeta(meta);
						inventory.setItem(Slot.INPUT_LEFT, clicked);
					} else {
						closeInventory();
					}
				}
			}
		}

		@EventHandler
		public void onInventoryDrag(InventoryDragEvent event) {
			if (event.getInventory().equals(inventory)) {
				for (int slot : Slot.values()) {
					if (event.getRawSlots().contains(slot)) {
						event.setCancelled(true);
						break;
					}
				}
			}
		}

		@EventHandler
		public void onInventoryClose(InventoryCloseEvent event) {
			if (open && event.getInventory().equals(inventory)) {
				closeInventory();
				if(preventClose) {
					Bukkit.getScheduler().runTask(plugin, AnvilGUI.this::openInventory);
				}
			}
		}
	}

	public static class Builder {
		private Consumer<Player> closeListener;
		private boolean preventClose = false;
		private BiFunction<Player, String, Response> completeFunction;
		private Plugin plugin;
		private String title = "Repair & Name";
		private String itemText = "";
		private ItemStack item;

		public Builder preventClose() {
			preventClose = true;
			return this;
		}

		public Builder onClose(Consumer<Player> closeListener) {
			Validate.notNull(closeListener, "closeListener cannot be null");
			this.closeListener = closeListener;
			return this;
		}

		public Builder onComplete(BiFunction<Player, String, Response> completeFunction) {
			Validate.notNull(completeFunction, "Complete function cannot be null");
			this.completeFunction = completeFunction;
			return this;
		}

		public Builder plugin(Plugin plugin) {
			Validate.notNull(plugin, "Plugin cannot be null");
			this.plugin = plugin;
			return this;
		}

		public Builder text(String text) {
			Validate.notNull(text, "Text cannot be null");
			this.itemText = text;
			return this;
		}

		public Builder title(String title) {
			Validate.notNull(title, "title cannot be null");
			this.title = title;
			return this;
		}

		public Builder item(ItemStack item) {
			Validate.notNull(item, "item cannot be null");
			this.item = item;
			return this;
		}

		public AnvilGUI open(Player player) {
			
			return new AnvilGUI(plugin, player, title, itemText, item, preventClose, closeListener, completeFunction);
		}

	}

	public static class Response {

		private final String text;

		private Response(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}
		public static Response close() {
			return new Response(null);
		}

		public static Response text(String text) {
			return new Response(text);
		}

	}
	
	public static class Slot {

		private static final int[] values = new int[] {Slot.INPUT_LEFT, Slot.INPUT_RIGHT, Slot.OUTPUT};
		public static final int INPUT_LEFT = 0;
		public static final int INPUT_RIGHT = 1;
		public static final int OUTPUT = 2;
		public static int[] values() {
			return values;
		}
	}

}