package com.lying.variousgods.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.lying.variousgods.capabilities.PlayerData;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class PacketPraying
{
	private UUID playerID;
	private BlockPos altarPos = BlockPos.ZERO;
	
	public PacketPraying(UUID idIn)
	{
		this(idIn, BlockPos.ZERO);
	}
	public PacketPraying(UUID idIn, BlockPos altar)
	{
		this.playerID = idIn;
		this.altarPos = altar;
	}
	
	public static PacketPraying decode(FriendlyByteBuf par1Buffer)
	{
		PacketPraying packet = new PacketPraying(par1Buffer.readUUID());
		packet.altarPos = par1Buffer.readBlockPos();
		return packet;
	}
	
	public static void encode(PacketPraying msg, FriendlyByteBuf par1Buffer)
	{
		par1Buffer.writeUUID(msg.playerID);
		par1Buffer.writeBlockPos(msg.altarPos);
	}
	
	public static void handle(PacketPraying msg, Supplier<NetworkEvent.Context> cxt)
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
			if(target == null)
				return;
			
			PlayerData data = PlayerData.getCapability(target);
			if(data.hasAltar())
				data.startPraying();
			else
				data.stopPraying();
		}
	}
}
