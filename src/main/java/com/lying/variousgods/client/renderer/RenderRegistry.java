package com.lying.variousgods.client.renderer;

import com.lying.variousgods.client.VGModelLayers;
import com.lying.variousgods.client.model.ModelHearthLightIndicator;
import com.lying.variousgods.client.model.ModelHearthLightLantern;
import com.lying.variousgods.client.renderer.blockentity.BloodAltarEntityRenderer;
import com.lying.variousgods.client.renderer.blockentity.EnderAltarEntityRenderer;
import com.lying.variousgods.client.renderer.blockentity.TomeAltarEntityRenderer;
import com.lying.variousgods.client.renderer.entity.EntityHearthLightRenderer;
import com.lying.variousgods.init.VGBlockEntities;
import com.lying.variousgods.init.VGEntities;
import com.lying.variousgods.reference.Reference;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Reference.ModInfo.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RenderRegistry
{
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerEntityRenderer(VGEntities.HEARTH_LIGHT.get(), EntityHearthLightRenderer::new);
		event.registerEntityRenderer(VGEntities.GUARD_ZOMBIE.get(), ZombieRenderer::new);
		event.registerEntityRenderer(VGEntities.GUARD_SKELETON.get(), SkeletonRenderer::new);
	}
	
	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
	{
		event.registerLayerDefinition(VGModelLayers.HEARTH_LANTERN, () -> ModelHearthLightLantern.createBodyLayer());
		event.registerLayerDefinition(VGModelLayers.HEARTH_INDICATOR, () -> ModelHearthLightIndicator.createBodyLayer());
	}
	
	@SubscribeEvent
	public static void registerTileRenderers(ModelEvent.RegisterAdditional event)
	{
		BlockEntityRenderers.register(VGBlockEntities.TOME_ALTAR.get(), TomeAltarEntityRenderer::new);
		BlockEntityRenderers.register(VGBlockEntities.BLOOD_ALTAR.get(), BloodAltarEntityRenderer::new);
		BlockEntityRenderers.register(VGBlockEntities.ENDER_ALTAR.get(), EnderAltarEntityRenderer::new);
	}
}
