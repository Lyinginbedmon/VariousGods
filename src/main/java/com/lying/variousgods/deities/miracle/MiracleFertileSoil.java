package com.lying.variousgods.deities.miracle;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

public class MiracleFertileSoil extends Miracle
{
	public MiracleFertileSoil() { super(Power.MAJOR); }
	
	public float getUtility(Player playerIn, Level worldIn)
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void addListeners(IEventBus bus)
	{
		bus.addListener(EventPriority.LOWEST, this::onBonemeal);
	}
	
	public void onBonemeal(BonemealEvent event)
	{
		if(event.isCanceled() || event.getResult() == Result.DENY)
			return;
		
		Player player = event.getEntity();
		if(!checkMiracle(player, Miracles.FERTILE_SOIL.get()))
			return;
		
		reportMiracle(player, Miracles.FERTILE_SOIL.get());
		
		Level world = event.getLevel();
		BlockState state = event.getBlock();
		for(int y=-2; y<=2; y++)
			for(int x=-2; x<=2; x++)
				for(int z=-2; z<=2; z++)
				{
					BlockPos pos = event.getPos().offset(x, y, z);
					BlockState blockAt = world.getBlockState(pos);
					
					if(blockAt.getBlock() == state.getBlock())
						BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), world, pos, player);
				}
	}
}
