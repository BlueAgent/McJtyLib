package mcjty.lib.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a packet that can be used to update the NBT on the held item of a player.
 */
public class PacketUpdateNBTItem implements IMessage {
    private Map<String,Argument> args;

    @Override
    public void fromBytes(ByteBuf buf) {
        args = AbstractServerCommand.readArguments(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        AbstractServerCommand.writeArguments(buf, args);
    }

    public PacketUpdateNBTItem() {
    }

    public PacketUpdateNBTItem(Argument... arguments) {
        if (arguments == null) {
            this.args = null;
        } else {
            args = new HashMap<String, Argument>(arguments.length);
            for (Argument arg : arguments) {
                args.put(arg.getName(), arg);
            }
        }
    }

    public static class Handler implements IMessageHandler<PacketUpdateNBTItem, IMessage> {
        @Override
        public IMessage onMessage(PacketUpdateNBTItem message, MessageContext ctx) {
            MinecraftServer.getServer().addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketUpdateNBTItem message, MessageContext ctx) {
            EntityPlayerMP playerEntity = ctx.getServerHandler().playerEntity;
            ItemStack heldItem = playerEntity.getHeldItem();
            if (heldItem == null) {
                return;
            }
            NBTTagCompound tagCompound = heldItem.getTagCompound();
            if (tagCompound == null) {
                tagCompound = new NBTTagCompound();
                heldItem.setTagCompound(tagCompound);
            }
            for (Map.Entry<String, Argument> entry : message.args.entrySet()) {
                String key = entry.getKey();
                switch (entry.getValue().getType()) {
                    case TYPE_STRING:
                        tagCompound.setString(key, entry.getValue().getString());
                        break;
                    case TYPE_INTEGER:
                        tagCompound.setInteger(key, entry.getValue().getInteger());
                        break;
                    case TYPE_COORDINATE:
                        throw new RuntimeException("Coordinate not supported for PacketUpdateNBTItem!");
                    case TYPE_BOOLEAN:
                        tagCompound.setBoolean(key, entry.getValue().getBoolean());
                        break;
                    case TYPE_DOUBLE:
                        tagCompound.setDouble(key, entry.getValue().getDouble());
                        break;
                }
            }
        }

    }

}
