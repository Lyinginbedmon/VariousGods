package com.lying.variousgods.client.gui.menu;

import com.lying.variousgods.block.BlockAltar;
import com.lying.variousgods.capabilities.PlayerData;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.common.ForgeMod;

public abstract class MenuAltar extends AbstractContainerMenu implements MenuProvider
{
	protected final BlockPos position;
	
	public MenuAltar(MenuType<MenuAltar> menuType, int containerIdIn, HitResult hitResult)
	{
		super(menuType, containerIdIn);
		this.position = hitResult == null || hitResult.getType() != Type.BLOCK ? null : ((BlockHitResult)hitResult).getBlockPos();
	}
	
	public ItemStack quickMoveStack(Player player, int slot) { return ItemStack.EMPTY; }
	
	public boolean stillValid(Player player)
	{
		Level world = player.getLevel();
		return player.blockPosition().closerThan(position, player.getAttributeValue(ForgeMod.REACH_DISTANCE.get())) && world.getBlockState(position).getBlock() instanceof BlockAltar;
	}
	
	public void removed(Player player)
	{
		if(player instanceof ServerPlayer)
			PlayerData.getCapability(player).setPraying();
	}
}
