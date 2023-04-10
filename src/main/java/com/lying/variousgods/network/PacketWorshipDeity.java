package com.lying.variousgods.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.lying.variousgods.capabilities.PlayerData;
import com.lying.variousgods.client.gui.menu.MenuAltar;
import com.lying.variousgods.deities.DeityRegistry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class PacketWorshipDeity
{
	private UUID playerID;
	private String deityName;
	
	public PacketWorshipDeity(UUID idIn)
	{
		this(idIn, "");
	}
	public PacketWorshipDeity(UUID idIn, String deityIn)
	{
		this.playerID = idIn;
		this.deityName = deityIn;
	}
	
	public static PacketWorshipDeity decode(FriendlyByteBuf par1Buffer)
	{
		return new PacketWorshipDeity(par1Buffer.readUUID(), par1Buffer.readUtf());
	}
	
	public static void encode(PacketWorshipDeity msg, FriendlyByteBuf par1Buffer)
	{
		par1Buffer.writeUUID(msg.playerID);
		par1Buffer.writeUtf(msg.deityName);
	}
	
	public static void handle(PacketWorshipDeity msg, Supplier<NetworkEvent.Context> cxt)
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
			
			PlayerData data = PlayerData.getCapability(target);
			data.setDeity(DeityRegistry.getInstance().getDeity(msg.deityName));
			
			if(target.containerMenu instanceof MenuAltar)
				target.openMenu((MenuAltar)target.containerMenu);
			
			if(data.hasDeity())
				data.startPraying();
		}
		else
			;
	}
}
