package com.lying.variousgods.init;

import com.lying.variousgods.reference.Reference;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class VGItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.ModInfo.MOD_ID);
    
    public static final RegistryObject<Item> REAPER_BAG_ITEM = ITEMS.register("reaper_bag", () -> new BlockItem(VGBlocks.REAPER_BAG.get(), new Item.Properties()));
    public static final RegistryObject<Item> SOUL_KNIFE = ITEMS.register("soul_knife", () -> new SwordItem(Tiers.NETHERITE, 3, -3.2F, new Item.Properties()));
    
    public static final RegistryObject<Item> STONE_ALTAR_ITEM = ITEMS.register("stone_altar", () -> new BlockItem(VGBlocks.STONE_ALTAR.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> HOURGLASS_ALTAR_ITEM = ITEMS.register("hourglass_altar", () -> new BlockItem(VGBlocks.HOURGLASS_ALTAR.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> GOLDEN_ALTAR_ITEM = ITEMS.register("golden_altar", () -> new BlockItem(VGBlocks.GOLDEN_ALTAR.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> BONE_ALTAR_ITEM = ITEMS.register("bone_altar", () -> new BlockItem(VGBlocks.BONE_ALTAR.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> WOODEN_ALTAR_ITEM = ITEMS.register("wooden_altar", () -> new BlockItem(VGBlocks.WOODEN_ALTAR.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> CRYSTAL_ALTAR_ITEM = ITEMS.register("crystal_altar", () -> new BlockItem(VGBlocks.CRYSTAL_ALTAR.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> TOME_ALTAR_ITEM = ITEMS.register("tome_altar", () -> new BlockItem(VGBlocks.TOME_ALTAR.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> WINGED_ALTAR_ITEM = ITEMS.register("winged_altar", () -> new BlockItem(VGBlocks.WINGED_ALTAR.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> BLOOD_ALTAR_ITEM = ITEMS.register("blood_altar", () -> new BlockItem(VGBlocks.BLOOD_ALTAR.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> BLAZE_ALTAR_ITEM = ITEMS.register("blaze_altar", () -> new BlockItem(VGBlocks.BLAZE_ALTAR.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> REDSTONE_ALTAR_ITEM = ITEMS.register("redstone_altar", () -> new BlockItem(VGBlocks.REDSTONE_ALTAR.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> ENDER_ALTAR_ITEM = ITEMS.register("ender_altar", () -> new BlockItem(VGBlocks.ENDER_ALTAR.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> MUSHROOM_ALTAR_ITEM = ITEMS.register("mushroom_altar", () -> new BlockItem(VGBlocks.MUSHROOM_ALTAR.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> OVERGROWN_ALTAR_ITEM = ITEMS.register("overgrown_altar", () -> new BlockItem(VGBlocks.OVERGROWN_ALTAR.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> FOUNTAIN_ALTAR_ITEM = ITEMS.register("fountain_altar", () -> new BlockItem(VGBlocks.FOUNTAIN_ALTAR.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> FLORAL_ALTAR_ITEM = ITEMS.register("floral_altar", () -> new BlockItem(VGBlocks.FLORAL_ALTAR.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    
	public static Item register(String nameIn, Item itemIn)
	{
		ITEMS.register(Reference.ModInfo.MOD_ID+"."+nameIn, () -> itemIn);
		return itemIn;
	}
}
