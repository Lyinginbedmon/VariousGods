package com.lying.variousgods.api.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class PlayerBreadcrumbEvent extends PlayerEvent
{
	private final BlockPos pos;
	
	public PlayerBreadcrumbEvent(Player player)
	{
		super(player);
		pos = player.blockPosition();
	}
	
	public BlockPos position() { return this.pos; }
}
