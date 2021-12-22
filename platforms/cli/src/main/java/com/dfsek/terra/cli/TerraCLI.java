package com.dfsek.terra.cli;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;

import com.dfsek.terra.api.util.vector.Vector2Int;
import com.dfsek.terra.cli.world.CLIWorld;

import net.querz.mca.MCAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public final class TerraCLI {
    private static final Logger LOGGER = LoggerFactory.getLogger(TerraCLI.class);
    
    public static void main(String... args) {
        LOGGER.info("Starting Terra CLI...");
        
        CLIPlatform platform = new CLIPlatform();
        platform.getEventManager().callEvent(new PlatformInitializationEvent());
    
        ConfigPack generate = platform.getConfigRegistry().get("OVERWORLD").orElseThrow(); // TODO: make this a cli argument
    
        CLIWorld world = new CLIWorld(1, 2, 384, -64, generate);
        
        world.generate();
        
        world.serialize().forEach(mcaFile -> {
            Vector2Int pos = mcaFile.getLeft();
            String name = MCAUtil.createNameFromRegionLocation(pos.getX(), pos.getZ());
            LOGGER.info("Writing region ({}, {}) to {}", pos.getX(), pos.getZ(), name);
            mcaFile.getRight().cleanupPalettesAndBlockStates();
            
            try {
                MCAUtil.write(mcaFile.getRight(), name);
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
    }
}
