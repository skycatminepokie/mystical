# Mystical
Mystical is a server-side fabric mod, meant to spice up gameplay. (It will still run on your machine, you don't need a remote server)
As you play, a mysterious force casts spells over the world, changing game mechanics and adding new challenges. Think of those chaos mods that people stream on twitch, but more sensible. It won't destroy your fun or your world in a few hours.

Mystical is **highly configurable**, **open-source**, and **awesome**.

Please feel free to ask questions or contribute to the project on [GitHub](https://github.com/skycatminepokie/mystical). Known issues, bugs, and future plans are also on GitHub.

### Warnings/known bugs
- Major updates (the x in version x.y.z) indicate BREAKING changes - your game WILL CRASH if proper steps aren't taken when updating.

| Command                            | Effect                                       | Permission                               | Default requirement |
|------------------------------------|----------------------------------------------|------------------------------------------|---------------------|
| `/mystical`                        |                                              | `mystical.command.mystical`              | None                |
| `/mystical spell`                  |                                              | `mystical.command.spell`                 | None                |
| `/mystical spell new`              | Adds a new random spell                      | `mystical.command.spell.new`             | Op Level 4          |
| `/mystical spell new <spell>`      | Adds a new spell                             | `mystical.command.spell.new`             | Op Level 4          |
| `/mystical spell list`             | Shows all active spells                      | `mystical.command.mystical.spell.list`   | None                |
| `/mystical spell delete`           | Shows all active spells with a delete button | `mystical.command.mystical.spell.delete` | Op Level 4          |
| `/mystical spell delete <spell>`   | Deletes a spell                              | `mystical.command.mystical.spell.delete` | Op Level 4          |
| `/mystical spell reload`           | Reloads spells from file                     | `mystical.command.mystical.reload`       | Op Level 4          |
| `/mystical haven`                  | Use power to haven the current chunk         | `mystical.command.mystical.haven.haven`  | None                |
| `/mystical haven info`             | Shows haven info from the current chunk      | `mystical.command.mystical.haven.info`   | None                |
| `/mystical power`                  | Tells the player how much power they have    | `mystical.command.mystical.power`        | None                |
| `/mystical power add <players>`    | Gives a player power (used to haven chunks)  | `mystical.command.mystical.power.add`    | Op Level 4          |
| `/mystical power remove <players>` | Reduces a player's power                     | `mystical.command.mystical.power.remove` | Op Level 4          |
| `/mystical power get <players>`    | Shows how much power a player has            | `mystical.command.mystical.power.get`    | Op Level 3          |


Requires OwO-lib and Fabric API

#### Can I use this in a modpack?
Yes! Just don't rehost it without permission. And if you feel like it, let me know - I'm excited to see where this project goes!