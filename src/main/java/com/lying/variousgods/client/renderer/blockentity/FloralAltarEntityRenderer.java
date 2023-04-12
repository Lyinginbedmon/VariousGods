package com.lying.variousgods.client.renderer.blockentity;

import com.lying.variousgods.block.BlockAltar;
import com.lying.variousgods.block.entity.FloralAltarEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public class FloralAltarEntityRenderer implements BlockEntityRenderer<FloralAltarEntity>
{
	private static final BlockState BLOCK_POPPY = Blocks.POPPY.defaultBlockState();
	private static final BlockState BLOCK_TULIP = Blocks.PINK_TULIP.defaultBlockState();
	private static final BlockState BLOCK_DANDELION = Blocks.DANDELION.defaultBlockState();
	private static final BlockState BLOCK_LILY_OF_THE_VALLEY = Blocks.LILY_OF_THE_VALLEY.defaultBlockState();
	private static final BlockState BLOCK_AZURE_BLUET = Blocks.AZURE_BLUET.defaultBlockState();
	private static final BlockState BLOCK_BLUE_ORCHID = Blocks.BLUE_ORCHID.defaultBlockState();
	
	private final BlockRenderDispatcher blockRenderer;
	
	public FloralAltarEntityRenderer(BlockEntityRendererProvider.Context context)
	{
		this.blockRenderer = context.getBlockRenderDispatcher();
	}
	
	public void render(FloralAltarEntity altarTile, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferSource, int p_112311_, int p_112312_)
	{
		BlockState state = altarTile.getBlockState();
		if(state.getValue(BlockAltar.FACING).getAxis() == Axis.X)
		{
			matrixStack.translate(1D, 0D, 0D);
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(-90F));
		}
		
		renderFlower(matrixStack, bufferSource, BLOCK_POPPY,				0.0D,	0.0D,	p_112311_, p_112312_);
		renderFlower(matrixStack, bufferSource, BLOCK_TULIP,				-3.0D,	-3.0D,	p_112311_, p_112312_);
		renderFlower(matrixStack, bufferSource, BLOCK_AZURE_BLUET,			2.5D,	2.0D,	p_112311_, p_112312_);
		renderFlower(matrixStack, bufferSource, BLOCK_DANDELION,			-4.5D,	-0.5D,	p_112311_, p_112312_);
		renderFlower(matrixStack, bufferSource, BLOCK_BLUE_ORCHID,			4.0D,	-2.0D,	p_112311_, p_112312_);
		renderFlower(matrixStack, bufferSource, BLOCK_LILY_OF_THE_VALLEY,	-4.0D,	3.0D,	p_112311_, p_112312_);
	}
	
	private void renderFlower(PoseStack matrixStack, MultiBufferSource bufferSource, BlockState state, double x, double z, int par6Int, int par7Int)
	{
		matrixStack.pushPose();
			matrixStack.translate(x * (1/16D), 0.21875D, z * (1/16D));
			matrixStack.translate(0.5D, 0D, 0.5D);
			float scale = 0.5F;
			matrixStack.scale(scale, scale, scale);
			matrixStack.translate(-0.5D, 0D, -0.5D);
			this.blockRenderer.renderSingleBlock(state, matrixStack, bufferSource, par6Int, par7Int, ModelData.EMPTY, RenderType.cutout());
		matrixStack.popPose();
	}
}
