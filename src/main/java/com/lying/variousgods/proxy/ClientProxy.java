package com.lying.variousgods.proxy;

import com.lying.variousgods.capabilities.PlayerData;
import com.lying.variousgods.client.gui.screen.ScreenAltarDeity;
import com.lying.variousgods.client.gui.screen.ScreenAltarStart;
import com.lying.variousgods.init.VGMenus;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.network.NetworkEvent;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy
{
	private static final Minecraft mc = Minecraft.getInstance();
	private static PlayerData localData = new PlayerData(null);
	
	public static void registerKeyMappings(RegisterKeyMappingsEvent event)
	{
		
	}
	
	public void clientInit()
	{
		MenuScreens.register(VGMenus.ALTAR_MENU.get(), ScreenAltarStart::new);
		MenuScreens.register(VGMenus.DEITY_MENU.get(), ScreenAltarDeity::new);
	}
	
	public void registerHandlers()
	{
		
	}
	
	public void onLoadComplete(FMLLoadCompleteEvent event)
	{
		
	}
	
	public Player getPlayerEntity(NetworkEvent.Context ctx){ return (ctx.getDirection().getReceptionSide().isClient() ? mc.player : super.getPlayerEntity(ctx)); }
	
	public PlayerData getPlayerData(Player playerIn)
	{
		localData.setPlayer(playerIn);
		return localData;
	}
}
