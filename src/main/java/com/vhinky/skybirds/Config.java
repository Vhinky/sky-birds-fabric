package com.vhinky.skybirds;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Настройки Sky Birds для Fabric: файл config/sky_birds.properties.
 * Те же параметры, что и в версии для NeoForge. Файл создаётся при первом запуске;
 * изменения подхватываются после перезапуска игры.
 */
public final class Config {
    private static final String FILE = "sky_birds.properties";

    public static final Value<Boolean> ENABLED = new Value<>(true);
    public static final Value<Double> FREQUENCY = new Value<>(1.0);
    public static final Value<Integer> MAX_FLOCKS = new Value<>(5);
    public static final Value<Double> SIZE_MULTIPLIER = new Value<>(1.0);

    private Config() {}

    public static void load() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve(FILE);
        Properties p = new Properties();
        if (Files.exists(path)) {
            try (Reader r = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                p.load(r);
            } catch (IOException ignored) {
                // битый файл — берём значения по умолчанию
            }
        }
        ENABLED.set(Boolean.parseBoolean(p.getProperty("enabled", "true")));
        FREQUENCY.set(clamp(parseDouble(p.getProperty("frequency"), 1.0), 0.1, 5.0));
        MAX_FLOCKS.set((int) clamp(parseDouble(p.getProperty("maxFlocks"), 5), 1, 20));
        SIZE_MULTIPLIER.set(clamp(parseDouble(p.getProperty("sizeMultiplier"), 1.0), 0.5, 3.0));
        save(path);
    }

    private static void save(Path path) {
        Properties p = new Properties();
        p.setProperty("enabled", String.valueOf(ENABLED.get()));
        p.setProperty("frequency", String.valueOf(FREQUENCY.get()));
        p.setProperty("maxFlocks", String.valueOf(MAX_FLOCKS.get()));
        p.setProperty("sizeMultiplier", String.valueOf(SIZE_MULTIPLIER.get()));
        try {
            Files.createDirectories(path.getParent());
            try (Writer w = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                p.store(w, "Sky Birds: enabled (true/false), frequency (0.1-5), maxFlocks (1-20), sizeMultiplier (0.5-3)");
            }
        } catch (IOException ignored) {
            // не смогли записать — не страшно
        }
    }

    private static double parseDouble(String s, double def) {
        if (s == null) return def;
        try {
            return Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    /** Значение настройки: тот же вид обращения, что в NeoForge (Config.X.get()). */
    public static final class Value<T> {
        private T value;

        Value(T def) {
            this.value = def;
        }

        public T get() {
            return value;
        }

        void set(T v) {
            this.value = v;
        }
    }
}
