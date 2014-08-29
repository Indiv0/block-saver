package in.nikitapek.blocksaver.logging.prism;

import in.nikitapek.blocksaver.events.ReinforcedBlockDamageEvent;
import in.nikitapek.blocksaver.management.BlockSaverInfoManager;
import me.botsko.prism.Prism;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public final class PrismReinforcedBlockDamageListener implements Listener {
    private final BlockSaverInfoManager infoManager;

    public PrismReinforcedBlockDamageListener(BlockSaverInfoManager infoManager) {
        this.infoManager = infoManager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void listen(ReinforcedBlockDamageEvent event) {
        Location location = event.getBlock().getLocation();

        if (event.isLogged()) {
            Prism.actionsRecorder.addToQueue(new BlockSaverAction(location, event.getPlayerName(), PrismBridge.DAMAGE_EVENT, infoManager.getReinforcement(location)));
        }
    }
}
