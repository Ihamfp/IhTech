package ihamfp.IhTech.common.packets;

import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketEnergyChange implements IMessage {
	private BlockPos blockPos;
	private int level;
	
	public PacketEnergyChange() {}
	
	public PacketEnergyChange(BlockPos pos, int level) {
		this.blockPos = pos;
		this.level = level;
	}
	
	public void setParams(BlockPos pos, int level) {
		this.blockPos = pos;
		this.level = level;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		blockPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		level = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(blockPos.getX());
		buf.writeInt(blockPos.getY());
		buf.writeInt(blockPos.getZ());
		buf.writeInt(level);
	}
	
	public static class Handler implements IMessageHandler<PacketEnergyChange, IMessage> {

		@Override
		public IMessage onMessage(final PacketEnergyChange message, final MessageContext ctx) {
			IThreadListener mainThread = FMLCommonHandler.instance().getWorldThread(ctx.netHandler);
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					handle(message, ctx);
				}
			});
			return null;
		}
		
		private void handle(PacketEnergyChange message, MessageContext ctx) {
			//EntityPlayer player = ctx.getServerHandler().playerEntity;
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			if (player == null || player.worldObj == null)
				return;
			TileEntity tile = player.worldObj.getTileEntity(message.blockPos);
			if (tile instanceof ITileEntityEnergyStorage) {
				IEnergyStorage e = ((ITileEntityEnergyStorage) tile).getEnergyStorage();
				if (e == null) return;
				if (e.getEnergyStored() < message.level) {
					e.receiveEnergy(message.level-e.getEnergyStored(), false);
				} else if (e.getEnergyStored() > message.level) {
					e.extractEnergy(e.getEnergyStored()-message.level, false);
				}
			}
		}
		
	}
}
