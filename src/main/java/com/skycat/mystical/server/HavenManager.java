package com.skycat.mystical.server;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.util.Utils;
import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.dynamic.Codecs;
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
    public static final Codec<HavenManager> CODEC = RecordCodecBuilder.create(instance -> (instance.group(
            Codec.LONG.xmap(ChunkPos::new, ChunkPos::toLong).listOf().xmap(HashSet::new, Utils::setToList).fieldOf("havenedChunks").forGetter(HavenManager::getHavenedChunks), // Take a long, map that to a ChunkPos, make it a list of those, then map that to a set
            Codec.unboundedMap(Codecs.UUID, Codec.INT).xmap(Utils::toHashMap, map -> map).fieldOf("powerMap").forGetter(HavenManager::getPowerMap) // Yes, map -> map. Awesome.
    ).apply(instance, HavenManager::new)));
    @Getter public HashSet<ChunkPos> havenedChunks;
    @Getter public HashMap<UUID, Integer> powerMap;
    private static final File SAVE_FILE = new File("config/havenManager.json");
    public static int baseHavenCost = 1000;

    public HavenManager(HashSet<ChunkPos> havenedChunks, HashMap<UUID, Integer> powerMap) {
        this.havenedChunks = havenedChunks;
        this.powerMap = powerMap;
    }

    public HavenManager() {
        this.havenedChunks = new HashSet<>();
        this.powerMap = new HashMap<>();
    }

    public static HavenManager loadOrNew() {
        try (Scanner scanner = new Scanner(SAVE_FILE)) {
            return Mystical.GSON.fromJson(scanner.nextLine(), HavenManager.class);
        } catch (IOException e) {
            Utils.log(Utils.translateString("text.mystical.logging.failedToLoadHavenManager"), Mystical.CONFIG.failedToLoadHavenManagerLogLevel());
            return new HavenManager();
        }
    }

    public void save() {
        try (PrintWriter pw = new PrintWriter(SAVE_FILE)) {
            pw.println(Mystical.GSON.toJson(this));
        } catch (IOException e) {
            Utils.log(Utils.translateString("text.mystical.logging.failedToSaveHavenManager"), Mystical.CONFIG.failedToSaveHavenManagerLogLevel());
            // TODO: Dump info
        }
    }

    /**
     * Gets the cost to haven the chunk containing a given block position
     *
     * @param blockPos The position of the block
     * @return The cost to haven
     * @implNote Simple overload of {@link #getHavenCost(ChunkPos)}.
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
     * @implNote Simple overload of {@link #getHavenCost(BlockPos)}
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
     * @implNote Simple overload of {@link #havenChunk(ChunkPos)}.
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
     * @implNote Simple overload of {@link #havenChunk(BlockPos)}.
     */
    public boolean havenChunk(int x, int z) {
        return havenChunk(new BlockPos(x, 0, z));
    }

    /**
     * Havens a chunk at a player's expense.
     * @param chunk The chunk to haven.
     * @param player The player to charge.
     * @return {@code true} on success, {@code false} if the havening fails or the player does not have enough power
     */
    public boolean tryHaven(ChunkPos chunk, ServerPlayerEntity player) {
        int cost = getHavenCost(chunk);
        if (hasPower(player, cost) && havenChunk(chunk)) { // Maintain order, lazy boolean operations used.
            // Player has enough power and the haven succeeded
            removePower(player, cost);
            return true;
        }
        return false;
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
     * @implNote Simple overload of {@link #isInHaven(BlockPos)}.
     */
    public boolean isInHaven(int x, int z) {
        return isInHaven(new BlockPos(x, 0, z));
    }

    /**
     * Check if a block is inside a havened chunk.
     *
     * @param blockPos The block position
     * @return {@code true} if the chunk is havened
     * @implNote Simple overload of {@link #isInHaven(ChunkPos)}.
     */
    public boolean isInHaven(BlockPos blockPos) {
        return isInHaven(new ChunkPos(blockPos));
    }

    /**
     * Checks if an entity is inside a havened chunk.
     * @param entity The entity to check.
     * @return {@code true} if the entity is in a havened chunk.
     * @implNote Simple overload of {@link #isInHaven(ChunkPos)}.
     */
    public boolean isInHaven(Entity entity) {
        return isInHaven(entity.getChunkPos());
    }

    /**
     * Gives power to a player.
     * @param player The player to grant power to.
     * @param power The amount of power to grant.
     * @return The amount of power the player has after adding.
     * @implNote Simple overload of {@link #addPower(UUID, int)}.
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
     * @implSpec Calling this ensures that {@code powerMap.get(playerUUID) != null}.
     */
    public int getPower(UUID playerUUID) {
        if (powerMap.get(playerUUID) == null) {
            powerMap.put(playerUUID, 0);
            return 0;
        }
        return powerMap.get(playerUUID);
    }

    /**
     * Get how much power a player has.
     * @param player The player.
     * @return The power the player has, or 0 if they have no power/power is not yet tracked for this player.
     * @implSpec Calling this ensures that {@code powerMap.get(playerUUID) != null}.
     * @implNote Simple overload of {@link #getPower(UUID)}.
     */
    public int getPower(ServerPlayerEntity player) {
        return getPower(player.getUuid());
    }

    /**
     * Removes power from a player, if and only if the player has enough power.
     * @param player The player to remove power from.
     * @param power The amount of power to remove.
     * @return {@code true} on success, {@code false} when the player does not have enough power (so nothing was done).
     * @implNote Simple overload of {@link #removePower(UUID, int)}.
     */
    public boolean removePower(ServerPlayerEntity player, int power) {
        return removePower(player.getUuid(), power);
    }

    /**
     * Removes power from a player, if and only if the player has enough power.
     * @param playerUUID The uuid of the player to remove power from.
     * @param power The amount of power to remove.
     * @return {@code true} on success, {@code false} when the player does not have enough power (so nothing was done).
     */
    public boolean removePower(UUID playerUUID, int power) {
        int prevPower = getPower(playerUUID);
        if (hasPower(playerUUID, power)) {
            powerMap.put(playerUUID, prevPower - power);
            return true;
        }
        return false;
    }

    /**
     * Checks if a player has at least {@code power} power.
     * @param playerUUID The UUID of the player.
     * @param power The amount of power required.
     * @return {@code (player's power) >= power}.
     */
    public boolean hasPower(UUID playerUUID, int power) {
        return getPower(playerUUID) >= power;
    }

    /**
     * Checks if a player has at least {@code power} power.
     * @param player The player.
     * @param power The amount of power required.
     * @return {@code (player's power) >= power}.
     * @implNote Simple overload of {@link #hasPower(UUID, int)}.
     */
    public boolean hasPower(ServerPlayerEntity player, int power) {
        return hasPower(player.getUuid(), power);
    }
}
