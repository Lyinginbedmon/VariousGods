package com.lying.variousgods.deities.miracle;

import com.lying.variousgods.api.event.LivingConsumableEvent;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;

public class MiracleFullStomach extends Miracle
{
	public MiracleFullStomach() { super(Power.MINOR); }
	
	public float getUtility(Player playerIn, Level worldIn) { return 1F - ((float)playerIn.getFoodData().getFoodLevel() / 20F); }
	
	public void addListeners(IEventBus bus)
	{
		bus.addListener(this::onFoodEaten);
	}
	
	public void onFoodEaten(LivingConsumableEvent.Eat event)
	{
		if(event.getEntity().getType() != EntityType.PLAYER || event.getItem().getCount() > 1)
			return;
		
		Player player = (Player)event.getEntity();
		if(!checkMiracle(player, Miracles.FULL_STOMACH.get()))
			return;
		
		player.getFoodData().setFoodLevel(20);
		
		reportMiracle(player, Miracles.FULL_STOMACH.get());
	}
}
