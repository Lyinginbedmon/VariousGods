package com.lying.variousgods.deities.miracle;

import com.lying.variousgods.api.event.FortuneLevelEvent;
import com.lying.variousgods.data.VGBlockTags;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;

public abstract class MiracleBountiful extends Miracle
{
	public MiracleBountiful()
	{
		super(Power.MINOR);
	}
	
	public void addListeners(IEventBus bus)
	{
		bus.addListener(this::onFortuneEvent);
	}
	
	public void onFortuneEvent(FortuneLevelEvent event)
	{
		if(!event.isCanceled() && event.getEntity().getType() == EntityType.PLAYER && applysTo(event.getBlockState()))
		{
			Player player = (Player)event.getEntity();
			if(!checkMiracle(player, getSelf()))
				return;
			
			event.setFortuneLevel(event.getFortuneLevel() + 5);
			reportMiracle(player, getSelf());
		}
	}
	
	public abstract Miracle getSelf();
	
	public abstract boolean applysTo(BlockState state);
	
	public static class MiracleBountifulHarvest extends MiracleBountiful
	{
		public Miracle getSelf() { return Miracles.BOUNTIFUL_HARVEST.get(); }
		
		public boolean applysTo(BlockState state) { return state.is(VGBlockTags.CROP_BLOCKS); }
		
		public float getUtility(Player playerIn, Level worldIn) { return 0.5F; }
	}
	
	public static class MiracleBountifulMine extends MiracleBountiful
	{
		public Miracle getSelf() { return Miracles.BOUNTIFUL_MINE.get(); }
		
		public boolean applysTo(BlockState state) { return state.is(VGBlockTags.ORE_BLOCKS); }
		
		public float getUtility(Player playerIn, Level worldIn)
		{
			ItemStack heldItem = playerIn.getItemBySlot(EquipmentSlot.MAINHAND);
			return (float)heldItem.getDamageValue() / (float)heldItem.getMaxDamage();
		}
	}
}
