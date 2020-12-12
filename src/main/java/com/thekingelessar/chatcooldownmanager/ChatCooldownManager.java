package com.thekingelessar.chatcooldownmanager;

import com.thekingelessar.chatcooldownmanager.enhancements_mod.ChatInputExtender;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod (modid = ChatCooldownManager.MODID, version = ChatCooldownManager.VERSION)
public class ChatCooldownManager
{
    public static final String MODID = "chatcooldownmanager";
    public static final String VERSION = "1.0";
    
    public static boolean isObfuscated;
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        isObfuscated = isObfuscated();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        new ChatInputExtender();
        MinecraftForge.EVENT_BUS.register(new ServerTracker());
        MinecraftForge.EVENT_BUS.register(new TickHandler());
    }
    
    private static boolean isObfuscated()
    {
        try
        {
            Minecraft.class.getDeclaredField("logger");
            return false;
        }
        catch (NoSuchFieldException e1)
        {
            return true;
        }
    }
}
