package shateq.nopeeking.fabric;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class Rubbernecks {
    private static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().excludeFieldsWithModifiers(Modifier.PRIVATE).create();

    public boolean functional = true; //Still working even if empty blacklist
    public boolean self_visible = false; //Does LocalPlayer want to be alone
    public boolean as_whitelist = false; //Should blacklist be considered whitelist
    //public boolean indicator = false; //Indicator when pointing Rubbernecks

    public List<EntityType<?>> blacklist = new ArrayList<>();

    private Path path;

    public static @NotNull Path configDir() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static @NotNull Rubbernecks defaults(Path path) {
        var rubbernecks = new Rubbernecks();
        rubbernecks.path = path;
        return rubbernecks;
    }

    public static @NotNull Rubbernecks read(Path config) {
        Rubbernecks rubbernecks;

        if (Files.exists(config)) {
            try {
                FileReader reader = new FileReader(config.toFile());
                rubbernecks = GSON.fromJson(reader, Rubbernecks.class);
            } catch (IOException e) {
                throw new RuntimeException("Couldn't read configuration.");
            }

            rubbernecks.path = config;
        } else {
            rubbernecks = defaults(config);
        }

        try {
            rubbernecks.write();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return rubbernecks;
    }

    public void write() throws IOException {
        Path folder = this.path.getParent();
        if (!Files.exists(folder)) {
            Files.createDirectory(folder);
        } else if (!Files.isDirectory(folder)) {
            throw new IOException("Path is not a directory: " + folder);
        }

        Path temp = this.path.resolveSibling(this.path.getFileName() + ".temp");
        Files.writeString(temp, GSON.toJson(this)); //.toJson(this)

        Files.move(temp, this.path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        //Files.deleteIfExists(temp);
    }
}
