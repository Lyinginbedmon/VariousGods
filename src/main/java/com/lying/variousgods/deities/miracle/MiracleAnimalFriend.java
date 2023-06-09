package com.lying.variousgods.deities.miracle;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class MiracleAnimalFriend extends Miracle
{
	public MiracleAnimalFriend()
	{
		super(Power.MINOR);
	}
	
	public float getUtility(Player playerIn, Level worldIn) { return 0.5F; }
	
	public void addListeners(IEventBus bus)
	{
		bus.addListener(this::onAnimalTame);
	}
	
	public void onAnimalTame(PlayerInteractEvent.EntityInteract event)
	{
		if(!event.isCanceled() && event.getTarget() instanceof TamableAnimal && !((TamableAnimal)event.getTarget()).isTame())
		{
			Player player = event.getEntity();
			if(!checkMiracle(player, Miracles.ANIMAL_FRIEND.get()))
				return;
			
			((TamableAnimal)event.getTarget()).tame(player);
			event.setCanceled(true);
			reportMiracle(player, Miracles.ANIMAL_FRIEND.get());
		}
	}
}
