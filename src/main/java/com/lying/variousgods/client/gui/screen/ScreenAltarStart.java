package com.lying.variousgods.client.gui.screen;

import com.lying.variousgods.capabilities.PlayerData;
import com.lying.variousgods.client.gui.menu.MenuAltar;
import com.lying.variousgods.client.gui.menu.MenuAltarStart;
import com.lying.variousgods.network.PacketHandler;
import com.lying.variousgods.network.PacketPraying;
import com.lying.variousgods.reference.Reference;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenAltarStart extends ScreenAltar<MenuAltarStart>
{
	private Button chooseGodButton, startPrayingButton;
	
	public ScreenAltarStart()
	{
		this(null, null, Component.empty());
	}
	
	public ScreenAltarStart(MenuAltar altarIn, Inventory inv, Component displayName)
	{
		super((MenuAltarStart)altarIn, inv, displayName);
	}
	
	protected void init()
	{
         this.addRenderableWidget(this.chooseGodButton = new Button(this.width / 2 - 100, 196, 98, 20, Component.translatable("container."+Reference.ModInfo.MOD_ID+".altar.choose_god"), (button) -> {
            altarMenu.openDeityMenu(minecraft.player);
         }));
         this.addRenderableWidget(this.startPrayingButton = new Button(this.width / 2 + 2, 196, 98, 20, Component.translatable("container."+Reference.ModInfo.MOD_ID+".altar.pray"), (button) -> {
            PacketHandler.sendToServer(new PacketPraying(this.minecraft.player.getUUID()));
         }));
	}
	
	public void tick()
	{
		PlayerData data = PlayerData.getCapability(this.minecraft.player);
		boolean isPraying = data.isPraying();
		this.chooseGodButton.active = !isPraying;
		this.startPrayingButton.active = !isPraying && (data.canPray() || this.minecraft.player.isCreative());
	}
}
