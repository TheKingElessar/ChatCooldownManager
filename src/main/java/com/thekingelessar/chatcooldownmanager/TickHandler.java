package com.thekingelessar.chatcooldownmanager;

import com.thekingelessar.chatcooldownmanager.chatwindow.GuiChatExtended;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import static com.thekingelessar.chatcooldownmanager.ServerTracker.hasChatCooldown;
import static com.thekingelessar.chatcooldownmanager.ServerTracker.isHypixel;

public class TickHandler
{
    public static List<String> scheduledCommands = new ArrayList<String>();
    public static List<String> scheduledChat = new ArrayList<String>();
    
    public static int ticksSinceLastCommand = 11;
    public static int ticksSinceLastChat = 71;
    
    @SideOnly (Side.CLIENT)
    @SubscribeEvent (priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onEvent(TickEvent.ClientTickEvent tickEvent)
    {
        if (isHypixel && hasChatCooldown)
        {
            if (ticksSinceLastChat >= 160 && scheduledChat.size() > 0)
            {
                sendChat(scheduledChat.remove(0));
            }
        }
        else if(scheduledChat.size() > 0)
        {
            sendChat(scheduledChat.remove(0));
        }
        
        if (isHypixel)
        {
            if (ticksSinceLastCommand > 10 && scheduledCommands.size() > 0)
            {
                sendCommand(scheduledCommands.remove(0));
            }
        }
        else if (scheduledCommands.size() > 0)
        {
            sendCommand(scheduledCommands.remove(0));
        }
        
        if (ticksSinceLastChat < 160)
        {
            ticksSinceLastChat++;
        }
        
        if (ticksSinceLastCommand < 20)
        {
            ticksSinceLastCommand++;
        }
    }
    
    @SideOnly (Side.CLIENT)
    public static void sendChat(String message)
    {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(message);
        
        C01PacketChatMessage packet = new C01PacketChatMessage(message);
        GuiChatExtended.message.set(packet, message);
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue((Packet) packet);
        ticksSinceLastChat = 0;
    }
    
    @SideOnly (Side.CLIENT)
    public static void sendCommand(String command)
    {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(command);
    
        if (ClientCommandHandler.instance.executeCommand((ICommandSender) Minecraft.getMinecraft().thePlayer, command) != 0)
        {
            return;
        }
    
        C01PacketChatMessage packet = new C01PacketChatMessage(command);
        GuiChatExtended.message.set(packet, command);
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue((Packet) packet);
        ticksSinceLastCommand = 0;
    }
}