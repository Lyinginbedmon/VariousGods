package com.lying.variousgods.client.renderer.blockentity;

import com.lying.variousgods.block.BlockAltar;
import com.lying.variousgods.block.entity.BloodAltarEntity;
import com.lying.variousgods.init.VGItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BloodAltarEntityRenderer implements BlockEntityRenderer<BloodAltarEntity>
{
	public BloodAltarEntityRenderer(BlockEntityRendererProvider.Context p_i226017_1_) { }
	
	public void render(BloodAltarEntity altarTile, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferSource, int p_112311_, int p_112312_)
	{
		BlockState state = altarTile.getBlockState();
		matrixStack.pushPose();
			matrixStack.translate(0.5D, 0.0D, 0.5D);
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(-state.getValue(BlockAltar.FACING).getClockWise().toYRot()));
			matrixStack.translate(0.25D, 0D, 0D);
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(45F));
			matrixStack.mulPose(Vector3f.XP.rotationDegrees(90F));
			float scale = 0.5F;
			matrixStack.scale(scale, scale, scale);
			
			ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
			ItemStack knife = new ItemStack(VGItems.SOUL_KNIFE.get());
			BakedModel knifeModel = itemRenderer.getModel(knife, altarTile.getLevel(), (LivingEntity)null, 0);
			itemRenderer.render(knife, TransformType.FIXED, false, matrixStack, bufferSource, p_112311_, OverlayTexture.NO_OVERLAY, knifeModel);
		matrixStack.popPose();
	}
}
