package com.lying.variousgods.client.gui.menu;

import com.lying.variousgods.init.VGMenus;
import com.lying.variousgods.network.PacketHandler;
import com.lying.variousgods.network.PacketOpenDeityMenu;
import com.lying.variousgods.reference.Reference;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeMod;

public class MenuAltarStart extends MenuAltar
{
	public MenuAltarStart(int containerId, Inventory inv)
	{
		this(containerId, (HitResult)null);
	}
	
	public MenuAltarStart(int containerId, HitResult hitResult)
	{
		super(VGMenus.ALTAR_MENU.get(), containerId, hitResult);
	}
	
	public void openDeityMenu(Player player)
	{
		PacketHandler.sendToServer(new PacketOpenDeityMenu(player.getUUID()));
	}
	
	public AbstractContainerMenu createMenu(int containerId, Inventory inv, Player player)
	{
		double range = (double)player.getAttributeValue(ForgeMod.REACH_DISTANCE.get());
		return new MenuAltarDeity(containerId, player.pick(range, 0F, false));
	}
	
	public Component getDisplayName() { return Component.translatable("container."+Reference.ModInfo.MOD_ID+".altar_god"); }
}
