package com.lying.variousgods.client.renderer.blockentity;

import com.lying.variousgods.block.BlockAltar;
import com.lying.variousgods.block.entity.TomeAltarEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.EnchantTableRenderer;
import net.minecraft.world.level.block.state.BlockState;

public class TomeAltarEntityRenderer implements BlockEntityRenderer<TomeAltarEntity>
{
	private final BookModel bookModel;
	
	public TomeAltarEntityRenderer(BlockEntityRendererProvider.Context p_i226017_1_)
	{
		this.bookModel = new BookModel(p_i226017_1_.bakeLayer(ModelLayers.BOOK));
	}
	
	public void render(TomeAltarEntity altarTile, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferSource, int p_112311_, int p_112312_)
	{
		BlockState state = altarTile.getBlockState();
		if(!state.getValue(BlockAltar.WATERLOGGED))
		{
			float facing = state.getValue(BlockAltar.FACING).getClockWise().toYRot();
			matrixStack.pushPose();
				matrixStack.translate(0.5D, 0.3125D, 0.5D);
				matrixStack.mulPose(Vector3f.YP.rotationDegrees(-facing));
				matrixStack.mulPose(Vector3f.ZP.rotationDegrees(67.5F));
				matrixStack.translate(0D, -0.125D, 0D);
				if(state.getValue(BlockAltar.PRAYING))
				{
					this.bookModel.setupAnim(0F, 0.1F, 0.9F, 1.2F);
					VertexConsumer consumer = EnchantTableRenderer.BOOK_LOCATION.buffer(bufferSource, RenderType::entitySolid);
					this.bookModel.render(matrixStack, consumer, p_112311_, p_112312_, 1F, 1F, 1F, 1F);
				}
				else
				{
					matrixStack.pushPose();
						matrixStack.translate(0.05D, 0D, 0.2D);
						matrixStack.mulPose(Vector3f.YP.rotationDegrees(90F));
						this.bookModel.setupAnim(0F, 0.1F, 0.9F, 0F);
						VertexConsumer consumer = EnchantTableRenderer.BOOK_LOCATION.buffer(bufferSource, RenderType::entitySolid);
						this.bookModel.render(matrixStack, consumer, p_112311_, p_112312_, 1F, 1F, 1F, 1F);
					matrixStack.popPose();
				}
			matrixStack.popPose();
		}
	}
}
