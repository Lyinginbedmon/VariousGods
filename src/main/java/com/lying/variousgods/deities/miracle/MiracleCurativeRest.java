package com.lying.variousgods.deities.miracle;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class MiracleCurativeRest extends Miracle
{
	public MiracleCurativeRest() { super(Power.MINOR); }
	
	public float getUtility(Player playerIn, Level worldIn) { return 1F - (playerIn.getHealth() / playerIn.getMaxHealth()); }
	
	public void addListeners(IEventBus bus)
	{
		bus.addListener(this::onSleepFinished);
	}
	
	public void onSleepFinished(SleepFinishedTimeEvent event)
	{
		for(Player player : event.getLevel().players())
		{
			if(!checkMiracle(player, Miracles.CURATIVE_REST.get()))
				return;
			
			// Perform miracle
			player.heal(Float.MAX_VALUE);
			
			reportMiracle(player, Miracles.CURATIVE_REST.get());
		}
	}
}
