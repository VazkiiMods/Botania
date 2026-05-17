package vazkii.botania.fabric.block_entity;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.block.block_entity.red_string.RedStringContainerBlockEntity;
import vazkii.botania.fabric.internal_caps.RedStringContainerStorage;

import java.util.EnumMap;

public class FabricRedStringContainerBlockEntity extends RedStringContainerBlockEntity {
	@Nullable
	private RedStringContainerStorage storage;
	private final EnumMap<Direction, RedStringContainerStorage> directionalStorages = new EnumMap<>(Direction.class);
	@Nullable
	private BlockPos clientPos;

	public FabricRedStringContainerBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	@Nullable
	public static Storage<ItemVariant> getStorage(RedStringContainerBlockEntity container, @Nullable Direction direction) {
		if (container instanceof FabricRedStringContainerBlockEntity c) {
			return c.storage(direction);
		}
		return null;
	}

	public Storage<ItemVariant> storage(@Nullable Direction direction) {
		if (direction == null) {
			if (storage == null) {
				storage = new RedStringContainerStorage(this, null);
			}
			return storage;
		}
		return directionalStorages.computeIfAbsent(direction, dir -> new RedStringContainerStorage(this, dir));
	}

	@Override
	public void onBound(@Nullable BlockPos pos) {
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		var tag = super.getUpdateTag(registries);
		// We cannot query for the storage api on the client - so we send the binding position.
		BlockPos binding = getBinding();
		// TODO: could be replaced by block state properties, if we accept the binding range is fixed
		if (binding == null) {
			// hack: empty NBT gets the packet ignored but we don't want that
			tag.putByte("-", (byte) 0);
		} else {
			tag.putInt("bindX", binding.getX());
			tag.putInt("bindY", binding.getY());
			tag.putInt("bindZ", binding.getZ());
		}
		return tag;
	}

	@Override
	protected void loadAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		if (cmp.contains("bindX")) {
			clientPos = new BlockPos(cmp.getInt("bindX"), cmp.getInt("bindY"), cmp.getInt("bindZ"));
		} else {
			clientPos = null;
		}
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Nullable
	@Override
	public BlockPos getBinding() {
		return level.isClientSide ? clientPos : super.getBinding();
	}
}
