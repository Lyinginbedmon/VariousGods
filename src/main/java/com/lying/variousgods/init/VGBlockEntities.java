package com.lying.variousgods.init;

import com.lying.variousgods.block.entity.BloodAltarEntity;
import com.lying.variousgods.block.entity.TomeAltarEntity;
import com.lying.variousgods.reference.Reference;

import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Reference.ModInfo.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class VGBlockEntities
{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Reference.ModInfo.MOD_ID);
    
	public static final RegistryObject<BlockEntityType<TomeAltarEntity>> TOME_ALTAR = BLOCK_ENTITIES.register("tome_altar", () -> BlockEntityType.Builder.of(TomeAltarEntity::new, VGBlocks.TOME_ALTAR.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "tome_altar")));
	public static final RegistryObject<BlockEntityType<BloodAltarEntity>> BLOOD_ALTAR = BLOCK_ENTITIES.register("blood_altar", () -> BlockEntityType.Builder.of(BloodAltarEntity::new, VGBlocks.BLOOD_ALTAR.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "blood_altar")));
}
