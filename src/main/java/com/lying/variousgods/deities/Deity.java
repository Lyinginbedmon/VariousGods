package com.lying.variousgods.deities;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.lying.variousgods.VariousGods;
import com.lying.variousgods.deities.miracle.Miracle;
import com.lying.variousgods.deities.miracle.Miracles;
import com.lying.variousgods.deities.personality.PersonalityModel;
import com.lying.variousgods.init.VGRegistries;
import com.lying.variousgods.init.MiracleTags;
import com.lying.variousgods.reference.Reference;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandFunction;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.RegistryObject;

public class Deity
{
	private final String simpleName;
	private final Component displayName;
	private final Component blurbText;
	private final List<TagKey<Miracle>> domains = Lists.newArrayList();
	private final ItemStack displayItem;
	private final long randSeed;
	
	private final PersonalityModel personality;
	
	private Random rand;
	private CommandFunction.CacheableFunction functionOnPray = CommandFunction.CacheableFunction.NONE;
	private CommandFunction.CacheableFunction functionWhenSelected = CommandFunction.CacheableFunction.NONE;
	private CommandFunction.CacheableFunction functionWhenSelectedFirst = CommandFunction.CacheableFunction.NONE;
	private CommandFunction.CacheableFunction functionWhenAbandoned = CommandFunction.CacheableFunction.NONE;
	
	public Deity(String simpleNameIn, ItemStack itemIn, PersonalityModel personalityIn, long seed, List<TagKey<Miracle>> domainsIn)
	{
		this(simpleNameIn, Component.translatable("deity."+Reference.ModInfo.MOD_ID+"."+simpleNameIn+"_name"), itemIn, Component.translatable("deity."+Reference.ModInfo.MOD_ID+"."+simpleNameIn+"_desc").withStyle(ChatFormatting.ITALIC), personalityIn, seed, domainsIn);
	}
	
	public Deity(String simpleNameIn, Component nameIn, ItemStack itemIn, Component description, PersonalityModel personalityIn, long seed, List<TagKey<Miracle>> domainsIn)
	{
		this.simpleName = simpleNameIn;
		this.displayName = nameIn;
		this.blurbText = description;
		this.personality = personalityIn;
		this.randSeed = seed;
		this.rand = new Random(seed);
		if(domainsIn != null)
			this.domains.addAll(domainsIn);
		this.displayItem = itemIn;
	}
	public Deity(String simpleNameIn, Component nameIn, ItemStack itemIn, Component description, long seed, List<TagKey<Miracle>> domainsIn)
	{
		this(simpleNameIn, nameIn, itemIn, description, new PersonalityModel(), seed, domainsIn);
	}
	
	public Deity setFunctions(CommandFunction.CacheableFunction pray, CommandFunction.CacheableFunction selected, CommandFunction.CacheableFunction first, CommandFunction.CacheableFunction abandoned)
	{
		this.functionOnPray = pray;
		this.functionWhenSelected = selected;
		this.functionWhenSelectedFirst = first;
		this.functionWhenAbandoned = abandoned;
		return this;
	}
	
	public boolean isEmpty() { return simpleName().equals(DeityRegistry.APOSTATE.simpleName()); }
	
	public String simpleName() { return this.simpleName; }
	
	public Component displayName() { return this.displayName; }
	
	public Component description() { return this.blurbText; }
	
	public List<TagKey<Miracle>> domains() { return this.domains; }
	
	public ItemStack getDisplayItem() { return this.displayItem.copy(); }
	
	public boolean hasMiracle(Miracle miracleIn) { return hasMiracle(Miracles.getRegistryName(miracleIn)); }
	
	public boolean hasMiracle(RegistryObject<Miracle> miracleIn)
	{
		if(miracleIn != null)
			for(TagKey<Miracle> domain : domains())
				for(RegistryObject<Miracle> miracle : VGRegistries.MIRACLES.getEntries())
					if(miracle.isPresent() && miracle.get().is(domain) && miracle == miracleIn)
						return true;
		return false;
	}
	
	public List<Miracle> miracles()
	{
		List<Miracle> miracles = Lists.newArrayList();
		for(TagKey<Miracle> domain : domains())
			for(RegistryObject<Miracle> miracle : VGRegistries.MIRACLES.getEntries())
				if(miracle.isPresent() && miracle.get().is(domain) && !miracles.contains(miracle.get()))
					miracles.add(miracle.get());
		return miracles;
	}
	
	public PersonalityModel getPersonality() { return this.personality; }
	
	public double opinionOf(Player playerIn) { return this.personality.currentOpinion(playerIn); }	
	
	public Random getRandom() { return this.rand; }
	
	public void onPlayerSelected(ServerPlayer player, boolean isFirstGod)
	{
		MinecraftServer server = player.getServer();
		if(isFirstGod)
			this.functionWhenSelectedFirst.get(server.getFunctions()).ifPresent((exec) -> server.getFunctions().execute(exec, player.createCommandSourceStack().withSuppressedOutput().withPermission(2)));
		
		this.functionWhenSelected.get(server.getFunctions()).ifPresent((exec) -> server.getFunctions().execute(exec, player.createCommandSourceStack().withSuppressedOutput().withPermission(2)));
	}
	
	public void onPlayerAbandon(ServerPlayer player)
	{
		MinecraftServer server = player.getServer();
		this.functionWhenAbandoned.get(server.getFunctions()).ifPresent((exec) -> server.getFunctions().execute(exec, player.createCommandSourceStack().withSuppressedOutput().withPermission(2)));
	}
	
	public void onPlayerPray(ServerPlayer player)
	{
		MinecraftServer server = player.getServer();
		this.functionOnPray.get(server.getFunctions()).ifPresent((exec) -> server.getFunctions().execute(exec, player.createCommandSourceStack().withSuppressedOutput().withPermission(2)));
	}
	
	public static Deity fromJson(String name, JsonElement json) throws JsonSyntaxException
	{
		if(json == null)
			return null;
		JsonObject object = json.getAsJsonObject();
		
		Component displayName = Component.literal(name);
		if(object.has("DisplayName"))
		{
			String s = object.get("DisplayName").getAsString();
			try
			{
				displayName = Component.Serializer.fromJson(s);
			}
			catch (Exception exception)
			{
				VariousGods.LOG.warn("Failed to parse deity display name {}", s, exception);
			}
		}
		
		Component description = Component.empty();
		if(object.has("Description"))
		{
			String s = object.get("Description").getAsString();
			try
			{
				description = Component.Serializer.fromJson(s);
			}
			catch (Exception exception)
			{
				VariousGods.LOG.warn("Failed to parse deity description {}", s, exception);
			}
		}
		
		PersonalityModel personality = new PersonalityModel();
		if(object.has("Personality"))
			personality = PersonalityModel.fromJson(object.get("Personality").getAsJsonObject());
		
		List<TagKey<Miracle>> domains = Lists.newArrayList();
		if(object.has("Domains"))
		{
			JsonArray domainArray = object.get("Domains").getAsJsonArray();
			for(int i=0; i<domainArray.size(); i++)
			{
				String s = domainArray.get(i).getAsString();
				if(s.startsWith("#"))
				{
					ResourceLocation registryName = new ResourceLocation(s.substring(1));
					TagKey<Miracle> domain = TagKey.create(Miracle.REGISTRY_KEY, registryName);
					if(domain != null)
						domains.add(domain);
				}
				else
					throw new JsonSyntaxException("Unknown domain tag '" + s);
			}
		}
		else
			domains = Lists.newArrayList(MiracleTags.GOOD);
		
		long seed = 0L;
		if(object.has("Seed"))
			seed = object.get("Seed").getAsLong();
		
		ItemStack displayItem = new ItemStack(Items.WHEAT);
		if(object.has("Item"))
		{
			try
			{
				displayItem = ItemStack.of(TagParser.parseTag(object.get("Item").getAsString()));
			}
			catch(Exception e) { }
		}
		displayItem.setCount(1);
		
		CommandFunction.CacheableFunction whenSelected = CommandFunction.CacheableFunction.NONE;
		CommandFunction.CacheableFunction whenSelectedFirst = CommandFunction.CacheableFunction.NONE;
		CommandFunction.CacheableFunction whenAbandoned = CommandFunction.CacheableFunction.NONE;
		CommandFunction.CacheableFunction onPray = CommandFunction.CacheableFunction.NONE;
		if(object.has("Functions"))
		{
			JsonObject functions = object.getAsJsonObject("Functions");
			whenSelected = functions.has("WhenSelected") ? new CommandFunction.CacheableFunction(new ResourceLocation(GsonHelper.getAsString(functions, "WhenSelected"))) : CommandFunction.CacheableFunction.NONE;
			whenSelectedFirst = functions.has("WhenSelectedFirst") ? new CommandFunction.CacheableFunction(new ResourceLocation(GsonHelper.getAsString(functions, "WhenSelectedFirst"))) : CommandFunction.CacheableFunction.NONE;
			whenAbandoned = functions.has("WhenAbandoned") ? new CommandFunction.CacheableFunction(new ResourceLocation(GsonHelper.getAsString(functions, "WhenAbandoned"))) : CommandFunction.CacheableFunction.NONE;
			onPray = functions.has("OnPray") ? new CommandFunction.CacheableFunction(new ResourceLocation(GsonHelper.getAsString(functions, "OnPray"))) : CommandFunction.CacheableFunction.NONE;
		}
		
		return new Deity(name, displayName, displayItem, description, personality, seed, domains).setFunctions(onPray, whenSelected, whenSelectedFirst, whenAbandoned);
	}
	
	public JsonObject toJson()
	{
		JsonObject json = new JsonObject();
		
		json.addProperty("DisplayName", Component.Serializer.toJson(this.displayName));
		json.addProperty("Description", Component.Serializer.toJson(blurbText));
		json.addProperty("Item", this.displayItem.serializeNBT().toString());
		json.add("Personality", this.personality.toJson());
		JsonArray domains = new JsonArray();
		this.domains.forEach((domain) -> domains.add("#" + domain.location().toString()));
		json.add("Domains", domains);
		json.addProperty("Seed", this.randSeed);
		
		JsonObject functions = new JsonObject();
		if(this.functionWhenSelected.getId() != null)
			functions.addProperty("WhenSelected", this.functionWhenSelected.getId().toString());
		if(this.functionWhenSelectedFirst.getId() != null)
			functions.addProperty("WhenSelectedFirst", this.functionWhenSelectedFirst.getId().toString());
		if(this.functionWhenAbandoned.getId() != null)
			functions.addProperty("WhenAbandoned", this.functionWhenAbandoned.getId().toString());
		if(this.functionOnPray.getId() != null)
			functions.addProperty("OnPray", this.functionOnPray.getId().toString());
		json.add("Functions", functions);
		return json;
	}
}
