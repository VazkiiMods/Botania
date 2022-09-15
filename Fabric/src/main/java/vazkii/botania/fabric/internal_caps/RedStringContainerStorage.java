package vazkii.botania.fabric.internal_caps;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiCache;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.block.block_entity.red_string.RedStringContainerBlockEntity;

import java.util.Iterator;

@SuppressWarnings("UnstableApiUsage")
public class RedStringContainerStorage implements Storage<ItemVariant> {
	private final RedStringContainerBlockEntity container;
	private final Direction direction;

	private BlockApiCache<Storage<ItemVariant>, Direction> cache;
	private BlockPos cachePos;

	public RedStringContainerStorage(RedStringContainerBlockEntity container, Direction direction) {
		this.container = container;
		this.direction = direction;
	}

	private Storage<ItemVariant> getStorage() {
		Level level = container.getLevel();
		BlockPos pos = container.getBinding();
		if (level.isClientSide || pos == null) {
			return Storage.empty();
		}

		if (!pos.equals(cachePos)) {
			cachePos = pos;
			cache = BlockApiCache.create(ItemStorage.SIDED, ((ServerLevel) level), pos);
		}

		Storage<ItemVariant> storage = cache.find(direction);
		if (storage == null) {
			return Storage.empty();
		}
		return storage;
	}

	@Override
	public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
		return getStorage().insert(resource, maxAmount, transaction);
	}

	@Override
	public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
		return getStorage().extract(resource, maxAmount, transaction);
	}

	@Override
	public Iterator<StorageView<ItemVariant>> iterator() {
		return getStorage().iterator();
	}

	@Override
	public boolean supportsInsertion() {
		return getStorage().supportsInsertion();
	}

	@Override
	public long simulateInsert(ItemVariant resource, long maxAmount, @Nullable TransactionContext transaction) {
		return getStorage().simulateInsert(resource, maxAmount, transaction);
	}

	@Override
	public boolean supportsExtraction() {
		return getStorage().supportsExtraction();
	}

	@Override
	public long simulateExtract(ItemVariant resource, long maxAmount, @Nullable TransactionContext transaction) {
		return getStorage().simulateExtract(resource, maxAmount, transaction);
	}

	@Override
	public @Nullable StorageView<ItemVariant> exactView(TransactionContext transaction, ItemVariant resource) {
		return getStorage().exactView(transaction, resource);
	}

	@Override
	public long getVersion() {
		return getStorage().getVersion();
	}
}
