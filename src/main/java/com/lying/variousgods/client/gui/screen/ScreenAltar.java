package com.lying.variousgods.client.gui.screen;

import com.lying.variousgods.client.gui.menu.MenuAltar;
import com.lying.variousgods.network.PacketHandler;
import com.lying.variousgods.network.PacketPraying;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class ScreenAltar<T extends MenuAltar> extends Screen implements MenuAccess<MenuAltar>
{
	protected final T altarMenu;
	
	public ScreenAltar(T altarIn, Inventory inv, Component displayName)
	{
		super(displayName);
		this.altarMenu = altarIn;
	}
	
	public MenuAltar getMenu()
	{
		return this.altarMenu;
	}
	
	public void onClose()
	{
		this.minecraft.player.closeContainer();
		super.onClose();
	}
	
	public boolean isPauseScreen() { return false; }
	
	protected void closeScreen()
	{
		this.minecraft.player.closeContainer();
        PacketHandler.sendToServer(new PacketPraying(this.minecraft.player.getUUID()));
	}
}
