package com.lying.variousgods.proxy;

import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

public interface IProxy 
{
	public default void init() { }
	
	public default void clientInit() { }
	
	public default void registerHandlers(){ }
	
	public default void onLoadComplete(FMLLoadCompleteEvent event){ }
}