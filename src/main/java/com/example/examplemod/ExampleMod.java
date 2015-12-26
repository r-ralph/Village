package com.example.examplemod;

import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = ExampleMod.MODID, version = ExampleMod.VERSION)
public class ExampleMod
{
    public static final String MODID = "examplemod";
    public static final String VERSION = "1.0";

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// some example code
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());

        initElf();
        initOrc();
    }

    private void initElf() {
        EntityRegistry.registerGlobalEntityID(EntityElf.class, "elf", EntityRegistry.findGlobalUniqueEntityId(), 10, 100000);
        RenderingRegistry.registerEntityRenderingHandler(EntityElf.class, new RenderElf(new ModelVillager(0.0f), 0));
    }

    private void initOrc() {
        EntityRegistry.registerGlobalEntityID(EntityOrc.class, "orc", EntityRegistry.findGlobalUniqueEntityId(), 10, 100000);
        RenderingRegistry.registerEntityRenderingHandler(EntityOrc.class, new RenderOrc(new ModelVillager(0.0f), 0));
    }
}
