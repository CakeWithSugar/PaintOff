package mc.cws.paintOff.Po.Executors;

import mc.cws.paintOff.Configuration;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.Game.Management.Verteiler;
import mc.cws.paintOff.Po.ArenaManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.Objects;

public class Po implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (sender == null) {
            System.err.println("Dieser Befehl kann nur von Spielern ausgeführt werden!");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.WHITE + "Verfügbare Befehle:");
            sender.sendMessage(ChatColor.GOLD + "/" + Configuration.mainCommand + " pos1");
            sender.sendMessage(ChatColor.GOLD + "/" + Configuration.mainCommand + " pos2");
            sender.sendMessage(ChatColor.GOLD + "/" + Configuration.mainCommand + " spawn");
            sender.sendMessage(ChatColor.GOLD + "/" + Configuration.mainCommand + " create <Name>");
            sender.sendMessage(ChatColor.GOLD + "/" + Configuration.mainCommand + " delete <Name>");
            sender.sendMessage(ChatColor.GOLD + "/" + Configuration.mainCommand + " list");
            sender.sendMessage(ChatColor.GOLD + "/" + Configuration.mainCommand + " list <Name>");
            sender.sendMessage(ChatColor.GOLD + "/" + Configuration.mainCommand + " lobby");
            sender.sendMessage(ChatColor.GOLD + "/" + Configuration.mainCommand + " mechanik");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("pos1")) {
            ArenaManager.setPos1(sender, ((Player) sender).getLocation());
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("pos2")) {
            ArenaManager.setPos2(sender, ((Player) sender).getLocation());
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("spawn")) {
            ArenaManager.setSpawn(sender, ((Player) sender).getLocation());
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
            String arenaName = args[1];
            ArenaManager.createArena(sender, arenaName, player);
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
            String arenaName = args[1];
            ArenaManager.deleteArena(sender, arenaName);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            sender.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.WHITE + "Arenen:");
            File arenasFolder = new File(Configuration.mainCommand+"-arenas");
            if (arenasFolder.exists()) {
                for (File arenaFolder : Objects.requireNonNull(arenasFolder.listFiles())) {
                    if (arenaFolder.isDirectory()) {
                        sender.sendMessage(ChatColor.GOLD + "- " + arenaFolder.getName());
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Keine Arenen gefunden!");
            }
            return true;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("list")) {
            String arenaName = args[1];
            File arenaFolder = new File(Configuration.mainCommand+"-arenas", arenaName);
            if (arenaFolder.exists()) {
                File blocksFile = new File(arenaFolder, "arena.dat");
                if (blocksFile.exists()) {
                    sender.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.WHITE + "Arena-Informationen:");
                    sender.sendMessage(ChatColor.GOLD + "- Name: " + arenaName);
                    sender.sendMessage(ChatColor.GOLD + "- Datei: " + blocksFile.getAbsolutePath());
                } else {
                    sender.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Arena-Datei nicht gefunden!");
                }
            } else {
                sender.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Arena nicht gefunden!");
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("lobby")) {
            Location location = player.getLocation();
            File lobbyFolder = new File(Configuration.mainCommand+"-lobby");
            if (!lobbyFolder.exists()) {
                lobbyFolder.mkdirs();
            }
            File lobbyFile = new File(lobbyFolder, "lobby.dat");
            
            try {
                if (!lobbyFile.getParentFile().exists()) {
                    lobbyFile.getParentFile().mkdirs();
                }
                if (!lobbyFile.exists()) {
                    lobbyFile.createNewFile();
                }
                // Save coordinates in simple integer format
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(lobbyFile))) {
                    // Convert to integers and use dots as decimal separator
                    writer.write(location.getBlockX() + "." + location.getBlockY() + "." + location.getBlockZ());
                    writer.newLine();
                    writer.write("0.0.0"); // Add dummy block data
                }
                sender.sendMessage(ChatColor.GREEN + Configuration.name + " | " + ChatColor.GRAY + "Lobby-Punkt gesetzt!");
            } catch (IOException e) {
                sender.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Fehler beim Speichern des Lobby-Punkts!");
            }
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Arena-Erstellungshilfe:");
            sender.sendMessage(ChatColor.GRAY + "/" + Configuration.mainCommand + " pos1 - Setze die erste Ecke des Auswahlbereichs");
            sender.sendMessage(ChatColor.GRAY + "/" + Configuration.mainCommand + " pos2 - Setze die zweite Ecke des Auswahlbereichs");
            sender.sendMessage(ChatColor.GRAY + "/" + Configuration.mainCommand + " spawn - Setze den Spawnpunkt-B");
            sender.sendMessage(ChatColor.GRAY + "/" + Configuration.mainCommand + " create <name> - Erstelle Arena mit dem gewählten Bereich");
            sender.sendMessage(ChatColor.GRAY + "/" + Configuration.mainCommand + " delete <name> - Lösche eine Arena");
            sender.sendMessage(ChatColor.GRAY + "/" + Configuration.mainCommand + " list - Zeige alle Arenen");
            sender.sendMessage(ChatColor.GRAY + "/" + Configuration.mainCommand + " list <name> - Zeige Details zu einer Arena");
            sender.sendMessage(ChatColor.GRAY + "/" + Configuration.mainCommand + " mechanik - Zeige die Spielmechaniken");
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("mechanik")) {
            sender.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Der Start:");
            sender.sendMessage(ChatColor.GOLD + "- Erstelle eine Arena:", ChatColor.GRAY + " 1. /po pos1 ||" + "2. /po pos2 ||" + "3. /po spawn ||" + "4. /po create <Name> ||" + "5. /po lobby");
            sender.sendMessage(ChatColor.GOLD + "- Modifiziere dein Spiel:", ChatColor.GRAY + " /pomenu");
            sender.sendMessage(ChatColor.GOLD + "- Starte dein Spiel:", ChatColor.GRAY + " /poqueue || oder den Server beitreten und /postart [nur als admin möglich!]");
            sender.sendMessage(ChatColor.GOLD + "- Teamverteilung:", ChatColor.GRAY + " Die Teamverteilung erfolgt automatisch und zufällig! Es gibt 2 Teams weche jewails eine eindeutige Farbe haben.");
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Die Mechaniken:");
            sender.sendMessage(ChatColor.GOLD + "- Waffenwahl:", ChatColor.GRAY + " Wähle ein Kit mit /poarsenal aus oder klicke die kiste während der queue!");
            sender.sendMessage(ChatColor.GOLD + "- Primärwaffen:", ChatColor.GRAY + " Jede Primärwaffe hat ihren eigenen Benefits und Stärken. Sie kann jederzeit mit der nötigen XP benutzt werden!");
            sender.sendMessage(ChatColor.GOLD + "- Sekundärwaffen:", ChatColor.GRAY + " Sekundärwaffen liegen rechts neben der Primärwaffe. Sie brauchen eine bestimmte anzahl an LEVELS, um sie zu nutzen! Sie stehen in /poarsenal in der Beschreibung der Waffe!");
            sender.sendMessage(ChatColor.GOLD + "- Spezialwaffen:", ChatColor.GRAY + " Spezialwaffen liegen auf der 2. Position im Inventar. Sie können nur dann aktiviert werden, wenn der Spieler die jeweiligen ULTPUNKTE erreicht hat!");
            sender.sendMessage(ChatColor.GOLD + "- Ultpunkte:", ChatColor.GRAY + " Ultpunkte werden durch das setzen von Farbblöcken vergeben. Pro Block ein Ultpunkt! Sie können durch Primärwaffen und Sekundärwaffen erhalten werden! Spezialwaffen geben keine Ultpunkte!");
            sender.sendMessage(ChatColor.GOLD + "- Eintauchen:", ChatColor.GRAY + " Ist ein Spieler auf seiner eigenen Farbe, kann er SHIFT drücken um einzutauchen! Ist er eingetaucht bewegt er sicht schneller, regenneriert sich und seine Xp und wird unsichtbar!");
            sender.sendMessage(ChatColor.GOLD + "- Teleport:", ChatColor.GRAY + " Ein Spieler kann sich wann er möchte zu seinem Spawn, oder einem Mitspieler teleportieren!");
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("info")) {
            if (Start.getQueueNumber((Player) sender) == -1) {
                sender.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Du bist nicht in einer Queue!");
                return true;
            }

            int n = Start.getQueueNumber((Player) sender);
            String team = Verteiler.getTeam((Player) sender, n);
            if (team == null) {
                sender.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Du bist nicht in einem Team!");
                return true;
            }
            sender.sendMessage(ChatColor.GOLD + Configuration.name + " | " + ChatColor.GRAY + "Du bist aktuell in der Queue Nummer " + Start.getQueueNumber((Player) sender));
            sender.sendMessage(ChatColor.GRAY + "Es gibt " + Verteiler.colorA[n] + Verteiler.teamA.get(n).size() + ChatColor.GRAY + " in Team A!");
            sender.sendMessage(ChatColor.GRAY + "Es gibt " + Verteiler.colorB[n] + Verteiler.teamB.get(n).size() + ChatColor.GRAY + " in Team B!");
            return true;
        }
        sender.sendMessage(ChatColor.RED + Configuration.name + " | " + ChatColor.GRAY + "Ungültiger Befehl!");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GRAY + "/" + Configuration.mainCommand + " create | delete <arenaname>");
        sender.sendMessage(ChatColor.GRAY + "/" + Configuration.mainCommand + " pos1 | pos2 | spawn");
        sender.sendMessage(ChatColor.GRAY + "/" + Configuration.mainCommand + " list | <arenaname>");
        sender.sendMessage(ChatColor.GRAY + "/" + Configuration.mainCommand + " help | info [wenn in einem Spiel] | mechanik");
        sender.sendMessage(ChatColor.GRAY + "/" + Configuration.mainCommand + " lobby ");
        sender.sendMessage(ChatColor.GRAY + "/" + Configuration.mainCommand + "queue ");
        sender.sendMessage(ChatColor.GRAY + "/" + Configuration.mainCommand + "leave ");
        sender.sendMessage(ChatColor.GRAY + "/" + Configuration.mainCommand + "points");
        sender.sendMessage(ChatColor.GRAY + "/" + Configuration.mainCommand + "arsenal ");
        sender.sendMessage(ChatColor.GRAY + "/" + Configuration.mainCommand + "stop [admin] ");
        sender.sendMessage(ChatColor.GRAY + "/" + Configuration.mainCommand + "start [admin] ");
        sender.sendMessage(ChatColor.GRAY + "/" + Configuration.mainCommand + "menu [admin] ");
        return false;
    }
}
