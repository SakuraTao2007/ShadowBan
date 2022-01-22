package cn.loyisa.shadowban.tasks;

import cn.loyisa.shadowban.ShadowBan;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BanTask implements Runnable {

    private final ShadowBan shadowBan;

    public BanTask(ShadowBan shadowBan) {
        this.shadowBan = shadowBan;
    }

    @Override
    public void run() {
        for (UUID uuid : shadowBan.shadowBanMap.keySet()) {
            if (System.currentTimeMillis() < shadowBan.shadowBanMap.get(uuid)) {
                continue;
            }
            Player player = Bukkit.getPlayer(uuid);
            FileConfiguration config = shadowBan.getConfigManager().getConfig();
            List<String> commandsList = new ArrayList<>();
            if (config.isString("banCommands")) {
                commandsList.add(config.getString("banCommands"));
            } else if (config.isList("banCommands")) {
                commandsList.addAll(config.getStringList("banCommands"));
            }
            for (String command : commandsList) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, command));
            }
            shadowBan.getStorageManager().getStorageEngine().remove(player);
        }
    }


}
