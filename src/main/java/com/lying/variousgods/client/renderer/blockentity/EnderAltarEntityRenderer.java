package com.lying.variousgods.client.renderer.blockentity;

import com.lying.variousgods.block.BlockAltar;
import com.lying.variousgods.block.entity.EnderAltarEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class EnderAltarEntityRenderer implements BlockEntityRenderer<EnderAltarEntity>
{
	public EnderAltarEntityRenderer(BlockEntityRendererProvider.Context context) { }
	
	public void render(EnderAltarEntity altarTile, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferSource, int p_112311_, int p_112312_)
	{
		if(altarTile.timeOpen() > 0)
		{
			BlockState state = altarTile.getBlockState();
			Direction facing = state.getValue(BlockAltar.FACING);
			BlockPos pos = altarTile.getBlockPos();
			Vec3 portalPos = BlockAltar.Ender.getPortalVec(altarTile.getBlockPos(), state).subtract(pos.getX(), pos.getY(), pos.getZ());
			matrixStack.translate(portalPos.x, portalPos.y, portalPos.z);
			matrixStack.pushPose();
				if(facing.getAxis() != Axis.Z)
					matrixStack.mulPose(Vector3f.YP.rotationDegrees(90F));
				matrixStack.mulPose(Vector3f.ZP.rotationDegrees(45F));
				matrixStack.pushPose();
					VertexConsumer buffer = bufferSource.getBuffer(renderType());
					Matrix4f matrix4f = matrixStack.last().pose();
					float scale = 0.35F * ((float)altarTile.timeOpen() / (float)EnderAltarEntity.MAX_OPEN_TIME);
					renderFace(matrix4f, buffer, -0.5F, 0.5F, -0.5F, 0.5F, scale);	// South
					renderFace(matrix4f, buffer, -0.5F, 0.5F, 0.5F, -0.5F, scale);	// North
				matrixStack.popPose();
			matrixStack.popPose();
		}
	}
	
	private void renderFace(Matrix4f matrix, VertexConsumer buffer, float minX, float maxX, float minY, float maxY, float scale)
	{
		minX *= scale;
		maxX *= scale;
		minY *= scale;
		maxY *= scale;
		
		buffer.vertex(matrix, minX, minY, 0F).endVertex();
		buffer.vertex(matrix, maxX, minY, 0F).endVertex();
		buffer.vertex(matrix, maxX, maxY, 0F).endVertex();
		buffer.vertex(matrix, minX, maxY, 0F).endVertex();
	}
	
	protected RenderType renderType() { return RenderType.endPortal(); }
}
