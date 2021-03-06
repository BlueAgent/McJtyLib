package mcjty.lib.network;

import mcjty.lib.typed.TypedMap;
import mcjty.lib.varia.DimensionId;
import mcjty.lib.varia.Logging;
import mcjty.lib.varia.WorldTools;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.Supplier;

/**
 * This is a packet that can be used to send a command from the client side (typically the GUI) to
 * a tile entity on the server side that implements CommandHandler. This will call 'executeWithResultInteger()' on
 * that command handler. A PacketIntegerFromServer will be sent back from the client.
 */
public class PacketRequestDataFromServer {
    protected BlockPos pos;
    private DimensionId type;
    protected String command;
    protected TypedMap params;
    private boolean dummy;

    public PacketRequestDataFromServer(PacketBuffer buf) {
        pos = buf.readBlockPos();
        type = DimensionId.fromPacket(buf);
        command = buf.readString(32767);
        params = TypedMapTools.readArguments(buf);
        dummy = buf.readBoolean();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        type.toBytes(buf);
        buf.writeString(command);
        TypedMapTools.writeArguments(buf, params);
        buf.writeBoolean(dummy);
    }

    public PacketRequestDataFromServer(DimensionId type, BlockPos pos, String command, TypedMap params, boolean dummy) {
        this.type = type;
        this.pos = pos;
        this.command = command;
        this.params = params;
        this.dummy = dummy;
    }

    public void handle(SimpleChannel channel, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            World world = WorldTools.getWorld(ctx.getSender().getEntityWorld(), type);
            if (world.isBlockLoaded(pos)) {
                TileEntity te = world.getTileEntity(pos);
                if (!(te instanceof ICommandHandler)) {
                    Logging.log("createStartScanPacket: TileEntity is not a CommandHandler!");
                    return;
                }
                ICommandHandler commandHandler = (ICommandHandler) te;
                TypedMap result = commandHandler.executeWithResult(command, params);
                if (result == null) {
                    Logging.log("Command " + command + " was not handled!");
                    return;
                }

                PacketDataFromServer msg = new PacketDataFromServer(dummy ? null : pos, command, result);
                channel.sendTo(msg, ctx.getSender().connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
            }
        });
        ctx.setPacketHandled(true);
    }
}
