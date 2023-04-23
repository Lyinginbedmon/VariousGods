package com.lying.variousgods.deities.miracle;

import java.util.Comparator;
import java.util.List;

import com.lying.variousgods.api.event.PlayerBreakItemEvent;
import com.lying.variousgods.capabilities.PlayerData;
import com.lying.variousgods.data.VGEntityTags;
import com.lying.variousgods.data.VGItemTags;
import com.lying.variousgods.deities.miracle.BindingContract.IInventoryContract;
import com.lying.variousgods.init.VGEnchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.eventbus.api.IEventBus;

public class MiracleContractPaladin extends Miracle
{
	public MiracleContractPaladin()
	{
		super(Power.MINOR);
	}
	
	public float getUtility(Player playerIn, Level worldIn)
	{
		ItemStack heldItem = playerIn.getItemBySlot(EquipmentSlot.MAINHAND);
		if(heldItem.is(VGItemTags.WEAPON) && heldItem.isDamageableItem())
			if(heldItem.getItem() instanceof TieredItem)
				return 1F - ((float)TierSortingRegistry.getTiersLowerThan(((TieredItem)heldItem.getItem()).getTier()).size() / (float)TierSortingRegistry.getSortedTiers().size());
			else
				return 0.8F;
		
		return 0F;
	}
	
	public void addListeners(IEventBus bus)
	{
		bus.addListener(this::onWeaponBreak);
	}
	
	public void onWeaponBreak(PlayerBreakItemEvent event)
	{
		if(event.getItem().getEnchantmentLevel(VGEnchantments.CONTRACT_ITEM.get()) > 0)
		{
			event.setCanceled(true);
			return;
		}
		
		if(!event.isCanceled() && event.getItem().is(VGItemTags.WEAPON))
		{
			Player player = event.getEntity();
			List<LivingEntity> bosses = player.getLevel().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(ContractPaladin.RANGE), (living) -> living.getType().is(VGEntityTags.BOSS));
			if(bosses.isEmpty() || !checkMiracle(player, Miracles.CONTRACT_PALADIN.get()))
				return;
			
			Comparator<LivingEntity> sorter = new Comparator<>()
					{
						public int compare(LivingEntity o1, LivingEntity o2)
						{
							float utility1 = o1.getHealth() / o1.distanceTo(player);
							float utility2 = o2.getHealth() / o2.distanceTo(player);
							return utility1 < utility2 ? -1 : utility1 > utility2 ? 1 : 0;
						}
					};
			bosses.sort(sorter);
			
			PlayerData.getCapability(player).addContract(new ContractPaladin(player, event.getItem(), bosses.get(0)));
			event.setCanceled(true);
			reportMiracle(player, Miracles.CONTRACT_PALADIN.get());
		}
	}
	
	public static class ContractPaladin extends BindingContract implements IInventoryContract
	{
		private static final double RANGE = 8D;
		
		private final ItemStack stack;
		private final LivingEntity boss;
		
		public ContractPaladin(Player targetIn, ItemStack stackIn, LivingEntity bossIn)
		{
			super(-1);
			this.boss = bossIn;
			this.stack = stackIn;
		}
		
		public void start(Player player, Level world)
		{
			this.stack.enchant(VGEnchantments.CONTRACT_ITEM.get(), 1);
		}
		
		public void doEffect(int ticksRemaining, Player player, Level world)
		{
			if(player == null || !player.isAlive())
				disregard();
			else if(boss == null || !boss.isAlive())
				disregard();
			else if(player.distanceTo(boss) > (RANGE * RANGE))
				disregard();
		}
		
		public boolean targets(ItemStack stack) { return stack.equals(this.stack, false); }
	}
}
