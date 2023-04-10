package com.lying.variousgods.block.entity;

import com.lying.variousgods.block.BlockAltar;
import com.lying.variousgods.init.VGBlockEntities;
import com.lying.variousgods.reference.Reference;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EnderAltarEntity extends BlockEntity
{
	public static final int MAX_OPEN_TIME = Reference.Values.TICKS_PER_SECOND;
	private int ticksPraying = 0;
	
	public EnderAltarEntity(BlockPos pos, BlockState state)
	{
		super(VGBlockEntities.ENDER_ALTAR.get(), pos, state);
	}
	
	public static void tick(Level world, BlockPos pos, BlockState state, EnderAltarEntity tile)
	{
		if(state.getValue(BlockAltar.PRAYING))
		{
			if(tile.ticksPraying < MAX_OPEN_TIME)
				tile.ticksPraying++;
		}
		else if(tile.ticksPraying > 0)
			tile.ticksPraying--;
	}
	
	public int timeOpen() { return this.ticksPraying; }
}
