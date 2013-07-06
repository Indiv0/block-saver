package in.nikitapek.blocksaver.management;

import in.nikitapek.blocksaver.serialization.Reinforcement;
import in.nikitapek.blocksaver.util.BlockSaverConfigurationContext;
import in.nikitapek.blocksaver.util.BlockSaverFeedback;
import in.nikitapek.blocksaver.util.BlockSaverPrismBridge;
import in.nikitapek.blocksaver.util.BlockSaverUtil;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class FeedbackManager {
    private static final byte PITCH_SHIFT = 50;

    private final BlockSaverInfoManager infoManager;

    private final Effect reinforcementDamageFailEffect;
    private final Effect reinforcementDamageSuccessEffect;
    private final Sound reinforceSuccessSound;
    private final Sound reinforceFailSound;
    private final Sound hitFailSound;

    private final String primaryFeedback;

    private boolean prismBridged = false;

    private BlockSaverPrismBridge prismBridge;

    public FeedbackManager(final BlockSaverConfigurationContext configurationContext) {
        this.infoManager = configurationContext.infoManager;

        this.reinforcementDamageFailEffect = configurationContext.reinforcementDamageFailEffect;
        this.reinforcementDamageSuccessEffect = configurationContext.reinforcementDamageSuccessEffect;
        this.reinforceSuccessSound = configurationContext.reinforceSuccessSound;
        this.reinforceFailSound = configurationContext.reinforceFailSound;
        this.hitFailSound = configurationContext.hitFailSound;
        this.primaryFeedback = configurationContext.primaryFeedback;

        prismBridge = null;

        if (!configurationContext.enableLogging) {
            return;
        }

        try {
            prismBridge = new BlockSaverPrismBridge(configurationContext.plugin);
        } catch (final NoClassDefFoundError ex) {
            configurationContext.plugin.getLogger().log(Level.WARNING, "\"enableLogging\" true but Prism not found. Logging will not be enabled.");
            return;
        }

        prismBridged = true;
    }

    public void sendFeedback(final Location location, final BlockSaverFeedback feedback, final Player player) {
        Reinforcement reinforcement = infoManager.getReinforcement(location);

        switch (feedback) {
            case REINFORCE_SUCCESS:
                location.getWorld().playSound(location, reinforceSuccessSound, 1.0f, PITCH_SHIFT);
                if (player == null) {
                    break;
                }
                if (isPrismBridged()) {
                    prismBridge.logCustomEvent(reinforcement, player, BlockSaverPrismBridge.ENFORCE_EVENT);
                }
                if (infoManager.getPlayerInfo(player.getName()).isReceivingTextFeedback() && player.hasPermission("blocksaver.feedback.reinforce.success")) {
                    player.sendMessage(ChatColor.GRAY + "Reinforced a block.");
                }
                break;
            case REINFORCE_FAIL:
                location.getWorld().playSound(location, reinforceFailSound, 1.0f, PITCH_SHIFT);
                if (player != null && infoManager.getPlayerInfo(player.getName()).isReceivingTextFeedback() && player.hasPermission("blocksaver.feedback.reinforce.fail")) {
                    player.sendMessage(ChatColor.GRAY + "Failed to reinforce a block.");
                }
                break;
            case DAMAGE_SUCCESS:
                if (player == null) {
                    break;
                }
                if (infoManager.getPlayerInfo(player.getName()).isReceivingTextFeedback() && player.hasPermission("blocksaver.feedback.damage.success")) {
                    player.sendMessage(ChatColor.GRAY + "Damaged a reinforced block.");
                }
                if ("visual".equals(primaryFeedback)) {
                    BlockSaverUtil.sendParticleEffect(location, (int) infoManager.getReinforcement(location).getReinforcementValue());
                } else if ("auditory".equals(primaryFeedback)) {
                    BlockSaverUtil.playMusicalEffect(location, (int) infoManager.getReinforcement(location).getReinforcementValue());
                } else {
                    location.getWorld().playEffect(location, reinforcementDamageSuccessEffect, 0);
                }
                if (isPrismBridged()) {
                    prismBridge.logCustomEvent(reinforcement, player, BlockSaverPrismBridge.DAMAGE_EVENT);
                }
                break;
            case DAMAGE_FAIL:
                location.getWorld().playEffect(location, reinforcementDamageFailEffect, 0);
                if (player != null && infoManager.getPlayerInfo(player.getName()).isReceivingTextFeedback() && player.hasPermission("blocksaver.feedback.damage.fail")) {
                    player.sendMessage(ChatColor.GRAY + "Failed to damage a reinforced block.");
                }
                break;
            case HIT_FAIL:
                location.getWorld().playSound(location, hitFailSound, 1.0f, 0f);
                if (player != null && infoManager.getPlayerInfo(player.getName()).isReceivingTextFeedback() && player.hasPermission("blocksaver.feedback.hit")) {
                    player.sendMessage(ChatColor.GRAY + "Your tool is insufficient to damage this reinforced block.");
                }
                break;
            case PERMISSIONS_FAIL:
                location.getWorld().playSound(location, hitFailSound, 1.0f, 0f);
                if (player != null && infoManager.getPlayerInfo(player.getName()).isReceivingTextFeedback() && player.hasPermission("blocksaver.feedback.permissions")) {
                    player.sendMessage(ChatColor.GRAY + "You do not have the necessary permissions for this action.");
                }
                break;
            default:
                break;
        }
    }

    public boolean isPrismBridged() {
        return prismBridged;
    }
}
