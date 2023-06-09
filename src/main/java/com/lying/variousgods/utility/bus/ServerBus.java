package com.lying.variousgods.utility.bus;

import java.util.List;

import org.apache.commons.compress.utils.Lists;

import com.lying.variousgods.capabilities.PlayerData;
import com.lying.variousgods.deities.miracle.BindingContract;
import com.lying.variousgods.deities.miracle.BindingContract.IInventoryContract;
import com.lying.variousgods.init.VGEnchantments;
import com.lying.variousgods.reference.Reference;
import com.lying.variousgods.utility.savedata.AltarWatcher;
import com.lying.variousgods.utility.savedata.BrewingStandWatcher;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.ModInfo.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerBus
{
	@SubscribeEvent
	public static void onAttachCapabilityEvent(AttachCapabilitiesEvent<Entity> event)
	{
		if(event.getObject().getType() == EntityType.PLAYER)
			event.addCapability(PlayerData.IDENTIFIER, new PlayerData((Player)event.getObject()));
	}
	
	@SubscribeEvent
	public static void onPlayerLogin(PlayerLoggedInEvent event)
	{
		PlayerData data = PlayerData.getCapability(event.getEntity());
		if(data != null)
			data.markDirty();
	}
	
	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.Clone event)
	{
		Player playerNew = event.getEntity();
		Player playerOld = event.getOriginal();
		playerOld.reviveCaps();
		
		PlayerData dataNew = PlayerData.getCapability(playerNew);
		PlayerData dataOld = PlayerData.getCapability(playerOld);
		if(dataNew != null && dataOld != null)
			dataNew.deserializeNBT(dataOld.serializeNBT());
		
		playerOld.invalidateCaps();
	}
	
	@SubscribeEvent
	public static void onLevelTick(LevelTickEvent event)
	{
		Level world = event.level;
		AltarWatcher watcher = AltarWatcher.instance(world);
		watcher.tick();
	}
	
	@SubscribeEvent
	public static void onPlayerTick(LivingTickEvent event)
	{
		if(event.getEntity().getType() == EntityType.PLAYER)
		{
			Player player = (Player)event.getEntity();
			PlayerData data = PlayerData.getCapability(player);
			data.tick();
			
			if(!player.getLevel().isClientSide())
			{
				List<BindingContract> contracts = data.contracts();
				contracts.removeIf((contract) -> !(contract instanceof IInventoryContract));
				
				searchAndDestroyContractItems(player, contracts, player.getInventory());
				searchAndDestroyContractItems(player, contracts, player.getEnderChestInventory());
			}
		}
	}
	
	private static void searchAndDestroyContractItems(Player player, List<BindingContract> contracts, Container inv)
	{
		List<ItemStack> toRemove = Lists.newArrayList();
		for(int i=0; i<inv.getContainerSize(); i++)
		{
			ItemStack item = inv.getItem(i);
			if(!item.isEmpty() && VGEnchantments.hasContractEnchantment(item))
			{
				boolean hasMatch = false;
				for(BindingContract contract : contracts)
					if(hasMatch = contract instanceof IInventoryContract && ((IInventoryContract)contract).targets(item))
						break;
				
				if(!hasMatch)
					toRemove.add(item);
			}
		}
		
		toRemove.forEach((item) -> IInventoryContract.destroyItem(item, player));
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onRightClickBrewingStand(PlayerInteractEvent.RightClickBlock event)
	{
		Level world = event.getLevel();
		BlockPos pos = event.getPos();
		if(world.getBlockState(pos).getBlock() != Blocks.BREWING_STAND || event.getUseBlock() == Result.DENY)
			return;
		
		BrewingStandWatcher.instance(world).setLastTouched(event.getEntity().getUUID(), pos);
	}
}
