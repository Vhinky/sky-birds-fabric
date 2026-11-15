package com.vhinky.skybirds;

import com.vhinky.skybirds.client.FlockManager;
import com.vhinky.skybirds.network.ForecastPayload;
import com.vhinky.skybirds.network.ReceivedForecast;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

/**
 * Клиентская часть Sky Birds: настройки и сами птицы.
 * На выделенном сервере этот класс не загружается.
 * Испуг от звуков и отрисовка птиц подключены миксинами (пакет mixin).
 */
public final class SkyBirdsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Config.load();
        ClientTickEvents.END_CLIENT_TICK.register(FlockManager::onClientTick);
        ClientPlayNetworking.registerGlobalReceiver(ForecastPayload.TYPE,
                (payload, context) -> ReceivedForecast.update(payload));
    }
}
