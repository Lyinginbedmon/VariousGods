package com.lying.variousgods.client;

import com.lying.variousgods.VariousGods;
import com.lying.variousgods.capabilities.PlayerData;
import com.lying.variousgods.client.gui.screen.ScreenAltarDeity;
import com.lying.variousgods.client.gui.screen.ScreenAltarStart;
import com.lying.variousgods.init.VGBlocks;
import com.lying.variousgods.init.VGMenus;
import com.lying.variousgods.utility.bus.ClientBus;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.network.NetworkEvent;

@OnlyIn(Dist.CLIENT)
public class ClientSetupEvents
{
	private static final Minecraft mc = Minecraft.getInstance();
	private static PlayerData localData = new PlayerData(null);
	
    @SubscribeEvent
	public static void registerKeyMappings(final RegisterKeyMappingsEvent event)
	{
    	
	}
	
    @SuppressWarnings("removal")
	@SubscribeEvent
	public static void clientInit(final FMLClientSetupEvent event)
	{
		event.enqueueWork(() ->
		{
    		ItemBlockRenderTypes.setRenderLayer(VGBlocks.HOURGLASS_ALTAR.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(VGBlocks.BONE_ALTAR.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(VGBlocks.CRYSTAL_ALTAR.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(VGBlocks.BLAZE_ALTAR.get(), RenderType.cutout());
    		
			MenuScreens.register(VGMenus.ALTAR_MENU.get(), ScreenAltarStart::new);
			MenuScreens.register(VGMenus.DEITY_MENU.get(), ScreenAltarDeity::new);
			
        	MinecraftForge.EVENT_BUS.register(ClientBus.class);
		});
	}
    
    @SubscribeEvent
	public static void onLoadComplete(final FMLLoadCompleteEvent event)
	{
    	
	}
    
	@SubscribeEvent
	public static void registerColorHandlersBlock(final RegisterColorHandlersEvent.Block event)
	{
		event.register((blockState, tintGetter, pos, layer) -> {
	         return tintGetter != null && pos != null ? BiomeColors.getAverageWaterColor(tintGetter, pos) : -1;
	      }, VGBlocks.FOUNTAIN_ALTAR.get());
		event.register((blockState, tintGetter, pos, layer) -> {
	         return 16711680;
	      }, VGBlocks.BLOOD_ALTAR_LIQUID.get());
	}
	
    @SubscribeEvent
	public static void registerOverlayEvent(final RegisterGuiOverlaysEvent event)
	{
    	// TODO Implement proper god opinion display
		VariousGods.LOG.info("Registering overlays");
//		event.registerAboveAll("god_status", new OverlayGodStatus());
	}
	
	public static Player getPlayerEntity(NetworkEvent.Context ctx){ return (ctx.getDirection().getReceptionSide().isClient() ? mc.player : ctx.getSender()); }
	
	public static PlayerData getPlayerData(Player playerIn)
	{
		localData.setPlayer(playerIn);
		return localData;
	}
	
	public static PlayerData getLocalData()
	{
		return getPlayerData(mc.player);
	}
}
