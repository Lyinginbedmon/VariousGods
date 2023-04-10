package com.lying.variousgods.block.entity;

import com.lying.variousgods.init.VGBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TomeAltarEntity extends BlockEntity
{
	public TomeAltarEntity(BlockPos pos, BlockState state)
	{
		super(VGBlockEntities.TOME_ALTAR.get(), pos, state);
	}
}
