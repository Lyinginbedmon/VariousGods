package com.lying.variousgods.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.lying.variousgods.client.gui.menu.MenuAltar;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class PacketOpenDeityMenu
{
	private UUID playerID;
	
	public PacketOpenDeityMenu(UUID idIn)
	{
		this.playerID = idIn;
	}
	
	public static PacketOpenDeityMenu decode(FriendlyByteBuf par1Buffer)
	{
		return new PacketOpenDeityMenu(par1Buffer.readUUID());
	}
	
	public static void encode(PacketOpenDeityMenu msg, FriendlyByteBuf par1Buffer)
	{
		par1Buffer.writeUUID(msg.playerID);
	}
	
	public static void handle(PacketOpenDeityMenu msg, Supplier<NetworkEvent.Context> cxt)
	{
		NetworkEvent.Context context = cxt.get();
		context.setPacketHandled(true);
		if(context.getDirection().getReceptionSide().isServer())
		{
			ServerPlayer player = context.getSender();
			Player target = null;
			if(player.getUUID().equals(msg.playerID))
				target = player;
			else
				target = player.getLevel().getPlayerByUUID(msg.playerID);
			
			if(target.containerMenu instanceof MenuAltar)
				target.openMenu((MenuAltar)target.containerMenu);
		}
		else
		{
			
		}
	}
}
