package com.lying.variousgods.data;

import org.jetbrains.annotations.Nullable;

import com.lying.variousgods.init.VGBlocks;
import com.lying.variousgods.reference.Reference;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;

public class VGBlockTags extends BlockTagsProvider
{
    public static final TagKey<Block> NATURE = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Reference.ModInfo.MOD_ID, "nature"));
    public static final TagKey<Block> FIRE = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Reference.ModInfo.MOD_ID, "fire"));
    public static final TagKey<Block> MUSHROOM = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Reference.ModInfo.MOD_ID, "mushroom"));
    public static final TagKey<Block> CROP_BLOCKS = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Reference.ModInfo.MOD_ID, "crop_blocks"));
    public static final TagKey<Block> ORE_BLOCKS = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Reference.ModInfo.MOD_ID, "ore_blocks"));
	
	public VGBlockTags(DataGenerator p_126511_, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(p_126511_, Reference.ModInfo.MOD_ID, existingFileHelper);
	}
	
	public String getName() { return "Various Gods block tags"; }
	
	protected void addTags()
	{
		tag(NATURE)
			.add(Blocks.CACTUS)
			.add(Blocks.LILY_PAD)
			.add(Blocks.VINE)
			.add(Blocks.GLOW_LICHEN)
			.add(Blocks.MOSS_CARPET)
			.add(Blocks.SWEET_BERRY_BUSH)
			.add(Blocks.SUGAR_CANE)
			.add(Blocks.HANGING_ROOTS)
			.add(Blocks.BAMBOO, Blocks.BAMBOO_SAPLING)
			.add(Blocks.BIG_DRIPLEAF, Blocks.SMALL_DRIPLEAF, Blocks.BIG_DRIPLEAF_STEM)
			.add(Blocks.AZALEA, Blocks.AZALEA_LEAVES, Blocks.FLOWERING_AZALEA, Blocks.FLOWERING_AZALEA_LEAVES)
			.add(Blocks.RED_MUSHROOM, Blocks.RED_MUSHROOM_BLOCK, Blocks.MUSHROOM_STEM)
			.add(Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM_BLOCK)
			.addTag(BlockTags.SAPLINGS)
			.addTag(BlockTags.LOGS)
			.addTag(BlockTags.LEAVES)
			.addTag(BlockTags.FLOWERS)
			.addTag(BlockTags.FLOWER_POTS)
			.addTag(BlockTags.BEEHIVES)
			.addTag(BlockTags.CAVE_VINES)
			.addTag(BlockTags.DIRT)
			.addTag(BlockTags.CROPS)
			.addTag(BlockTags.CORAL_PLANTS)
			.addTag(BlockTags.CORALS)
			.addTag(BlockTags.WALL_CORALS)
			.addTag(BlockTags.NYLIUM);
		tag(CROP_BLOCKS)
			.add(Blocks.SUGAR_CANE, Blocks.SWEET_BERRY_BUSH)
			.add(Blocks.MELON, Blocks.PUMPKIN)
			.add(Blocks.WHEAT, Blocks.BEETROOTS, Blocks.CARROTS, Blocks.POTATOES);
		tag(ORE_BLOCKS)
			.add(Blocks.ANCIENT_DEBRIS)
			.addTag(BlockTags.COAL_ORES)
			.addTag(BlockTags.IRON_ORES)
			.addTag(BlockTags.GOLD_ORES)
			.addTag(BlockTags.EMERALD_ORES)
			.addTag(BlockTags.DIAMOND_ORES)
			.addTag(BlockTags.REDSTONE_ORES)
			.addTag(BlockTags.LAPIS_ORES)
			.addTag(BlockTags.COPPER_ORES);
		tag(FIRE)
			.add(Blocks.TORCH, Blocks.REDSTONE_TORCH, Blocks.SOUL_TORCH, Blocks.WALL_TORCH, Blocks.REDSTONE_WALL_TORCH, Blocks.SOUL_WALL_TORCH)
			.add(Blocks.LANTERN, Blocks.SOUL_LANTERN)
			.add(Blocks.JACK_O_LANTERN)
			.add(Blocks.MAGMA_BLOCK)
			.add(Blocks.FURNACE, Blocks.SMOKER, Blocks.BLAST_FURNACE)
			.addTag(BlockTags.CAMPFIRES)
			.addTag(BlockTags.FIRE)
			.addTag(BlockTags.CANDLES);
		tag(MUSHROOM)
			.add(Blocks.RED_MUSHROOM, Blocks.RED_MUSHROOM_BLOCK)
			.add(Blocks.POTTED_BROWN_MUSHROOM, Blocks.POTTED_RED_MUSHROOM)
			.add(Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM_BLOCK, Blocks.MUSHROOM_STEM)
			.add(Blocks.MYCELIUM)
			.add(Blocks.SCULK, Blocks.SCULK_CATALYST, Blocks.SCULK_SENSOR, Blocks.SCULK_SHRIEKER, Blocks.SCULK_VEIN)
			.addTag(BlockTags.NYLIUM)
			.addTag(BlockTags.WART_BLOCKS);
		
		tag(BlockTags.MINEABLE_WITH_AXE).add(VGBlocks.WOODEN_ALTAR.get(), VGBlocks.TOME_ALTAR.get());
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(VGBlocks.CRYSTAL_ALTAR.get(), VGBlocks.GOLDEN_ALTAR.get(), VGBlocks.HOURGLASS_ALTAR.get());
	}
}
