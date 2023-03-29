package com.lying.variousgods.data;

import java.util.function.Consumer;

import com.lying.variousgods.init.VGItems;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
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
	}
	
	public String getName()
	{
		return "Various Gods crafting recipes";
	}
}
