package com.lying.variousgods.deities.miracle;

import com.lying.variousgods.api.event.LivingConsumableEvent;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;

public class MiracleHolySacrament extends Miracle
{
	public MiracleHolySacrament() { super(Power.MAJOR); }
	
	public float getUtility(Player playerIn, Level worldIn) { return 1F - (playerIn.getHealth() / playerIn.getMaxHealth()); }
	
	public void addListeners(IEventBus bus)
	{
		bus.addListener(this::onFoodEaten);
		bus.addListener(this::onFoodDrank);
	}
	
	private static void handleMiracle(Player player, ItemStack consumable)
	{
		if(consumable.getFoodProperties(player).getNutrition() > Foods.BREAD.getNutrition() || !checkMiracle(player, Miracles.HOLY_SACRAMENT.get()))
			return;
		
		player.heal(player.getMaxHealth() - consumable.getFoodProperties(player).getNutrition());
		
		reportMiracle(player, Miracles.HOLY_SACRAMENT.get());
	}
	
	public void onFoodEaten(LivingConsumableEvent.Eat event)
	{
		if(event.getEntity().getType() != EntityType.PLAYER)
			return;
		handleMiracle((Player)event.getEntity(), event.getItem());
	}
	
	public void onFoodDrank(LivingConsumableEvent.Drink event)
	{
		if(event.getEntity().getType() != EntityType.PLAYER || event.getItem().getItem() == Items.POTION)
			return;
		
		handleMiracle((Player)event.getEntity(), event.getItem());
	}
}
