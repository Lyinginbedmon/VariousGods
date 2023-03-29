package com.lying.variousgods.utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

import javax.annotation.Nullable;

import org.apache.commons.compress.utils.Lists;

import com.google.common.base.Function;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ExUtils
{
	public static Vec3 posToVec(BlockPos pos) { return new Vec3(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D); }
	
	@Nullable
	public static BlockPos searchAreaFor(BlockPos pos, Level worldIn, int radiusIn, BiPredicate<BlockPos, Level> predicateIn)
	{
		AABB oldBounds = new AABB(new Vec3(0, 0, 0), new Vec3(1, 1, 1));
		for(int radius = 0; radius < radiusIn; radius++)
		{
			for(int x=-radius; x<=radius; x++)
				for(int z=-radius; z<=radius; z++)
					for(int y=-radius; y<=radius; y++)
					{
						if(radius > 0 && oldBounds.contains(new Vec3(x + 0.5D, y + 0.5D, z + 0.5D)))
							continue;
						
						BlockPos bagPos = pos.offset(x, y, z);
						if(predicateIn.test(bagPos, worldIn))
							return bagPos;
					}
			
			oldBounds = new AABB(new Vec3(-radius, -radius, -radius), new Vec3(radius + 1, radius + 1, radius + 1));
		}
		return null;
	}
	
	public static List<BlockPos> searchForPositions(BlockPos pos, Level worldIn, int radiusIn, BiPredicate<BlockPos, Level> predicateIn)
	{
		List<BlockPos> positions = Lists.newArrayList();
		AABB oldBounds = new AABB(new Vec3(0, 0, 0), new Vec3(1, 1, 1));
		for(int radius = 0; radius < radiusIn; radius++)
		{
			for(int x=-radius; x<=radius; x++)
				for(int z=-radius; z<=radius; z++)
					for(int y=-radius; y<=radius; y++)
					{
						if(radius > 0 && oldBounds.contains(new Vec3(x + 0.5D, y + 0.5D, z + 0.5D)))
							continue;
						
						BlockPos bagPos = pos.offset(x, y, z);
						if(predicateIn.test(bagPos, worldIn))
							positions.add(bagPos);
					}
			
			oldBounds = new AABB(new Vec3(-radius, -radius, -radius), new Vec3(radius + 1, radius + 1, radius + 1));
		}
		
		return positions;
	}
	
	public static <T extends Object> T getWeightedObject(List<? extends T> set, Function<T, Double> evaluator, Function<Pair<T, T>, T> adder, Function<Pair<T,Double>, T> scalar)
	{
		double minVal = Double.MAX_VALUE;
		
		Map<T, Double> values = new HashMap<>();
		for(int i=0; i<set.size(); i++)
		{
			double val = evaluator.apply(set.get(i));
			values.put(set.get(i), val);
			minVal = Math.min(minVal, val);
		}
		
		// Weighted value
		double weightSum = 0D;
		T weightedVal = null;
		for(T pos : set)
		{
			double weight = minVal / values.get(pos);
			T value = scalar.apply(Pair.of(pos, 1 / weight));
			weightedVal = weightedVal == null ? value : adder.apply(Pair.of(weightedVal, value));
			weightSum += weight;
		}
		
		return scalar.apply(Pair.of(weightedVal, 1 / weightSum));
	}
}
