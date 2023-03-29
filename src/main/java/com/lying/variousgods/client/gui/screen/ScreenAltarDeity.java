package com.lying.variousgods.client.gui.screen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.compress.utils.Lists;

import com.lying.variousgods.client.gui.menu.MenuAltar;
import com.lying.variousgods.client.gui.menu.MenuAltarDeity;
import com.lying.variousgods.deities.Deity;
import com.lying.variousgods.deities.DeityRegistry;
import com.lying.variousgods.reference.Reference;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class ScreenAltarDeity extends ScreenAltar<MenuAltarDeity> implements MenuAccess<MenuAltar>
{
	private static final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
	private static final RandomSource rand = RandomSource.create();
	private static final int DESC_WIDTH = 250;
	private int index = 0;
	
	private Button incIndexButton, decIndexButton;
	private Button chooseDeityButton;
	
	public ScreenAltarDeity(MenuAltar altarIn, Inventory inv, Component displayName)
	{
		super((MenuAltarDeity)altarIn, inv, displayName);
		index = rand.nextInt(DeityRegistry.getInstance().getDeityCount());
	}
	
	protected void init()
	{
        this.addRenderableWidget(decIndexButton = new Button(this.width / 2 - 100, 45, 20, 20, Component.literal("<"), (button) -> {
        	index--;
        	if(index < 0)
        		index = DeityRegistry.getInstance().getDeityCount() - 1;
        	updateButtons();
        }));
        
        this.addRenderableWidget(incIndexButton = new Button(this.width / 2 + 2, 45, 20, 20, Component.literal(">"), (button) -> {
	        index++;
	        if(index >= DeityRegistry.getInstance().getDeityCount())
	        	index = 0;
	 		updateButtons();
        }));
        
        this.addRenderableWidget(this.chooseDeityButton = new Button(0, 45, 10, 20, Component.empty(), (button) -> 
        {
            altarMenu.openAltarMenu(minecraft.player, currentDeity().simpleName());
        }));
        
		updateButtons();
	}
	
	private List<String> deityNames()
	{
		Map<String, String> fullToSimple = new HashMap<>();
		DeityRegistry.getInstance().getDeities().forEach((god) -> fullToSimple.put(god.displayName().getString(), god.simpleName()));
		
		List<String> fullNames = Lists.newArrayList(); 
		fullNames.addAll(fullToSimple.keySet());
		fullNames.sort(String.CASE_INSENSITIVE_ORDER);
		
		List<String> names = Lists.newArrayList();
		fullNames.forEach((god) -> names.add(fullToSimple.get(god)));
		return names;
	}
	
	private Deity currentDeity()
	{
		return getDeity(index);
	}
	
	private Deity getDeity(int indexIn)
	{
		while(indexIn < 0)
			indexIn += DeityRegistry.getInstance().getDeityCount();
		
		indexIn %= DeityRegistry.getInstance().getDeityCount();
		
		return DeityRegistry.getInstance().getDeity(deityNames().get(indexIn));
	}
	
	private void updateButtons()
	{
		Deity current = currentDeity();
		this.chooseDeityButton.setMessage(current.displayName());
		this.chooseDeityButton.setWidth(this.font.width(current.displayName()) + 20);
		this.chooseDeityButton.x = (this.width - this.chooseDeityButton.getWidth()) / 2;
		
		int offset = Math.max(this.chooseDeityButton.getWidth(), 200);
		this.decIndexButton.x = (this.width - offset) / 2 - this.decIndexButton.getWidth() - 10;
		this.incIndexButton.x = (this.width + offset) / 2 + 10;
	}
	
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderDirtBackground(0);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		
		Deity current = currentDeity();
		ItemStack displayItem = current.getDisplayItem();
		int itemX = (this.width - 20) / 2;
		itemRenderer.renderAndDecorateFakeItem(displayItem, itemX, 25);
		
		int items = Math.min(6, DeityRegistry.getInstance().getDeityCount() / 2);
		for(int i=1; i<items; i++)
		{
			itemRenderer.renderAndDecorateFakeItem(getDeity(index + i).getDisplayItem(), itemX + 40 + (i * 20) + (i-1 * 15), 25);
			itemRenderer.renderAndDecorateFakeItem(getDeity(index - i).getDisplayItem(), itemX - 40 - (i * 20) - (i-1 * 15), 25);
		}
		
		renderLabels(matrixStack, mouseX, mouseY);
		
		if(this.decIndexButton.isHoveredOrFocused())
			this.renderTooltip(matrixStack, getDeity(index - 1).displayName(), mouseX, mouseY);
		else if(this.incIndexButton.isHoveredOrFocused())
			this.renderTooltip(matrixStack, getDeity(index + 1).displayName(), mouseX, mouseY);
		else if(this.chooseDeityButton.isHoveredOrFocused())
			this.renderTooltip(matrixStack, Component.translatable("container."+Reference.ModInfo.MOD_ID+".altar.choose_god.select", currentDeity().displayName()), mouseX, mouseY);
	}
	
	public void renderLabels(PoseStack matrixStack, int mouseX, int mouseY)
	{
		Deity current = currentDeity();
		int yLevel = this.chooseDeityButton.y + this.chooseDeityButton.getHeight() / 2;
		yLevel += this.font.lineHeight + 4;
		
		List<String> domainNames = Lists.newArrayList();
		if(!current.domains().isEmpty())
		{
			current.domains().forEach((domain) -> domainNames.add(domain.location().getPath()));
			domainNames.sort(String.CASE_INSENSITIVE_ORDER);
			String domainList = String.join(", ", domainNames);
			Component domainDisplay = Component.translatable("container."+Reference.ModInfo.MOD_ID+".altar.choose_god.domains", domainList);
			this.font.draw(matrixStack, domainDisplay, (this.width - this.font.width(domainDisplay)) / 2, yLevel, -1);
		}
		yLevel += this.font.lineHeight * 2 + 8;
		for(FormattedCharSequence line : this.font.split(current.description(), DESC_WIDTH))
			this.font.draw(matrixStack, line, (this.width - this.font.width(line)) / 2, yLevel += this.font.lineHeight, -1);
	}
}
