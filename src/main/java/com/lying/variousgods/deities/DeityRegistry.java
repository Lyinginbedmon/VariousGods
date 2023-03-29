package com.lying.variousgods.deities;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.compress.utils.Lists;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.lying.variousgods.VariousGods;
import com.lying.variousgods.deities.miracle.Miracle;
import com.lying.variousgods.deities.personality.PersonalityModel;
import com.lying.variousgods.deities.personality.PersonalityTraits;
import com.lying.variousgods.init.MiracleTags;
import com.lying.variousgods.reference.Reference;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class DeityRegistry extends SimpleJsonResourceReloadListener
{
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	public static final Deity APOSTATE = new Deity("apostate", Component.translatable("deity."+Reference.ModInfo.MOD_ID+".apostate_name"), new ItemStack(Blocks.PLAYER_HEAD), Component.translatable("deity."+Reference.ModInfo.MOD_ID+".apostate_desc"), new PersonalityModel(), 17218L, null);
	
	private static DeityRegistry instance;
	
	public DeityRegistry()
	{
		super(GSON, "deities");
	}
	
	public static DeityRegistry getInstance()
	{
		if(instance == null)
			instance = new DeityRegistry();
		return instance;
	}
	
	private static final List<Deity> DEFAULT_DEITIES = Lists.newArrayList();
	
	private Map<String, Deity> deities = new HashMap<>();
	
	public int getDeityCount() { return this.deities.size(); }
	
	public Set<String> getDeityNames(){ return this.deities.keySet(); }
	
	public Collection<Deity> getDeities(){ return this.deities.values(); }
	
	public Deity getDeity(String simpleName) { return deities.getOrDefault(simpleName, APOSTATE); }
	
	public void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager manager, ProfilerFiller filler)
	{
		VariousGods.LOG.info("Attempting to load deities from data, entries: "+objectIn.size());
		Map<String, Deity> loaded = new HashMap<>();
		objectIn.forEach((name, json) -> {
            try
            {
            	Deity builder = Deity.fromJson(name.getPath(), json);
                if(builder != null)
                {
                	String domains = " [";
                	List<TagKey<Miracle>> domainList = builder.domains();
                	for(int i=0; i<domainList.size(); i++)
                	{
                		domains += domainList.get(i).location().getPath();
                		if(i < domainList.size() - 1)
                			domains += ", ";
                		else
                			domains += "]";
                	}
                	VariousGods.LOG.info(" -Loaded: "+builder.simpleName() + domains + "["+builder.miracles().size()+"]");
                    loaded.put(builder.simpleName(), builder);
                }
            }
            catch (IllegalArgumentException | JsonParseException e)
            {
            	VariousGods.LOG.error("Failed to load deity {}: {}", name);
            }
            catch(Exception e)
            {
            	VariousGods.LOG.error("Unrecognised error loading deity {}", name);
            }
        });
		
		// If no gods were found in the datapack, load the defaults
		if(loaded.isEmpty())
		{
			VariousGods.LOG.warn("No deities found, loading defaults");
			DEFAULT_DEITIES.forEach((god) -> loaded.put(god.simpleName(), god));
		}
		
		deities.clear();
		loaded.forEach((name,deity) -> deities.put(name, deity));
		deities.put(APOSTATE.simpleName(), APOSTATE);
	}
	
	@SafeVarargs
	private static void addDefault(String simple, ItemStack displayItem, PersonalityModel personality, long seed, TagKey<Miracle>... domainsIn)
	{
		List<TagKey<Miracle>> domains = Lists.newArrayList();
		for(int i=0; i<domainsIn.length; i++)
			domains.add(domainsIn[i]);
		
		DEFAULT_DEITIES.add(new Deity(simple, displayItem, personality, seed, domains));
	}
	
	@SafeVarargs
	private static void addDefault(String simple, ItemStack displayItem, long seed, TagKey<Miracle>... domains)
	{
		addDefault(simple, displayItem, new PersonalityModel(), seed, domains);
	}
	
	public static List<Deity> getDefaultDeities() { return DEFAULT_DEITIES; }
	
	static
	{
		addDefault("acinum", new ItemStack(Items.HEART_OF_THE_SEA), new PersonalityModel(List.of(PersonalityTraits.ZOOLATER, PersonalityTraits.SEAFARER, PersonalityTraits.PESCETARIAN)), 46654, MiracleTags.WATER);
		addDefault("aeneas", new ItemStack(Items.OAK_BOAT), new PersonalityModel(List.of(PersonalityTraits.WANDERER, PersonalityTraits.INVENTOR)), 5388, MiracleTags.TRAVEL, MiracleTags.CREATION, MiracleTags.WATER);
		addDefault("basilla", new ItemStack(Blocks.CRACKED_STONE_BRICKS), new PersonalityModel(List.of(PersonalityTraits.HOMEBODY, PersonalityTraits.AGRIPHOBIC, PersonalityTraits.INVENTOR)), 5391, MiracleTags.PROTECTION, MiracleTags.CREATION, MiracleTags.EARTH);
		addDefault("erinus", new ItemStack(Blocks.MOSS_BLOCK), new PersonalityModel(List.of(PersonalityTraits.BRIGHT, PersonalityTraits.DRUID, PersonalityTraits.ZOOLATER, PersonalityTraits.VEGETARIAN)), 66091, MiracleTags.ANIMAL, MiracleTags.PLANT);
		addDefault("etronicus", new ItemStack(Blocks.SKELETON_SKULL), new PersonalityModel(List.of(PersonalityTraits.SHADOW, PersonalityTraits.PESSIMIST)), 34031, MiracleTags.DEATH, MiracleTags.DARKNESS);
		addDefault("flying", new ItemStack(Blocks.BELL), 17218, MiracleTags.LAW);
		addDefault("moriboca", new ItemStack(Blocks.MAGMA_BLOCK), new PersonalityModel(List.of(PersonalityTraits.BRUTE, PersonalityTraits.BLACKSMITH, PersonalityTraits.PYROMANIAC, PersonalityTraits.STEELSKIN)), 77384, MiracleTags.FIRE, MiracleTags.STRENGTH, MiracleTags.WAR);
		addDefault("philopos", new ItemStack(Items.ENDER_EYE), new PersonalityModel(List.of(PersonalityTraits.WANDERER, PersonalityTraits.SCHOLAR)), 85134, MiracleTags.LIGHT, MiracleTags.KNOWLEDGE);
		addDefault("phoenix", new ItemStack(Items.FEATHER), new PersonalityModel(List.of(PersonalityTraits.ARCHER, PersonalityTraits.WANDERER, PersonalityTraits.CLOUDWATCHER)), 19214, MiracleTags.AIR, MiracleTags.TRAVEL);
		addDefault("placitos", new ItemStack(Items.ENCHANTED_BOOK), new PersonalityModel(List.of(PersonalityTraits.SCHOLAR, PersonalityTraits.HOMEBODY)), 66732, MiracleTags.MAGIC, MiracleTags.KNOWLEDGE);
		addDefault("urlin", new ItemStack(Items.RED_MUSHROOM), new PersonalityModel(List.of(PersonalityTraits.ZOOLATER, PersonalityTraits.MYCOLOGIST)), 2847, MiracleTags.ANIMAL, MiracleTags.CHAOS);
		addDefault("lying", new ItemStack(Items.WRITABLE_BOOK), new PersonalityModel(List.of(PersonalityTraits.SCHOLAR)), 2847, MiracleTags.KNOWLEDGE, MiracleTags.TRICKERY);
	}
}
