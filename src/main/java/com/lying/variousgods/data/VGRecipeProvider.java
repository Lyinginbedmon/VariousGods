package com.lying.variousgods.data;

import java.util.function.Consumer;

import com.lying.variousgods.init.VGItems;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

public class VGRecipeProvider extends RecipeProvider
{
	private static final String ALTAR_GROUP = "altars";
	
	public VGRecipeProvider(DataGenerator generatorIn)
	{
		super(generatorIn);
	}
	
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer)
	{
		ShapedRecipeBuilder.shaped(VGItems.STONE_ALTAR_ITEM.get())
			.pattern(" S ")
			.pattern("CCC")
			.define('S', Blocks.SMOOTH_STONE_SLAB)
			.define('C', Tags.Items.COBBLESTONE)
			.group(ALTAR_GROUP)
			.unlockedBy("has_stone", has(Tags.Items.COBBLESTONE)).save(consumer);
		
		ShapedRecipeBuilder.shaped(VGItems.GOLDEN_ALTAR_ITEM.get())
			.pattern("III")
			.pattern("Q Q")
			.define('I', Tags.Items.INGOTS_GOLD)
			.define('Q', Tags.Items.GEMS_QUARTZ)
			.group(ALTAR_GROUP)
			.unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD)).save(consumer);
		
		ShapedRecipeBuilder.shaped(VGItems.BONE_ALTAR_ITEM.get())
			.pattern("SC")
			.define('S', Tags.Items.HEADS)
			.define('C', ItemTags.CANDLES)
			.group(ALTAR_GROUP)
			.unlockedBy("has_skull", has(Tags.Items.HEADS)).save(consumer);
		
		ShapedRecipeBuilder.shaped(VGItems.HOURGLASS_ALTAR_ITEM.get())
			.pattern("IGI")
			.pattern(" I ")
			.pattern("ISI")
			.define('I', Tags.Items.INGOTS_GOLD)
			.define('G', Tags.Items.GLASS)
			.define('S', Tags.Items.SAND)
			.group(ALTAR_GROUP)
			.unlockedBy("has_sand", has(Tags.Items.SAND)).save(consumer);
		
		ShapedRecipeBuilder.shaped(VGItems.WOODEN_ALTAR_ITEM.get())
			.pattern("L")
			.pattern("S")
			.define('L', ItemTags.LOGS)
			.define('S', ItemTags.WOODEN_SLABS)
			.group(ALTAR_GROUP)
			.unlockedBy("has_wood", has(ItemTags.PLANKS)).save(consumer);
		
		ShapedRecipeBuilder.shaped(VGItems.CRYSTAL_ALTAR_ITEM.get())
			.pattern("C")
			.pattern("S")
			.define('C', Tags.Items.GEMS_AMETHYST)
			.define('S', ItemTags.SLABS)
			.group(ALTAR_GROUP)
			.unlockedBy("has_amethyst", has(Tags.Items.GEMS_AMETHYST)).save(consumer);
		
		ShapedRecipeBuilder.shaped(VGItems.TOME_ALTAR_ITEM.get())
			.pattern("B")
			.pattern("S")
			.define('B', Items.BOOK)
			.define('S', ItemTags.WOODEN_SLABS)
			.group(ALTAR_GROUP)
			.unlockedBy("has_book", has(Items.BOOK)).save(consumer);
		
		ShapedRecipeBuilder.shaped(VGItems.WINGED_ALTAR_ITEM.get())
			.pattern("CBC")
			.pattern(" S ")
			.define('B', Items.QUARTZ_BLOCK)
			.define('C', Items.QUARTZ)
			.define('S', Items.QUARTZ_SLAB)
			.group(ALTAR_GROUP)
			.unlockedBy("has_quartz", has(Items.QUARTZ)).save(consumer);
		
		ShapelessRecipeBuilder.shapeless(VGItems.BLOOD_ALTAR_ITEM.get())
			.requires(Items.DEEPSLATE)
			.requires(Items.IRON_SWORD)
			.requires(Items.GLASS_BOTTLE)
			.group(ALTAR_GROUP)
			.unlockedBy("has_deepslate", has(Items.DEEPSLATE)).save(consumer);
		
		ShapedRecipeBuilder.shaped(VGItems.BLAZE_ALTAR_ITEM.get())
			.pattern("F")
			.pattern("B")
			.define('F', Items.FIRE_CHARGE)
			.define('B', Items.NETHER_BRICK_SLAB)
			.group(ALTAR_GROUP)
			.unlockedBy("has_nether_brick", has(Items.NETHER_BRICK)).save(consumer);
		
		ShapedRecipeBuilder.shaped(VGItems.REDSTONE_ALTAR_ITEM.get())
			.pattern("TLT")
			.pattern("SSS")
			.define('T', Items.REDSTONE_TORCH)
			.define('L', Items.LEVER)
			.define('S', Items.SMOOTH_STONE_SLAB)
			.group(ALTAR_GROUP)
			.unlockedBy("has_redstone_dust", has(Tags.Items.DUSTS_REDSTONE)).save(consumer);
		
		ShapedRecipeBuilder.shaped(VGItems.ENDER_ALTAR_ITEM.get())
			.pattern("R R")
			.pattern(" P ")
			.pattern("SSS")
			.define('R', Items.END_ROD)
			.define('P', Items.PURPUR_SLAB)
			.define('S', Items.END_STONE)
			.group(ALTAR_GROUP)
			.unlockedBy("has_purpur", has(Tags.Items.END_STONES)).save(consumer);
		
		ShapedRecipeBuilder.shaped(VGItems.MUSHROOM_ALTAR_ITEM.get())
			.pattern("RCB")
			.define('R', Items.RED_MUSHROOM)
			.define('C', Items.RED_MUSHROOM_BLOCK)
			.define('B', Items.BROWN_MUSHROOM)
			.group(ALTAR_GROUP)
			.unlockedBy("has_mushroom", has(Tags.Items.MUSHROOMS)).save(consumer);
		
		ShapedRecipeBuilder.shaped(VGItems.OVERGROWN_ALTAR_ITEM.get())
			.pattern("M")
			.pattern("S")
			.define('M', Tags.Items.COBBLESTONE_MOSSY)
			.define('S', Items.COBBLESTONE_SLAB)
			.group(ALTAR_GROUP)
			.unlockedBy("has_mossy", has(Tags.Items.COBBLESTONE_MOSSY)).save(consumer);
		
		ShapedRecipeBuilder.shaped(VGItems.FOUNTAIN_ALTAR_ITEM.get())
			.pattern("PBP")
			.pattern("PPP")
			.define('P', Items.PRISMARINE_SHARD)
			.define('B', Items.WATER_BUCKET)
			.group(ALTAR_GROUP)
			.unlockedBy("has_prismarine", has(Items.PRISMARINE_SHARD)).save(consumer);
		
		ShapedRecipeBuilder.shaped(VGItems.FLORAL_ALTAR_ITEM.get())
			.pattern("FFF")
			.pattern("SSS")
			.define('F', ItemTags.FLOWERS)
			.define('S', ItemTags.WOODEN_SLABS)
			.group(ALTAR_GROUP)
			.unlockedBy("has_flowers", has(ItemTags.FLOWERS)).save(consumer);
	}
	
	public String getName()
	{
		return "Various Gods crafting recipes";
	}
}
