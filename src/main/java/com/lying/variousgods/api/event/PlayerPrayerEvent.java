package com.lying.variousgods.api.event;

import com.lying.variousgods.deities.Deity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class PlayerPrayerEvent extends PlayerEvent
{
	private final BlockPos pos;
	private final Deity theGod;
	
	public PlayerPrayerEvent(Player player, Deity god, BlockPos posIn)
	{
		super(player);
		theGod = god;
		pos = posIn;
	}
	
	public BlockPos position() { return this.pos; }
	
	public Deity god() { return this.theGod; }
}
