package com.thekingelessar.chatcooldownmanager.chatwindow;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.thekingelessar.chatcooldownmanager.ChatCooldownManager;
import com.thekingelessar.chatcooldownmanager.TickHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.command.ICommandSender;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;

import java.io.IOException;

import static com.thekingelessar.chatcooldownmanager.ChatCooldownManager.CCM_PREFIX;
import static com.thekingelessar.chatcooldownmanager.ServerTracker.hasChatCooldown;
import static com.thekingelessar.chatcooldownmanager.ServerTracker.isHypixel;
import static com.thekingelessar.chatcooldownmanager.TickHandler.ticksSinceLastChat;
import static com.thekingelessar.chatcooldownmanager.TickHandler.ticksSinceLastCommand;

// Significant part of the code from: https://www.curseforge.com/minecraft/mc-mods/vanilla-enhancements
public class GuiChatExtended extends GuiChat
{
    private String defaultInputTextField = "";
    
    public static final FieldWrapper<String> message = new FieldWrapper(ChatCooldownManager.isObfuscated ? "field_149440_a" : "message", C01PacketChatMessage.class);
    
    public GuiChatExtended()
    {
    }
    
    public GuiChatExtended(String defaultText)
    {
        super(defaultText);
        this.defaultInputTextField = defaultText;
    }
    
    @Override
    public void initGui()
    {
        super.initGui();
        this.inputField.setMaxStringLength(256);
        this.inputField.setText(this.defaultInputTextField);
    }
    
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 28 || keyCode == 156) // Enter pressed
        {
            String text = this.inputField.getText().trim();
            if (text.length() > 0)
            {
                if (ClientCommandHandler.instance.executeCommand((ICommandSender) Minecraft.getMinecraft().thePlayer, text) != 0)
                {
                    return;
                }
                
                boolean canSendNow = canSendChatMessage(text);
                
                if (!canSendNow)
                {
                    String substring = text.substring(0, Math.min(9, text.length()));
                    if (text.charAt(0) == "/".charAt(0))
                    {
                        TickHandler.scheduledCommands.add(text);
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(CCM_PREFIX + ChatFormatting.WHITE + "Command queued: " + ChatFormatting.GRAY + substring + "..."));
                    }
                    else // Todo: if command is like /pchat or /gchat, add to scheduledChat list
                    {
                        TickHandler.scheduledChat.add(text);
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(CCM_PREFIX + ChatFormatting.WHITE + "Chat message queued: " + ChatFormatting.GRAY + substring + "..."));
                    }
                }
                else
                {
                    
                    if (text.charAt(0) == "/".charAt(0))
                    {
                        TickHandler.sendCommand(text);
                        ticksSinceLastCommand = 0;
                    }
                    else
                    {
                        TickHandler.sendChat(text);
                        ticksSinceLastChat = 0;
                    }
                }
                
            }
            this.mc.displayGuiScreen((GuiScreen) null);
        }
        else
        {
            super.keyTyped(typedChar, keyCode);
        }
    }
    
    public boolean canSendChatMessage(String chatMessage)
    {
        if (isHypixel && hasChatCooldown)
        {
            // Queue chat and commands
            if (chatMessage.charAt(0) == "/".charAt(0))
            {
                if (ticksSinceLastCommand < 11)
                {
                    return false;
                }
            }
            else
            {
                if (ticksSinceLastChat < 160)
                {
                    return false;
                }
            }
        }
        else if (isHypixel)
        {
            // Queue only commands
            if (chatMessage.charAt(0) == "/".charAt(0))
            {
                if (ticksSinceLastCommand < 11)
                {
                    return false;
                }
            }
        }
        return true;
    }
}
