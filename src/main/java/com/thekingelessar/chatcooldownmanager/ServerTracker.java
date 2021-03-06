package com.thekingelessar.chatcooldownmanager;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.Timer;
import java.util.TimerTask;

// Most of code from: https://github.com/voidinvoid/VoidChat/
public class ServerTracker
{
    
    public static boolean isHypixel;
    public static boolean hasChatCooldown = false;
    public static boolean testingRank = false;
    
    @SubscribeEvent
    public void checkHypixel(FMLNetworkEvent.ClientConnectedToServerEvent e)
    {
        String[] s = e.manager.getRemoteAddress().toString().split("/");
        if (s.length < 1)
        {
            isHypixel = false;
            return;
        }
        if (s[0].endsWith(".hypixel.net"))
        {
            isHypixel = true;
        }
        
        if (isHypixel)
        {
            final Timer r = new Timer();
            scheduleRankTester(r);
        }
    }
    
    public void scheduleRankTester(Timer r)
    {
        TimerTask t = new TimerTask()
        {
            @Override
            public void run()
            {
                
                if (Minecraft.getMinecraft().thePlayer == null)
                {
                    scheduleRankTester(r);
                }
                else
                {
                    testingRank = true;
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/mypos");
                    r.cancel();
                }
            }
        };
        
        r.schedule(t, 1000);
        
    }
    
    @SubscribeEvent
    public void disconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent e)
    {
        isHypixel = false;
    }
    
    @SubscribeEvent
    public void onClientChatRecieve(ClientChatReceivedEvent chatReceivedEvent)
    {
        String fullMessage = chatReceivedEvent.message.getUnformattedText();
        if (testingRank)
        {
            if (fullMessage.contains("You must be vip or higher to use this command!"))
            {
                testingRank = false;
                hasChatCooldown = true;
                chatReceivedEvent.setCanceled(true);
            }
        }
    }
}