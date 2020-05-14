package dev.itsmeow.plotsquarednightvision;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.intellectualsites.plotsquared.api.PlotAPI;
import com.github.intellectualsites.plotsquared.bukkit.events.PlayerEnterPlotEvent;
import com.github.intellectualsites.plotsquared.bukkit.events.PlayerLeavePlotEvent;
import com.github.intellectualsites.plotsquared.bukkit.events.PlotFlagAddEvent;
import com.github.intellectualsites.plotsquared.bukkit.events.PlotFlagRemoveEvent;
import com.github.intellectualsites.plotsquared.plot.flag.BooleanFlag;
import com.github.intellectualsites.plotsquared.plot.object.Plot;
import com.github.intellectualsites.plotsquared.plot.object.PlotPlayer;

import kr.entree.spigradle.Plugin;

@Plugin
public class PlotSquaredNightVision extends JavaPlugin implements Listener {

    public BooleanFlag nightVisionFlag;
    private static final PotionEffect NIGHT_VISION = new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 1, false, false);

    @Override
    public void onEnable() {
        PlotAPI api = new PlotAPI();
        api.addFlag(this.nightVisionFlag = new BooleanFlag("nightvision"));
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    private boolean nightVision(Plot plot) {
        return plot.getFlag(nightVisionFlag, false);
    }

    private static boolean hasNightVision(Player player) {
        return player.hasPotionEffect(PotionEffectType.NIGHT_VISION);
    }

    @EventHandler
    public void onPlayerEnterPlot(PlayerEnterPlotEvent event) {
        if(nightVision(event.getPlot()) && !hasNightVision(event.getPlayer())) {
            event.getPlayer().addPotionEffect(NIGHT_VISION);
        }
    }

    @EventHandler
    public void onPlayerExitPlot(PlayerLeavePlotEvent event) {
        if(nightVision(event.getPlot()) && hasNightVision(event.getPlayer())) {
            event.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
    }

    @EventHandler
    public void flagAdd(PlotFlagAddEvent event) {
        if(event.getFlag() == nightVisionFlag && nightVision(event.getPlot())) {
            for(PlotPlayer playerP : event.getPlot().getPlayersInPlot()) {
                Player player = this.getServer().getPlayer(playerP.getUUID());
                if(!hasNightVision(player)) {
                    // ?????????????????
                    player.addPotionEffect(NIGHT_VISION);
                }
            }
        }
    }

    @EventHandler
    public void flagRemove(PlotFlagRemoveEvent event) {
        if(event.getFlag() == nightVisionFlag && !nightVision(event.getPlot())) {
            for(PlotPlayer playerP : event.getPlot().getPlayersInPlot()) {
                Player player = this.getServer().getPlayer(playerP.getUUID());
                if(hasNightVision(player)) {
                    // ?????????????????
                    player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                }
            }
        }
    }

}
