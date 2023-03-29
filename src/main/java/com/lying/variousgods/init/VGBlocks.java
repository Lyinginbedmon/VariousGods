package com.lying.variousgods.init;

import com.lying.variousgods.block.BlockAltar;
import com.lying.variousgods.block.BlockReaperBag;
import com.lying.variousgods.reference.Reference;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class VGBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.ModInfo.MOD_ID);
    
    public static final RegistryObject<Block> REAPER_BAG = BLOCKS.register("reaper_bag", () -> new BlockReaperBag(BlockBehaviour.Properties.of(Material.AIR).strength(-1.0F, 3600000.8F).noLootTable().noOcclusion()));
    public static final RegistryObject<Block> STONE_ALTAR = BLOCKS.register("stone_altar", () -> new BlockAltar.Stone(BlockBehaviour.Properties.of(Material.STONE).strength(2F).noOcclusion()));
    public static final RegistryObject<Block> HOURGLASS_ALTAR = BLOCKS.register("hourglass_altar", () -> new BlockAltar.Hourglass(BlockBehaviour.Properties.of(Material.METAL).strength(2F).noOcclusion()));
    public static final RegistryObject<Block> GOLDEN_ALTAR = BLOCKS.register("golden_altar", () -> new BlockAltar.Golden(BlockBehaviour.Properties.of(Material.METAL).strength(2F).noOcclusion()));
    public static final RegistryObject<Block> BONE_ALTAR = BLOCKS.register("bone_altar", () -> new BlockAltar.Bone(BlockBehaviour.Properties.of(Material.STONE).strength(2F).noOcclusion()));
    public static final RegistryObject<Block> WOODEN_ALTAR = BLOCKS.register("wooden_altar", () -> new BlockAltar.Wooden(BlockBehaviour.Properties.of(Material.WOOD).strength(2F, 3F).noOcclusion()));
    public static final RegistryObject<Block> CRYSTAL_ALTAR = BLOCKS.register("crystal_altar", () -> new BlockAltar.Crystal(BlockBehaviour.Properties.of(Material.AMETHYST).noOcclusion()));
    public static final RegistryObject<Block> TOME_ALTAR = BLOCKS.register("tome_altar", () -> new BlockAltar.Tome(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion()));
    
    public static void init() { }
}
