package com.lying.variousgods;

import org.slf4j.Logger;

import com.lying.variousgods.client.ClientSetupEvents;
import com.lying.variousgods.commands.CommandDeity;
import com.lying.variousgods.commands.CommandMiracle;
import com.lying.variousgods.data.VGDataGenerators;
import com.lying.variousgods.deities.DeityRegistry;
import com.lying.variousgods.deities.miracle.Miracles;
import com.lying.variousgods.init.VGBlockEntities;
import com.lying.variousgods.init.VGBlocks;
import com.lying.variousgods.init.VGEnchantments;
import com.lying.variousgods.init.VGEntities;
import com.lying.variousgods.init.VGItems;
import com.lying.variousgods.init.VGMenus;
import com.lying.variousgods.init.VGRegistries;
import com.lying.variousgods.network.PacketHandler;
import com.lying.variousgods.reference.Reference;
import com.mojang.logging.LogUtils;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Reference.ModInfo.MOD_ID)
public class VariousGods
{
    // Directly reference a slf4j logger
    public static final Logger LOG = LogUtils.getLogger();
    
    public static final IEventBus EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
    
    public VariousGods()
    {
    	DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
    			{
    				EVENT_BUS.register(ClientSetupEvents.class);
    			});
    	
        // Register the commonSetup method for modloading
        EVENT_BUS.addListener(this::commonSetup);
        EVENT_BUS.addListener(this::completeSetup);
        EVENT_BUS.addListener(VGDataGenerators::onGatherData);
        
        VGEntities.ENTITIES.register(EVENT_BUS);
        VGBlockEntities.BLOCK_ENTITIES.register(EVENT_BUS);
        VGBlocks.BLOCKS.register(EVENT_BUS);
        VGItems.ITEMS.register(EVENT_BUS);
        VGRegistries.registerCustom(EVENT_BUS);
        VGEnchantments.ENCHANTMENTS.register(EVENT_BUS);
		VGMenus.MENUS.register(EVENT_BUS);
		
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        PacketHandler.init();
    }
    
    private void completeSetup(final FMLLoadCompleteEvent event)
    {
    	Miracles.registerMiracleListeners();
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
    }
    
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event)
    {
    	CommandDeity.register(event.getDispatcher());
    	CommandMiracle.register(event.getDispatcher());
    }
    
    @SubscribeEvent
    public void onReloadListenersEvent(AddReloadListenerEvent event)
    {
    	event.addListener(DeityRegistry.getInstance());
    }
}
