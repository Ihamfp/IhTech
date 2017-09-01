package ihamfp.IhTech.common.packets;

import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.TileEntities.machines.TileEntityElectricMachine;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketMachineSimpleUpdate implements IMessage {
	private BlockPos pos;
	private int progress;
	
	public PacketMachineSimpleUpdate() {}
	
	public PacketMachineSimpleUpdate(BlockPos pos, int progress) {
		this.pos = pos;
		this.progress = progress;
	}
	
	public void setParams(BlockPos pos, int progress) {
		this.pos = pos;
		this.progress = progress;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		progress = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(progress);
	}
	
	public static class Handler implements IMessageHandler<PacketMachineSimpleUpdate,IMessage> {
		
		public Handler() {};
		
		@Override
		public IMessage onMessage(final PacketMachineSimpleUpdate message, final MessageContext ctx) {
			IThreadListener mainThread = FMLCommonHandler.instance().getWorldThread(ctx.netHandler);
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					handle(message, ctx);
				}
			});
			return null;
		}
		
		private void handle(PacketMachineSimpleUpdate message, MessageContext ctx) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			if (player == null || player.world == null)
				return;
			TileEntity tile = player.world.getTileEntity(message.pos);
			if (tile instanceof TileEntityElectricMachine) {
				((TileEntityElectricMachine)tile).simpleUpdate(message.progress);
			}
		}
		
	}

}
