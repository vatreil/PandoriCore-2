package org.example;

import fr.pandorica.redis.RedisManager;
import fr.pandorica.redis.RedisPlayerSkin;
import net.minestom.server.instance.*;
import net.minestom.server.scoreboard.Team;

import java.util.UUID;

public class Main {

    public static Instance instance;
    public static Team team;
    public static void main(String[] args) {

        new RedisManager("localhost", "").connexion();

        RedisPlayerSkin.getSkin(UUID.fromString("d49d750d-ec66-4677-9841-687d5ec03145"));

//        // Initialization
//        MinecraftServer minecraftServer = MinecraftServer.init();
//        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
//        // Create the instance
//        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
//        // Set the ChunkGenerator
//        instanceContainer.setGenerator(unit ->
//                unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));
//        // Add an event callback to specify the spawning instance (and the spawn position)
//        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
//        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
//            final Player player = event.getPlayer();
//            event.setSpawningInstance(instanceContainer);
//            player.setRespawnPoint(new Pos(0, 42, 0));
//        });
//
//
//
//        // Start the server on port 25565
//        minecraftServer.start("0.0.0.0", 25565);
//
//
//        globalEventHandler.addChild(
//                PvPConfig.emptyBuilder()
//                        .potion(PotionConfig.legacyBuilder().drinking(false))
//                        .build().createNode()
//        );
//
//        team = MinecraftServer.getTeamManager().createBuilder("default")
//                .collisionRule(TeamsPacket.CollisionRule.NEVER)
//                .build();
//
//        PvpExtension.init();
//        FoodConfig foodConfig = FoodConfig.emptyBuilder(false).build();
//        DamageConfig damageConfig = DamageConfig.legacyBuilder()
//                .fallDamage(false)
//                .equipmentDamage(false)
//                .exhaustion(false)
//                .build();
//
//        PvPConfig pvPConfig = PvPConfig.legacyBuilder()
//                .food(foodConfig)
//                .damage(damageConfig)
//                .build();
//
//        instance = MinecraftServer.getInstanceManager().getInstances().iterator().next();
//        MinecraftServer.getGlobalEventHandler().addChild(pvPConfig.createNode());
//        instance.setExplosionSupplier(PvpExplosionSupplier.INSTANCE);


    }
}