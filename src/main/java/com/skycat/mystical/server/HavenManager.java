package com.skycat.mystical.server;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.UUID;

public class HavenManager {
    public HashSet<ChunkPos> havenedChunks = new HashSet<>();
    public HashMap<UUID, Integer> powerMap = new HashMap<>();
    private static final File SAVE_FILE = new File("config/havenManager.json");
    public static HavenManager loadOrNew() {
        try (Scanner scanner = new Scanner(SAVE_FILE)) {
            return Mystical.GSON.fromJson(scanner.nextLine(), HavenManager.class);
        } catch (IOException e) {
            Utils.log(Utils.translateString("text.mystical.havenManager.loadFailed"), Mystical.CONFIG.failedToLoadHavenManagerLogLevel());
            return new HavenManager();
        }
    }

    public static int baseHavenCost = 1000;

    public void save() {
        try (PrintWriter pw = new PrintWriter(SAVE_FILE)) {
            pw.println(Mystical.GSON.toJson(this));
        } catch (IOException e) {
            Utils.log(Utils.translateString("text.mystical.havenManager.saveFailed"), Mystical.CONFIG.failedToSaveHavenManagerLogLevel());
            // TODO: Dump info
        }
    }

    /**
     * Gets the cost to haven the chunk containing a given block position
     *
     * @param blockPos The position of the block
     * @return The cost to haven
     */
    public int getHavenCost(BlockPos blockPos) {
        return getHavenCost(new ChunkPos(blockPos));
    }

    /**
     * Gets the cost to haven the chunk containing a given block position
     *
     * @param x The x position of the block
     * @param z The z position of the block
     * @return The cost
     */
    public int getHavenCost(int x, int z) {
        return getHavenCost(new BlockPos(x, 0, z));
    }

    /**
     * Gets the cost to haven a specific chunk
     *
     * @param chunkPos The position of the chunk
     * @return The cost
     */
    public int getHavenCost(ChunkPos chunkPos) {
        return baseHavenCost; // TODO: allow for different costs for different chunks
    }

    /**
     * Havens a chunk
     *
     * @param chunkPos The position of the chunk to haven
     * @return {@code true} if the chunk was not already havened
     */
    public boolean havenChunk(ChunkPos chunkPos) {
        return havenedChunks.add(chunkPos);
    }

    /**
     * Havens a chunk based on a block position
     *
     * @param blockPos The position of a block inside the targeted chunk
     * @return {@code true} if the chunk was not already havened
     */
    public boolean havenChunk(BlockPos blockPos) {
        return havenChunk(new ChunkPos(blockPos));
    }

    /**
     * Havens a chunk based on a block position
     *
     * @param x The x position of a block in the chunk
     * @param z The z position of a block in the chunk
     * @return {@code true} if the chunk was not already havened
     */
    public boolean havenChunk(int x, int z) {
        return havenChunk(new BlockPos(x, 0, z));
    }

    public boolean tryHaven(ChunkPos chunk, PlayerEntity player) {
        // STOPSHIP: Check if player has money, charge them
        return havenChunk(chunk);
    }

    /**
     * Check if a chunk is havened
     *
     * @param chunkPos The position of the chunk to check
     * @return {@code true} if the chunk is havened
     */
    public boolean isInHaven(ChunkPos chunkPos) {
        return havenedChunks.contains(chunkPos);
    }

    /**
     * Check if a block is inside a havened chunk
     *
     * @param x The x position of the block
     * @param z The z position of the block
     * @return {@code true} if the chunk is havened
     */
    public boolean isInHaven(int x, int z) {
        return isInHaven(new BlockPos(x, 0, z));
    }

    /**
     * Check if a block is inside a havened chunk.
     *
     * @param blockPos The block position
     * @return {@code true} if the chunk is havened
     */
    public boolean isInHaven(BlockPos blockPos) {
        return isInHaven(new ChunkPos(blockPos));
    }

    public boolean isInHaven(Entity entity) {
        return isInHaven(entity.getChunkPos());
    }

    /**
     * Gives power to a player.
     * @param player The player to grant power to.
     * @param power The amount of power to grant.
     * @return The amount of power the player has after adding.
     */
    public int addPower(ServerPlayerEntity player, int power) {
        return addPower(player.getUuid(), power);
    }

    /**
     * Gives power to a player.
     * @param playerUUID The UUID of a player.
     * @param power The amount of power to grant.
     * @return The total power the player has after adding power.
     */
    public int addPower(UUID playerUUID, int power) {
        Integer currentPower = powerMap.get(playerUUID);
        if (currentPower != null) { // I believe this implies powerMap.containsKey(playerUUID)
            int sum = currentPower + power;
            powerMap.put(playerUUID, sum);
            return sum;
        }
        // else
        powerMap.put(playerUUID, power);
        return power;
    }

    /**
     * Get how much power a player has.
     * @param playerUUID The UUID of the player.
     * @return The power a player has, or 0 if they have no power/power is not yet tracked for this player.
     */
    public int getPower(UUID playerUUID) {
        if (powerMap.get(playerUUID) == null) {
            return 0;
        }
        return powerMap.get(playerUUID);
    }

    /**
     * Get how much power a player has.
     * @param player The player.
     * @return The power the player has, or 0 if they have no power/power is not yet tracked for this player.
     */
    public int getPower(ServerPlayerEntity player) {
        return getPower(player.getUuid());
    }
}
