package com.skycat.mystical;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skycat.mystical.util.Utils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class HavenManager {
    public static final Codec<HavenManager> CODEC = RecordCodecBuilder.create(instance -> (instance.group(
            Utils.CHUNK_POS_CODEC.listOf().xmap(HashSet::new, Utils::setToList).fieldOf("havenedChunks").forGetter(HavenManager::getHavenedChunks),
            Utils.hashMapCodec(Uuids.CODEC, "player", Codec.INT, "power").fieldOf("powerMap").forGetter(HavenManager::getPowerMap)
    ).apply(instance, HavenManager::new)));
    @Getter private final HashSet<ChunkPos> havenedChunks;
    @Getter private final HashMap<UUID, Integer> powerMap;
    @Getter private static final File SAVE_FILE = new File("config/havenManager.json");
    @Getter @Setter
    private boolean dirty = false;

    public HavenManager(HashSet<ChunkPos> havenedChunks, HashMap<UUID, Integer> powerMap) {
        this.havenedChunks = havenedChunks;
        this.powerMap = powerMap;
    }

    public void resetHavens() {
        havenedChunks.clear();
    }

    public void resetPower() {
        powerMap.clear();
    }

    public HavenManager() {
        this.havenedChunks = new HashSet<>();
        this.powerMap = new HashMap<>();
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
     * @param x The blockX position of the block
     * @param z The blockZ position of the block
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
        return Mystical.CONFIG.baseHavenCost();
    }

    /**
     * Havens a chunk
     *
     * @param chunkPos The position of the chunk to haven
     * @return {@code true} if the chunk was not already havened
     */
    public boolean addHaven(ChunkPos chunkPos) {
        markDirty();
        return havenedChunks.add(chunkPos);
    }

    /**
     * Havens a chunk based on a block position
     *
     * @param blockPos The position of a block inside the targeted chunk
     * @return {@code true} if the chunk was not already havened
     * @implNote Simple overload of {@link #addHaven(ChunkPos)}.
     */
    public boolean addHaven(BlockPos blockPos) {
        return addHaven(new ChunkPos(blockPos));
    }

    /**
     * Havens a chunk based on a block position
     *
     * @param blockX The x position of a block contained in the chunk
     * @param blockZ The z position of a block contained in the chunk
     * @return {@code true} if the chunk was not already havened
     * @implNote Simple overload of {@link #addHaven(BlockPos)}.
     */
    public boolean addHaven(int blockX, int blockZ) {
        return addHaven(new BlockPos(blockX, 0, blockZ));
    }
    
    public void markDirty() {
        dirty = true;
    }

    /**
     * Havens a chunk at a player's expense.
     * @param chunk The chunk to haven.
     * @param player The player to charge.
     * @return {@code true} on success, {@code false} if the havening fails or the player does not have enough power
     */
    public boolean tryHaven(ChunkPos chunk, ServerPlayerEntity player) {
        int cost = getHavenCost(chunk);
        if (hasPower(player, cost) && addHaven(chunk)) { // Maintain order, lazy boolean operations used.
            // Player has enough power and the haven succeeded
            removePower(player, cost);
            MysticalCriteria.MAKE_HAVEN_CRITERION.trigger(player);
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
     * @param x The blockX position of the block
     * @param z The blockZ position of the block
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
        markDirty();
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
            markDirty();
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
            markDirty();
            return true;
        }
        return false;
    }

    /**
     * Set the power that a player has.
     * @param player The UUID of the player to set the power of.
     * @param power The power to set the player to.
     */
    public void setPower(ServerPlayerEntity player, int power) {
        setPower(player.getUuid(), power);
    }

    /**
     * Set the power that a player has.
     * @param playerUUID The UUID of the player to set the power of.
     * @param power The power to set the player to.
     */
    public void setPower(UUID playerUUID, int power) {
        powerMap.put(playerUUID, power);
        markDirty();
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


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HavenManager other)) return false;
        if (!powerMap.equals(other.powerMap)) return false;
        return havenedChunks.equals(other.havenedChunks);
    }

}
