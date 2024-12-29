package cpw.mods.modlauncher;

import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.IModuleLayerManager;
import cpw.mods.modlauncher.api.TypesafeMap;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.fabricmc.loader.impl.game.GameProvider;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Launcher {
    private static final String MLSPEC_VERSION = "9.0";
    private static final String MLIMPL_VERSION = "10.0+kilt.1";

    public static Launcher INSTANCE = new Launcher();
    private final TypesafeMap blackboard;
    private final Environment environment;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final ClassLoader classLoader; // Here in case it's reflected into
    private List<Map<String, String>> kilt$modList;

    private Launcher() {
        this.blackboard = new TypesafeMap();
        this.environment = new Environment();
        environment.computePropertyIfAbsent(IEnvironment.Keys.MLSPEC_VERSION.get(), s -> MLSPEC_VERSION);
        environment.computePropertyIfAbsent(IEnvironment.Keys.MLIMPL_VERSION.get(), s -> MLIMPL_VERSION);
        environment.computePropertyIfAbsent(IEnvironment.Keys.MODLIST.get(), s -> kilt$modList = new ArrayList<>());
        this.classLoader = this.getClass().getClassLoader();
    }

    public void kilt$populateEnvironment() {
        FabricLoader loader = FabricLoader.getInstance();
        ModMetadata minecraftMetadata = loader.getModContainer("minecraft").orElseThrow().getMetadata();
        GameProvider provider = ((FabricLoaderImpl) loader).getGameProvider();

        String assetsDir = provider.getArguments().get("assetsDir");
        Path assetsPath;
        if (assetsDir != null) {
            assetsPath = Paths.get(assetsDir);
        } else {
            assetsPath = loader.getGameDir().resolve("assets");
        }

        String uuid = provider.getArguments().get("uuid");
        if (uuid != null) {
            environment.computePropertyIfAbsent(IEnvironment.Keys.UUID.get(), s -> uuid);
        }

        environment.computePropertyIfAbsent(IEnvironment.Keys.VERSION.get(), s -> minecraftMetadata.getVersion().getFriendlyString());
        environment.computePropertyIfAbsent(IEnvironment.Keys.GAMEDIR.get(), s -> loader.getGameDir());
        environment.computePropertyIfAbsent(IEnvironment.Keys.ASSETSDIR.get(), s -> assetsPath);
        environment.computePropertyIfAbsent(IEnvironment.Keys.LAUNCHTARGET.get(), s -> "kilt");
    }

    public final TypesafeMap blackboard() {
        return blackboard;
    }

    public Environment environment() {
        return environment;
    }

    public Optional<IModuleLayerManager> findLayerManager() {
        return Optional.empty();
    }
}
