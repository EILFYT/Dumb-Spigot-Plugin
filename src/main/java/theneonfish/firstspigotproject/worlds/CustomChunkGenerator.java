package theneonfish.firstspigotproject.worlds;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class CustomChunkGenerator extends ChunkGenerator {
    @Override
    public void generateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
    /*    for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    Material material;
                    double d = random.nextDouble();
                        if (d <= 0.33) material = Material.GOLD_BLOCK;
                        else if (d <= 0.66) material = Material.DIAMOND_BLOCK;
                        else material = Material.DEEPSLATE;
                    chunkData.setBlock(x, 1, z, material);
            }
        }*/
    }

}
