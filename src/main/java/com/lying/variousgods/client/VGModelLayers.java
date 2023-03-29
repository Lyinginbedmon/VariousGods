package com.lying.variousgods.client;

import com.lying.variousgods.reference.Reference;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VGModelLayers
{
	public static final ModelLayerLocation HEARTH_LANTERN				= new ModelLayerLocation(new ResourceLocation(Reference.ModInfo.MOD_ID, "hearth_lantern"), "main");
	public static final ModelLayerLocation HEARTH_INDICATOR				= new ModelLayerLocation(new ResourceLocation(Reference.ModInfo.MOD_ID, "hearth_indicator"), "main");
}
