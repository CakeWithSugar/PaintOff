package mc.cws.paintOff;

import mc.cws.paintOff.Game.Arena.Arena;
import mc.cws.paintOff.Game.Arena.ArenaAuswahl;
import mc.cws.paintOff.Ultimates.*;
import mc.cws.paintOff.Listener.*;
import mc.cws.paintOff.Game.Management.InGame.SnowballEffect;
import mc.cws.paintOff.Game.Shop.Lists;
import mc.cws.paintOff.PrimaryWeapons.Normal.Abwandlung.FiftySnipEx;
import mc.cws.paintOff.PrimaryWeapons.Normal.Abwandlung.Pikolat;
import mc.cws.paintOff.PrimaryWeapons.Prime.PrimeMillilat;
import mc.cws.paintOff.PrimaryWeapons.Normal.*;
import mc.cws.paintOff.Game.Management.DeadStop;
import mc.cws.paintOff.Listener.ArsenalInventoryListener;
import mc.cws.paintOff.Po.Executors.PoArsenal;
import mc.cws.paintOff.PrimaryWeapons.Haepec.HaeSchnipsLehr;
import mc.cws.paintOff.PrimaryWeapons.Prime.PrimeSchweddler;
import mc.cws.paintOff.PrimaryWeapons.Pyrex.PyrTwentySniplEx;
import mc.cws.paintOff.PrimaryWeapons.Pyrex.PyrBlubber;
import mc.cws.paintOff.PrimaryWeapons.Tulipa.TulQuinter;
import mc.cws.paintOff.PrimaryWeapons.Tulipa.TulTriAtler;
import mc.cws.paintOff.SecondaryWeapons.*;
import mc.cws.paintOff.Game.Shop.ShopInventory;
import mc.cws.paintOff.Po.TeleportInventoryListener;
import mc.cws.paintOff.Game.Management.InGame.Game;
import mc.cws.paintOff.Game.Management.Queue;
import mc.cws.paintOff.Game.Management.Start;
import mc.cws.paintOff.Game.Management.Stop;
import mc.cws.paintOff.Po.*;
import mc.cws.paintOff.Po.Executors.*;
import mc.cws.paintOff.Po.Modifications;
import mc.cws.paintOff.Po.Executors.PoMenu;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.Objects;

public final class PaintOffMain extends JavaPlugin {

    @Override
    public void onEnable() {
        setPlugins();
        registerListeners();

        Start.initializeArrays();
        ShopInventory.initialize();
        Lists.initializeMaps();

        // Initialize directories
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            if (!dataFolder.mkdirs()) {
                getLogger().warning("Failed to create plugin directory: " + dataFolder.getAbsolutePath());
            }
        }

        // Create po-arenas folder directly in plugin folder
        File arenasFolder = new File(Configuration.mainCommand+"-arenas");
        if (!arenasFolder.exists()) {
            if (!arenasFolder.mkdirs()) {
                getLogger().warning("Failed to create po-arenas directory: " + arenasFolder.getAbsolutePath());
            }
        }

        Queue.setParameters();
        Modifications.onEnable(this);


        Objects.requireNonNull(getCommand(Configuration.mainCommand+"leave")).setExecutor(new PoLeave());
        Objects.requireNonNull(getCommand(Configuration.mainCommand+"queue")).setExecutor(new PoQueue());
        Objects.requireNonNull(getCommand(Configuration.mainCommand+"start")).setExecutor(new PoStart());
        Objects.requireNonNull(getCommand(Configuration.mainCommand+"stop")).setExecutor(new PoStop());
        Objects.requireNonNull(getCommand(Configuration.mainCommand+"points")).setExecutor(new PoPoints());
        Objects.requireNonNull(getCommand(Configuration.mainCommand+"arsenal")).setExecutor(new PoArsenal());
        Objects.requireNonNull(getCommand(Configuration.mainCommand+"menu")).setExecutor(new PoMenu());
        Objects.requireNonNull(getCommand(Configuration.mainCommand)).setExecutor(new Po());

        for (Player player : getServer().getOnlinePlayers()) {
            Queue.queueGame(player);
        }
    }

    @Override
    public void onDisable() {
    }

    private void registerListeners () {
        getServer().getPluginManager().registerEvents(new Game(), this);
        getServer().getPluginManager().registerEvents(new JoinerAndLeaver(), this);
        getServer().getPluginManager().registerEvents(new ArsenalInventoryListener(), this);
        getServer().getPluginManager().registerEvents(new TeleportInventoryListener(), this);
        getServer().getPluginManager().registerEvents(new Modifications(), this);
        getServer().getPluginManager().registerEvents(new ArenaAuswahl(), this);
        getServer().getPluginManager().registerEvents(new ShopInventory(), this);

        getServer().getPluginManager().registerEvents(new DamageListener(),this);
        getServer().getPluginManager().registerEvents(new MovementListener(),this);
        getServer().getPluginManager().registerEvents(new ItemPickupListener(),this);
        getServer().getPluginManager().registerEvents(new DropItemListener(),this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(),this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(),this);
    }

    private void setPlugins() {
        Queue.setPlugin(this);
        Start.setPlugin(this);
        Game.setPlugin(this);
        SnowballEffect.setPlugin(this);
        Stop.setPlugin(this);
        ArenaManager.setPlugin(this);
        Arena.setPlugin(this);
        TeleportInventoryListener.setPlugin(this);
        Modifications.setPlugin(this);
        ArenaAuswahl.setPlugin(this);
        ShopInventory.setPlugin(this);
        DeadStop.setPlugin(this);

        Testing.setPlugin(this);

        SchnipsLehr.setPlugin(this);
        TriAtler.setPlugin(this);
        Quinter.setPlugin(this);
        TulTriAtler.setPlugin(this);
        Schweddler.setPlugin(this);
        SniplEx.setPlugin(this);
        Blubber.setPlugin(this);
        FiftySnipEx.setPlugin(this);
        PrimeSchweddler.setPlugin(this);
        Mikrolat.setPlugin(this);
        TulQuinter.setPlugin(this);
        Pikolat.setPlugin(this);
        PyrBlubber.setPlugin(this);
        HaeSchnipsLehr.setPlugin(this);
        PyrTwentySniplEx.setPlugin(this);
        PrimeMillilat.setPlugin(this);
        Musket.setPlugin(this);
        TulMusket.setPlugin(this);

        Aufdecker.setPlugin(this);
        Klotzbombe.setPlugin(this);
        Kreuzer.setPlugin(this);
        Marker.setPlugin(this);
        Reihenzieher.setPlugin(this);
        Vernebler.setPlugin(this);
        Amalgamalge.setPlugin(this);
        Klotzmine.setPlugin(this);
        Dreifarber.setPlugin(this);

        Wellenschlag.setPlugin(this);
        Tornedo.setPlugin(this);
        Sonnenwindler.setPlugin(this);
        PlatzRegen.setPlugin(this);
        GlitzerMeteor.setPlugin(this);
        Eruptor.setPlugin(this);
        Klotzhagel.setPlugin(this);
        Lauffeuer.setPlugin(this);
    }
}
