package com.lying.variousgods.deities.miracle;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.apache.commons.compress.utils.Lists;

import com.lying.variousgods.VariousGods;
import com.lying.variousgods.deities.miracle.MiracleBountiful.MiracleBountifulHarvest;
import com.lying.variousgods.deities.miracle.MiracleBountiful.MiracleBountifulMine;
import com.lying.variousgods.init.VGRegistries;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.RegistryObject;

public class Miracles
{
	// Registry objects for different miracles
	public static final RegistryObject<Miracle> SAFE_LANDING = register("safe_landing", () -> new MiracleSafeLanding());
	public static final RegistryObject<Miracle> INDOMITABLE = register("indomitable", () -> new MiracleIndomitable());
	public static final RegistryObject<Miracle> JUGGERNAUT = register("juggernaut", () -> new MiracleJuggernaut());
	public static final RegistryObject<Miracle> REAPERS_BAG = register("reapers_bag", () -> new MiracleReapersBag());
	public static final RegistryObject<Miracle> LAST_ARROW = register("last_arrow", () -> new MiracleLastArrow());
	public static final RegistryObject<Miracle> REDIRECT = register("redirect", () -> new MiracleRedirect());
	public static final RegistryObject<Miracle> CONTRACT_MINE = register("mining_contract", () -> new MiracleContractMine());
	public static final RegistryObject<Miracle> CONTRACT_PALADIN = register("paladin_contract", () -> new MiracleContractPaladin());
	public static final RegistryObject<Miracle> LIGHTNING = register("lightning", () -> new MiracleLightning());
	public static final RegistryObject<Miracle> ANIMAL_FRIEND = register("animal_friend", () -> new MiracleAnimalFriend());
	public static final RegistryObject<Miracle> BOUNTIFUL_HARVEST = register("bountiful_harvest", () -> new MiracleBountifulHarvest());
	public static final RegistryObject<Miracle> BOUNTIFUL_MINE = register("bountiful_mine", () -> new MiracleBountifulMine());
	// Fertile Soil - Bonemealing crops affects all similar crops within a 5x5x5 area
	public static final RegistryObject<Miracle> FERTILE_SOIL = register("fertile_soil", () -> new MiracleFertileSoil());
	public static final RegistryObject<Miracle> FULL_STOMACH = register("full_stomach", () -> new MiracleFullStomach());
	public static final RegistryObject<Miracle> HOLY_SACRAMENT = register("holy_sacrament", () -> new MiracleHolySacrament());
	public static final RegistryObject<Miracle> CURATIVE_REST = register("curative_rest", () -> new MiracleCurativeRest());
	// Holy Blessing - Praying heals you
	
	public static final RegistryObject<Miracle> STRONG_BREW = register("strong_brew", () -> new MiracleStrongBrew());
	// By God's Light - Damages hostile undead in an area around you
	// Deathguard - Spawns a group of temporary friendly zombies and skeletons to protect you
	public static final RegistryObject<Miracle> DEATHGUARD = register("deathguard", () -> new MiracleSummon.DeathGuard());
	// Momma Bear - Spawns a group of temporary friendly regional animals to protect you
	// Adder's Nest - Spawns a group of temporary friendly snakes to protect you
	public static final RegistryObject<Miracle> HEARTH_LIGHT = register("hearth_light", () -> new MiracleSummon.HearthLight());
	
	private static RegistryObject<Miracle> register(String nameIn, Supplier<Miracle> miracleIn)
	{
		return VGRegistries.MIRACLES.register(nameIn, miracleIn);
	}
	
	public static void init() { }
	
	public static void registerMiracleListeners()
	{
		VariousGods.LOG.info("# Adding miracle listeners #");
		VGRegistries.MIRACLES.getEntries().forEach((entry) -> 
		{
			entry.get().addListeners(MinecraftForge.EVENT_BUS);
			VariousGods.LOG.info("# * Added "+entry.getId());
		});
		VariousGods.LOG.info("# "+VGRegistries.MIRACLES.getEntries().size()+" total miracles #");
	}
	
	@Nullable
	public static RegistryObject<Miracle> getRegistryName(Miracle miracleIn)
	{
		for(RegistryObject<Miracle> entry : VGRegistries.MIRACLES.getEntries())
			if(entry.isPresent() && entry.get().getClass().equals(miracleIn.getClass()))
				return entry;
		return null;
	}
	
	@Nullable
	public static RegistryObject<Miracle> getByName(ResourceLocation nameIn)
	{
		for(RegistryObject<Miracle> entry : VGRegistries.MIRACLES.getEntries())
			if(entry.isPresent() && entry.getId().equals(nameIn))
				return entry;
		return null;
	}
	
	public static List<String> getMiracleNames()
	{
		List<String> names = Lists.newArrayList();
		for(RegistryObject<Miracle> entry : VGRegistries.MIRACLES.getEntries())
			names.add(entry.getId().toString());
		return names;
	}
}
