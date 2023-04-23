package com.lying.variousgods.utility.savedata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.compress.utils.Lists;

import com.lying.variousgods.block.BlockAltar;
import com.lying.variousgods.reference.Reference;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;

public class AltarWatcher extends SavedData
{
	protected static final String DATA_NAME = Reference.ModInfo.MOD_ID+"_altar_watcher";
	
	private Map<BlockPos, Integer> altarMap = new HashMap<>();
	private Level world;
	
	public static AltarWatcher instance(Level worldIn)
	{
		if(worldIn.isClientSide())
			return new AltarWatcher();
		
		AltarWatcher watcher = ((ServerLevel)worldIn).getDataStorage().computeIfAbsent(AltarWatcher::fromNbt, AltarWatcher::new, DATA_NAME);
		watcher.world = worldIn;
		return watcher;
	}
	
	public CompoundTag save(CompoundTag data)
	{
		ListTag altars = new ListTag();
		altarMap.forEach((pos,val) ->
		{
			if(val > 0)
			{
				CompoundTag tag = new CompoundTag();
				tag.put("Pos", NbtUtils.writeBlockPos(pos));
				tag.putInt("Users", val);
				altars.add(tag);
			}
		});
		data.put("Altars", altars);
		
		return data;
	}
	
	public void read(CompoundTag data)
	{
		altarMap.clear();
		if(data.contains("Altars", Tag.TAG_LIST))
		{
			ListTag altars = data.getList("Altars", Tag.TAG_COMPOUND);
			for(int i=0; i<altars.size(); i++)
			{
				CompoundTag tag = altars.getCompound(i);
				altarMap.put(NbtUtils.readBlockPos(tag.getCompound("Pos")), tag.getInt("Users"));
			}
		}
	}
	
	public static AltarWatcher fromNbt(CompoundTag tag)
	{
		AltarWatcher watcher = new AltarWatcher();
		watcher.read(tag);
		return watcher;
	}
	
	public void addUser(BlockPos pos)
	{
		altarMap.put(pos, altarMap.getOrDefault(pos, 0) + 1);
		
		BlockState state = world.getBlockState(pos);
		if(state.hasProperty(BlockAltar.PRAYING) && !world.isClientSide())
			world.setBlockAndUpdate(pos, state.setValue(BlockAltar.PRAYING, true));
	}
	
	public void removeUser(BlockPos pos)
	{
		int users = altarMap.getOrDefault(pos, 0) - 1;
		if(users <= 0)
		{
			altarMap.remove(pos);
			
			BlockState state = world.getBlockState(pos);
			if(state.hasProperty(BlockAltar.PRAYING) && !world.isClientSide())
				world.setBlockAndUpdate(pos, state.setValue(BlockAltar.PRAYING, false));
		}
		else
			altarMap.put(pos, users);
	}
	
	public void tick()
	{
		if(altarMap.isEmpty())
			return;
		
		List<BlockPos> toRemove = Lists.newArrayList();
		for(Entry<BlockPos, Integer> altar : altarMap.entrySet())
			if(altar.getValue() <= 0 || !world.getBlockState(altar.getKey()).hasProperty(BlockAltar.PRAYING))
				toRemove.add(altar.getKey());
		
		toRemove.forEach((pos) -> altarMap.remove(pos));
	}
}
