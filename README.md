# ChatCooldownHandler

## User Info

There are two main modules to this mod.

### Hypixel Chat Cooldown

This mod checks if you have a rank on Hypixel. If you don't, it keeps you from sending chat messages too quickly, spacing them out automatically. If needed, it will add all your messages to internal queues, ensuring you don't run into Hypixel's chat cooldown filter.

![Example of queued message](https://i.imgur.com/X3kv46U.png)

### Chat Character Count

This mod also increases the max chat character count on compatible servers. 1.8.9 clients can only send 100 characters in a chat message, but later versions can send up to 256 characters. This increases the max character count from 100 to 256. 

At the moment, this only works on single-player worlds and Hypixel. This module uses a modified version of [Vanilla Enhancements'](https://www.curseforge.com/minecraft/mc-mods/vanilla-enhancements) Chat module.

___ 

### Installation

This mod uses Minecraft Forge:

 - [Install Forge](https://apexminecrafthosting.com/how-to-install-forge-client-side/)

 - [Install Mods](https://apexminecrafthosting.com/how-to-install-mods-on-forge/)
 
Simply drop the [`.jar` mod file](https://github.com/TheKingElessar/ChatCooldownManager/releases/) in your `mods/` directory, and you're off!

## Developer Info

This mod was originally created because I needed a way for another mod of mine to deal with the Hypixel chat cooldown. Since this is so useful, though, I decided to make it a stand-alone mod.

If you use this mod as a dependency in your mod, you can add your chat messages/commands to the client's queue, too. This mod will take care of everything: checking the server, checking the rank, knowing when to send the message, etc.

To do so, you simply need to add your message to the proper `ArrayList`. You can find them below.

 - `scheduledCommands` is used for commands. It works with both client-side commands and commands sent to the server. The reason that commands and chat messages are stored differently is because Hypixel has a different cooldown for commands.

 - `scheduledChat` is used for chat messages.

```
package com.thekingelessar.chatcooldownmanager;

public class TickHandler
{
    public static List<String> scheduledCommands = new ArrayList<String>();
    public static List<String> scheduledChat = new ArrayList<String>();
}
```