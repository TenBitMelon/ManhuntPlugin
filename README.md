# Manhunt Plugin

A Minecraft Manhunt plugin for Spigot. Players create games, choose runners, hunters, or spectators, and play in newly generated world.

This is for Spigot 1.18. Java 16 or newer.

The plugin uses the server world named `world` as its hub.

If you want to run it yourself, you need to get the drink plugin, for commands

## Building youself

1. Build it using maven
2. Copy `target/ManhuntPlugin-1.0-SNAPSHOT.jar` to your server's `plugins` directory, alongside the drink plugin.
3. Start the server and join it. 

You also need to download and put in the plugin folder the `generateMapPreview_windows`, `generateMapPreview_mac`, `generateMapPreview_linux`, or `generateMapPreview_centos` matching the server OS. 
These are from [BiomesImage](https://github.com/TenBitMelon/BiomesImage)

## Playing!

The hub inventory provides items for creating a game, browsing games, and viewing game history. 

Or you can use commands:

- `/create [seed]` opens the game menu.  Options include seed, world type, difficulty, privacy, and hunter cooldown. A numeric seed is parsed as a number; other text is converted to a hash.
- Invite players with `/invite <player>`, or make the game public so players can find it in the Games in Progress menu.
- Select a team in the chat or `/teams runner`, `/teams hunter`, or `/teams spectator`. 
- The game starts after world generation and all players are ready.
- You can use `/leave` to leave a game and return to the hub.

The default settings are a random seed, normal world type and difficulty, a 20-second hunter cooldown.

## Authors

melonboy10 (thats me) for the plugin  and Enderlord0042 and Roblkyogre for BiomesImage
