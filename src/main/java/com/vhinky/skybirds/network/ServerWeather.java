package com.vhinky.skybirds.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.ServerLevelData;

/**
 * Серверная часть: раз в 5 секунд рассказывает игрокам с модом,
 * когда начнётся дождь. Игрокам без мода ничего не отправляется.
 */
public final class ServerWeather {
    private static final int SEND_INTERVAL = 100;

    private ServerWeather() {}

    public static void onServerTick(MinecraftServer server) {
        if (server.getTickCount() % SEND_INTERVAL != 0) return;
        ForecastPayload payload = current(server);
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            send(player, payload);
        }
    }

    public static void onPlayerJoin(ServerPlayer player) {
        send(player, current(player.server));
    }

    private static ForecastPayload current(MinecraftServer server) {
        ServerLevelData data = server.getWorldData().overworldData();
        return new ForecastPayload(data.getRainTime(), data.getClearWeatherTime());
    }

    private static void send(ServerPlayer player, ForecastPayload payload) {
        if (ServerPlayNetworking.canSend(player, ForecastPayload.TYPE)) {
            ServerPlayNetworking.send(player, payload);
        }
    }
}
