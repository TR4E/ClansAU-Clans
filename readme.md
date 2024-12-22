# Clans Plugin

The Clans plugin is an advanced Minecraft plugin designed to create a dynamic and competitive multiplayer experience. Players can form alliances, wage wars against enemies, claim territory, and build their clans into powerful factions. With features like custom weapons, world events, farming, and shops, the plugin offers endless opportunities for collaboration, competition, and strategic gameplay.

<br>

---

## Features

### Clan System
- **Factions/Teams**: Clans function like factions or teams with territory and land claiming.
- **Dominance System**: Gain 16+ dominance points over an enemied clan to trigger Pillaging Mode, where the Pillager can raid/grief the Pillagee's base.
- **Alliances and Enemies**: Form alliances and enemy relationships, including:
    - **Trusted Alliances**: Allow trusted allies to interact with doors and gates in your territory.
- **Clan Energy**: Energy decreases based on territory owned:
    - If energy depletes, the clan disbands, and its location is broadcasted to the chat.
    - Admin clan territory is always protected, has permanent energy, and is immune to TNT explosions.
- **Integration with Champions**: Interact with skills and weapons using:
    - **SkillLocationEvent / WeaponLocationEvent**: Adjust mechanics based on specific clan territories.
    - **SkillFriendlyFireEvent / WeaponFriendlyFireEvent**: Respect clan relations during combat, assisting allies and affecting enemies.
- **Combat Protection**: In safe-zone clan territories:
    - Players in combat can attack and be attacked.
    - Non-combat players can attack combat players, putting themselves into combat.

### KoTH (King of the Hill)
- **Territorial Battle**: Clans fight to control a specific area for rewards.
- **Timed Events**: Scheduled competitions to ensure balanced participation.
- **Exclusive Rewards**: Winning clans gain unique items and bonuses.

### Fields
- **Resource Gathering**: Mine ores that regenerate over time.
- **Loot Boxes**: Ender chests act as loot boxes containing various items:
    - Rare items and legendaries can be obtained.

### Supply Crate
- **Airdrop Mechanics**:
    - A beacon block placed in the wilderness becomes a supply crate.
    - Particles appear above, signaling a 1-minute wait for firework particles to transform the beacon into a chest.
- **Loot Contents**:
    - Items include armor class sets from Champions and custom items from Clans and Champions.

### Farming
- **Cultivation Zones**:
    - Farming is restricted to Y levels 44-60.
- **Resource Yield**:
    - Farming at specified levels ensures optimal crop growth and harvesting.

### Fishing
- **Weighted Fish**:
    - Catches are weighted and can be sold at shops for coins.
- **Rare Catches**:
    - Chance to catch legendaries and rare items.

### Custom TNT Mechanics
- **Block Transformations**:
    - Blocks hit by TNT undergo transformations for a cracked effect:
        - Stone Brick → Cracked Stone Brick → Air
        - Nether Brick → Netherrack → Air
        - Quartz Block → Chiseled Quartz Block → Air
        - Smooth Sandstone → Normal Sandstone → Air
        - Smooth Red Sandstone → Normal Red Sandstone → Air
        - Dark Prismarine → Prismarine Bricks → Prismarine → Air
- **Liquid Removal**:
    - Explosions remove liquids within the blast radius.

### Crate System
- **Voting Crates**:
    - Players receive crates for voting on server lists.
    - Contains a variety of rewards with configurable chances.

### Quests
- **Daily Challenges**:
    - Reset every day and include tasks like:
        - Killing 5 Assassins or Knights.
        - Mining 10 different ores at Fields.
        - Catching 15 fish at the Lake.

### World Events
- **Fishing Frenzy**:
    - Catch fish 2x faster with increased luck for rare catches.
- **Mining Madness**:
    - 2x loot from ores and ender chests with higher chances of rare items.

<br>

---

## Integration

### Core
- This plugin works as an extension of the Core, relying on its infrastructure for core functionality.

### [Champions-API](https://github.com/TR4E/Champions-API)
- Provides integration capabilities between Champions and other child plugins, such as Clans, allowing seamless interaction between systems.

<br>

---

## Contributing

Contributions are welcome! Feel free to open issues or submit pull requests to improve the plugin.
