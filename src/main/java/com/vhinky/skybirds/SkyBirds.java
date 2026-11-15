package com.vhinky.skybirds;

import com.vhinky.skybirds.network.ForecastPayload;
import com.vhinky.skybirds.network.ServerWeather;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

/**
 * Sky Birds — атмосферные птицы в небе (частицы, а не мобы).
 *
 * Общая часть — работает и на клиенте, и на сервере.
 * На сервере мод необязателен: если он там стоит, игроки с модом получают
 * прогноз погоды (птицы летают низко перед дождём). Игроки без мода
 * спокойно заходят на такой сервер.
 * Всё, что касается отрисовки птиц, — в {@link SkyBirdsClient}.
 */
public final class SkyBirds implements ModInitializer {
    public static final String MODID = "sky_birds";

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(ForecastPayload.TYPE, ForecastPayload.STREAM_CODEC);
        ServerTickEvents.END_SERVER_TICK.register(ServerWeather::onServerTick);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> ServerWeather.onPlayerJoin(handler.getPlayer()));
    }
}
