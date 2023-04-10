package com.lying.variousgods.init;

import com.lying.variousgods.capabilities.PlayerData;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class VGCapabilities
{
	public static final Capability<PlayerData> PLAYER_DATA	= CapabilityManager.get(new CapabilityToken<>() {});
	
	public static void onRegisterCapabilities(final RegisterCapabilitiesEvent event)
	{
		event.register(PlayerData.class);
	}
}
