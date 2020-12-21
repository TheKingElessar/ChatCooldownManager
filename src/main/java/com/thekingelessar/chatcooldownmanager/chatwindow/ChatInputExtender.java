package com.thekingelessar.chatcooldownmanager.chatwindow;

import com.thekingelessar.chatcooldownmanager.ChatCooldownManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.NetworkManager;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

// Code from: https://www.curseforge.com/minecraft/mc-mods/vanilla-enhancements
public class ChatInputExtender
{
    private final String[] whitelisted = new String[]{"hypixel.net"};
    
    private static final FieldWrapper<String> defaultText = new FieldWrapper(ChatCooldownManager.isObfuscated ? "field_146409_v" : "defaultInputFieldText", GuiChat.class);
    
    public ChatInputExtender()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onOpenChat(GuiOpenEvent event)
    {
        if (event.gui instanceof GuiChat && doesServerAllow())
        {
            String defaultText = (String) ChatInputExtender.defaultText.get(event.gui);
            if (defaultText == null)
            {
                defaultText = "";
            }
            
            event.gui = new GuiChatExtended(defaultText);
         //   event.gui.initGui();
        }
    }
    
    private boolean doesServerAllow()
    {
        Minecraft mc = Minecraft.getMinecraft();
        NetworkManager netManager = mc.getNetHandler().getNetworkManager();
        boolean isLocal = netManager.isLocalChannel();
        if (isLocal || isWhitelisted((mc.getCurrentServerData()).serverIP))
        {
            return true;
        }
        return false;
    }
    
    private boolean isWhitelisted(String ip)
    {
        for (String server : this.whitelisted)
        {
            if (ip.endsWith(server))
            {
                return true;
            }
        }
        return false;
    }
}
