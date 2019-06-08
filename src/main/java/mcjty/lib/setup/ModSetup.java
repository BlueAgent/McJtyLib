package mcjty.lib.setup;

import mcjty.lib.McJtyLib;
import mcjty.lib.McJtyRegister;
import mcjty.lib.multipart.MultipartBlock;
import mcjty.lib.multipart.MultipartTE;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModSetup extends DefaultModSetup {

    public static MultipartBlock multipartBlock;

    @Override
    public void preInit(FMLCommonSetupEvent e) {
        super.preInit(e);
        multipartBlock = new MultipartBlock();
        GameRegistry.registerTileEntity(MultipartTE.class, new ResourceLocation(McJtyLib.MODID, "multipart_te"));
        MinecraftForge.EVENT_BUS.register(new BlockRegister());
    }

    @Override
    protected void setupModCompat() {

    }

    @Override
    protected void setupConfig() {

    }

    @Override
    public void createTabs() {
    }

    private static class BlockRegister {
        @SubscribeEvent
        public void registerBlocks(RegistryEvent.Register<Block> event) {
            McJtyRegister.registerBlocks(McJtyLib.instance, event.getRegistry());
        }

        @SubscribeEvent
        public void registerItems(RegistryEvent.Register<Item> event) {
            McJtyRegister.registerItems(McJtyLib.instance, event.getRegistry());
        }

    }
}
