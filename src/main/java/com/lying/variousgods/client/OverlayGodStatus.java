package com.lying.variousgods.client;

import java.util.List;
import java.util.Map.Entry;

import com.lying.variousgods.capabilities.PlayerData;
import com.lying.variousgods.deities.Deity;
import com.lying.variousgods.deities.miracle.Miracle;
import com.lying.variousgods.deities.personality.Opinion;
import com.lying.variousgods.deities.personality.PersonalityContext;
import com.lying.variousgods.deities.personality.PersonalityModel;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.registries.RegistryObject;

public class OverlayGodStatus implements IGuiOverlay
{
	private static final Minecraft mc = Minecraft.getInstance();
	
	public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int width, int height)
	{
		Player player = mc.player;
		PlayerData data = PlayerData.getCapability(player);
		Deity god = data.getDeity();
		
		int textColor = -1;
		int yPos = 5;
		if(god == null)
		{
			ForgeGui.drawString(poseStack, gui.getFont(), "Current deity: NULL", 5, 5, -1);
			return;
		}
		else
		{
			PersonalityModel personality = god.getPersonality();
			Tuple<Double, Double> range = personality.range();
			
			ForgeGui.drawString(poseStack, gui.getFont(), "Current deity: "+god.displayName().getString(), 5, yPos, textColor);
			yPos += gui.getFont().lineHeight;
			
			List<TagKey<Miracle>> domains = god.domains();
			String domainList = "Domains: ";
			for(int i=0; i<domains.size(); i++)
			{
				domainList += domains.get(i).location().getPath().toString();
				if(i < domains.size() - 1)
					domainList += ", ";
			}
			ForgeGui.drawString(poseStack, gui.getFont(), domainList+" ("+god.miracles().size()+")", 5, yPos, textColor);
			yPos += gui.getFont().lineHeight;
			
			ForgeGui.drawString(poseStack, gui.getFont(), "Opinion: "+reducedString((int)(data.getOpinion() * 100))+"% ["+range.getA()+" / "+range.getB()+"]", 5, yPos, textColor);
			yPos += gui.getFont().lineHeight * 2;
			
			ForgeGui.drawString(poseStack, gui.getFont(), "Traits:", 5, yPos, textColor);
			yPos += gui.getFont().lineHeight;
			PersonalityContext context = new PersonalityContext(player);
			for(RegistryObject<Opinion> trait : personality.getTraits())
				if(trait.isPresent())
				{
					ForgeGui.drawString(poseStack, gui.getFont(), trait.getId().toString()+" {"+reducedString(trait.get().value(context))+"}", 15, yPos, textColor);
					yPos += gui.getFont().lineHeight;
				}
			yPos += gui.getFont().lineHeight * 2;
		}
		
		ForgeGui.drawString(poseStack, gui.getFont(), "Miracles: "+data.canHaveMiracle(), 5, yPos, textColor);
		yPos += gui.getFont().lineHeight;
		if(!data.getQuotients().isEmpty())
		{
			ForgeGui.drawString(poseStack, gui.getFont(), "Player quotients:", 5, yPos, textColor);
			yPos += gui.getFont().lineHeight;
			for(Entry<ResourceLocation, Double> entry : data.getQuotients().entrySet())
			{
				if(entry.getValue() == 0D)
					continue;
				ForgeGui.drawString(poseStack, gui.getFont(), entry.getKey().toString()+": "+reducedString(entry.getValue()), 15, yPos, textColor);
				yPos += 11;
			}
		}
	}
	
	private static String reducedString(double valueIn)
	{
		String val = String.valueOf(valueIn);
		return val.length() > 5 ? val.substring(0, 5) : val;
	}
}
