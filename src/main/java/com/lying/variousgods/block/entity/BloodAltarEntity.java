package com.lying.variousgods.block.entity;

import com.lying.variousgods.init.VGBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BloodAltarEntity extends BlockEntity
{
	public BloodAltarEntity(BlockPos pos, BlockState state)
	{
		super(VGBlockEntities.BLOOD_ALTAR.get(), pos, state);
	}
}
