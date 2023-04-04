package com.lying.variousgods.capabilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.lying.variousgods.api.event.PlayerPrayerEvent;
import com.lying.variousgods.client.ClientSetupEvents;
import com.lying.variousgods.deities.Deity;
import com.lying.variousgods.deities.DeityRegistry;
import com.lying.variousgods.deities.miracle.BindingContract;
import com.lying.variousgods.deities.personality.ContextQuotient;
import com.lying.variousgods.deities.personality.ContextQuotients;
import com.lying.variousgods.init.ExCapabilities;
import com.lying.variousgods.network.PacketHandler;
import com.lying.variousgods.network.PacketSyncPlayerData;
import com.lying.variousgods.reference.Reference;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerData implements ICapabilitySerializable<CompoundTag>
{
	public static final ResourceLocation IDENTIFIER = new ResourceLocation(Reference.ModInfo.MOD_ID, "player_data");
	private static final long OPINION_RATE = Reference.Values.TICKS_PER_SECOND * 5;
	private static final int DIET_LIMIT = 12;
	
	private Player thePlayer;
	
	private Map<ResourceLocation, Double> quotients = new HashMap<>();
	private List<TagKey<Item>> recentDiet = Lists.newArrayList();
	
	private String deityName = DeityRegistry.APOSTATE.simpleName();
	private boolean everWorshipped = false;
	private double prevOpinion, currentOpinion;
	private long ticksSinceOpinion = 0;
	
	private int ticksToMiracle = 0;
	private ResourceLocation forcedMiracle = null;
	
	private int prayingTicks = 0;
	private int prayingCooldown = 0;
	private BlockPos altarPos = BlockPos.ZERO;
	
	private List<Consumer<Player>> queuedEvents = Lists.newArrayList();
	private List<BindingContract> bindingContracts = Lists.newArrayList();
	
	/**
	 * TODO Implement breadcrumb trail handling
	 * Detect when player passes threshold point (exits portal, goes underground, etc.)
	 * Add positions every 16 blocks, link together by pathable proximity (ref: HearthLightPathfinder)
	 */
	private List<BlockPos> breadcrumbs = Lists.newArrayList();
	
	private boolean isDirty = true;
	
	public PlayerData(Player playerIn)
	{
		this.thePlayer = playerIn;
	}
	
	public void setPlayer(Player playerIn) { this.thePlayer = playerIn; }
	
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
	{
		return ExCapabilities.PLAYER_DATA.orEmpty(cap, LazyOptional.of(() -> this));
	}
	
	public static PlayerData getCapability(Player player)
	{
		if(player == null)
			return null;
		else if(player.getLevel().isClientSide())
			return ClientSetupEvents.getPlayerData(player);
		
		PlayerData data = player.getCapability(ExCapabilities.PLAYER_DATA).orElse(new PlayerData(player));
		data.thePlayer = player;
		return data;
	}
	
	public CompoundTag serializeNBT()
	{
		CompoundTag data = new CompoundTag();
		data.putString("Deity", hasDeity() ? this.deityName : DeityRegistry.APOSTATE.simpleName());
		data.putBoolean("Worshipped", everWorshipped);
		data.putDouble("OpinionPrev", hasDeity() ? this.prevOpinion : 0);
		data.putDouble("OpinionNow", hasDeity() ? this.currentOpinion : 0);
		data.putLong("CheckTime", this.ticksSinceOpinion);
		data.putInt("Cooldown", this.ticksToMiracle);
		
		data.putInt("Praying", this.prayingTicks);
		data.putInt("PrayingCooldown", this.prayingCooldown);
		data.put("AltarPos", NbtUtils.writeBlockPos(this.altarPos));
		
		if(!this.breadcrumbs.isEmpty())
		{
			ListTag crumbs = new ListTag();
			this.breadcrumbs.forEach((crumb) -> crumbs.add(NbtUtils.writeBlockPos(crumb)));
			data.put("Breadcrumbs", crumbs);
		}
		
		if(this.forcedMiracle != null)
			data.putString("Forced", this.forcedMiracle.toString());
		
		ListTag quotients = new ListTag();
		for(Entry<ResourceLocation, Double> entry : this.quotients.entrySet())
		{
			if(entry.getValue() <= 0D)
				continue;
			
			CompoundTag val = new CompoundTag();
			val.putString("Name", entry.getKey().toString());
			val.putDouble("Val", entry.getValue());
			
			quotients.add(val);
		}
		data.put("Values", quotients);
		
		ListTag diet = new ListTag();
		this.recentDiet.forEach((tag) -> diet.add(StringTag.valueOf(tag.location().toString())));
		data.put("Diet", diet);
		
		return data;
	}
	
	public void deserializeNBT(CompoundTag nbt)
	{
		this.deityName = nbt.getString("Deity");
		this.everWorshipped = nbt.getBoolean("Worshipped");
		this.prevOpinion = nbt.getDouble("OpinionPrev");
		this.currentOpinion = nbt.getDouble("OpinionNow");
		this.ticksSinceOpinion = nbt.getLong("CheckTime");
		this.ticksToMiracle = nbt.getInt("Cooldown");
		
		this.prayingTicks = nbt.getInt("Praying");
		this.prayingCooldown = nbt.getInt("PrayingCooldown");
		this.altarPos = NbtUtils.readBlockPos(nbt.getCompound("AltarPos"));
		
		this.breadcrumbs.clear();
		if(nbt.contains("Breadcrumbs", Tag.TAG_LIST))
		{
			ListTag crumbs = nbt.getList("Breadcrumbs", Tag.TAG_COMPOUND);
			for(int i=0; i<crumbs.size(); i++)
				this.breadcrumbs.add(NbtUtils.readBlockPos(crumbs.getCompound(i)));
		}
		
		this.forcedMiracle = nbt.contains("Forced", Tag.TAG_STRING) ? new ResourceLocation(nbt.getString("Forced")) : null;
		
		this.quotients.clear();
		ListTag quotients = nbt.getList("Values", Tag.TAG_COMPOUND);
		for(int i=0; i<quotients.size(); i++)
		{
			CompoundTag data = quotients.getCompound(i);
			this.quotients.put(new ResourceLocation(data.getString("Name")), data.getDouble("Val"));
		}
		
		this.recentDiet.clear();
		ListTag diet = nbt.getList("Diet", Tag.TAG_STRING);
		for(int i=0; i<diet.size(); i++)
		{
			ResourceLocation registryName = new ResourceLocation(diet.getString(i));
			TagKey<Item> dietary = TagKey.create(Registry.ITEM_REGISTRY, registryName);
			if(dietary != null)
				recentDiet.add(dietary);
		}
	}
	
	public void queueEvent(Consumer<Player> eventIn) { this.queuedEvents.add(eventIn); }
	public void addContract(BindingContract contractIn)
	{
		contractIn.start(thePlayer, thePlayer.getLevel());
		this.bindingContracts.add(contractIn);
		markDirty();
	}
	
	public List<BindingContract> contracts()
	{
		List<BindingContract> clone = Lists.newArrayList();
		clone.addAll(bindingContracts);
		return clone;
	}
	
	public boolean hasForcedMiracle() { return this.forcedMiracle != null; }
	@Nullable
	public ResourceLocation forcedMiracle() { return this.forcedMiracle; }
	public void setForceMiracle(@Nullable ResourceLocation nameIn) { this.forcedMiracle = nameIn; markDirty(); }
	public void clearForceMiracle() { setForceMiracle(null); }
	
	public boolean hasDeity() { return this.deityName != null && this.deityName.length() > 0 && !this.deityName.equals(DeityRegistry.APOSTATE.simpleName()); }
	public boolean hasHadDeity() { return this.everWorshipped; }
	public String getDeityName() { return this.deityName; }
	@Nullable
	public Deity getDeity() { return DeityRegistry.getInstance().getDeity(this.deityName); }
	public void setDeity(@Nonnull Deity deityIn)
	{
		if(!getDeity().isEmpty())
			getDeity().onPlayerAbandon((ServerPlayer)this.thePlayer);
		
		this.deityName = deityIn.simpleName();
		
		if(!deityIn.isEmpty())
			deityIn.onPlayerSelected((ServerPlayer)this.thePlayer, !everWorshipped);
		
		everWorshipped = everWorshipped || !deityIn.isEmpty();
		markDirty();
		
		Deity god = getDeity();
		if(!god.isEmpty())
			this.prevOpinion = this.currentOpinion = 0D;
		else
			this.prevOpinion = this.currentOpinion = god.opinionOf(thePlayer);
	}
	
	public double getOpinion() { return this.prevOpinion + ((currentOpinion - prevOpinion) * ((double)ticksSinceOpinion / (double)OPINION_RATE)); }
	
	public void setQuotient(ResourceLocation quotientName, double value)
	{
		this.quotients.put(quotientName, value);
		markDirty();
	}
	public void addQuotient(ResourceLocation quotientName, double value)
	{
		setQuotient(quotientName, getQuotient(quotientName) + value);
	}
	public double getQuotient(ResourceLocation quotientName) { return this.quotients.getOrDefault(quotientName, 0D); }
	
	public Map<ResourceLocation, Double> getQuotients() { return this.quotients; }
	
	public void addTagToDiet(TagKey<Item> tag)
	{
		this.recentDiet.add(tag);
		while(recentDiet.size() > DIET_LIMIT)
			recentDiet.remove(0);
		markDirty();
	}
	
	public Map<TagKey<Item>, Double> getRecentDiet()
	{
		Map<TagKey<Item>, Integer> tally = new HashMap<>();
		for(TagKey<Item> tag : this.recentDiet)
			tally.put(tag, 1 + tally.getOrDefault(tag, 0));
		
		Map<TagKey<Item>, Double> diet = new HashMap<>();
		tally.forEach((tag,count) -> diet.put(tag, (double)count / (double)this.recentDiet.size()));
		return diet;
	}
	
	public void setAltarPos(BlockPos pos) { this.altarPos = pos; markDirty(); }
	public boolean isPrayingAt(BlockPos pos) { return isPraying() && pos.equals(altarPos); }
	
	public void tick()
	{
		Deity god = getDeity();
		if(god == null || thePlayer == null || thePlayer.getLevel().isClientSide())
			return;
		
		if(this.prayingTicks > 0 && --this.prayingTicks == 0)
		{
			this.prayingCooldown = Reference.Values.TICKS_PER_MINUTE * 5;
			if(!MinecraftForge.EVENT_BUS.post(new PlayerPrayerEvent(thePlayer, god, thePlayer.blockPosition())))
			{
				addQuotient(ContextQuotients.PRAYER.getId(), 1);
				god.onPlayerPray((ServerPlayer)thePlayer);
			}
		}
		
		if(this.prayingCooldown > 0)
			--this.prayingCooldown;
		
		if(!this.queuedEvents.isEmpty())
		{
			for(Consumer<Player> event : this.queuedEvents)
				event.accept(thePlayer);
			
			this.queuedEvents.clear();
		}
		
		if(!this.bindingContracts.isEmpty())
		{
			this.bindingContracts.forEach((contract) -> 
			{
				contract.tick(thePlayer, thePlayer.getLevel());
				if(contract.isComplete())
					contract.cleanup(thePlayer, thePlayer.getLevel());
			});
			
			this.bindingContracts.removeIf((contract) -> contract.isComplete());
		}
		
		if(!this.quotients.isEmpty())
		{
			Map<ResourceLocation, Double> quotientMap = new HashMap<>();
			for(Entry<ResourceLocation, Double> entry : this.quotients.entrySet())
			{
				ContextQuotient quotient = ContextQuotients.getByName(entry.getKey()).get();
				double val = entry.getValue() - quotient.decayRate();
				if(val > 0)
					quotientMap.put(entry.getKey(), val);
			}
			
			this.quotients.clear();
			this.quotients.putAll(quotientMap);
			markDirty();
		}
		
		if(thePlayer.getRandom().nextInt((int)Math.max(1, OPINION_RATE - this.ticksSinceOpinion++)) == 0)
		{
			this.prevOpinion = this.currentOpinion;
			this.currentOpinion = god.opinionOf(thePlayer);
			this.ticksSinceOpinion = 0;
			markDirty();
		}
		
		if(this.ticksToMiracle > 0)
		{
			--this.ticksToMiracle;
			markDirty();
		}
		
		if(isDirty && !this.thePlayer.getLevel().isClientSide())
		{
			PacketHandler.sendTo((ServerPlayer)this.thePlayer, new PacketSyncPlayerData(this.thePlayer.getUUID(), this));
			this.isDirty = false;
		}
	}
	
	public boolean canHaveMiracle() { return this.ticksToMiracle <= 0; }
	public void setMiracleCooldown(int par1Int)
	{
		this.ticksToMiracle = par1Int;
		markDirty();
	}
	
	public boolean canPray() { return this.prayingCooldown == 0; }
	public boolean isPraying() { return this.prayingTicks > 0; }
	
	public void setPraying(BlockPos altarPos)
	{
		this.prayingTicks = Reference.Values.TICKS_PER_SECOND * 5;
		markDirty();
	}
	
	public void setPraying()
	{
		this.prayingTicks = 0;
		markDirty();
	}
	
	public void markDirty() { this.isDirty = true; }
}
