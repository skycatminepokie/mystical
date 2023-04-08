# Mystical
Mystical is a server-side fabric mod, meant to spice up gameplay.
As you play, a mysterious force casts spells over the world, changing game mechanics and adding new challenges. Think of those chaos mods that people stream on twitch, but more sensible. It won't destroy your fun or your world in a few hours.

Mystical is **highly configurable**, **open-source**, and ***awesome***.

Mystical is currently in beta - things are likely to change, and more polish and documentation is yet to come. Please feel free to ask questions or contribute to the project on [GitHub](https://github.com/skycatminepokie/mystical). Known issues, bugs, and future plans are also on GitHub.

| Command                          | Effect                                       | Permission                               | Default requirement |
| -------------------------------- | -------------------------------------------- | ---------------------------------------- | ------------------- |
| `/mystical`                      |                                              | `mystical.command.mystical`              | None                |
| `/mystical spell`                |                                              | `mystical.command.spell`                 | None                |
| `/mystical spell new`            | Adds a new random spell                      | `mystical.command.spell.new`             | OP Level 4          |
| `/mystical spell new <spell>`    | Adds a new spell                             | `mystical.command.spell.new`             | OP Level 4          |
| `/mystical spell list`           | Shows all active spells                      | `mystical.command.mystical.spell.list`   | None                |
| `/mystical spell delete`         | Shows all active spells with a delete button | `mystical.command.mystical.spell.delete` | Op Level 4          |
| `/mystical spell delete <spell>` | Deletes a spell                              | `mystical.command.mystical.spell.delete` | Op Level 4          |
| `/mystical spell reload`         | Reloads spells from file                     | `mystical.command.mystical.reload`       | Op Level 4          |
