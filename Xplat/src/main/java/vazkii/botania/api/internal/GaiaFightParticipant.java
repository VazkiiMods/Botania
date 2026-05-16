package vazkii.botania.api.internal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.entity.GaiaGuardianEntity;

import java.lang.ref.WeakReference;
import java.util.UUID;

public class GaiaFightParticipant {
	public static final Codec<GaiaFightParticipant> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					UUIDUtil.CODEC.fieldOf("uuid").forGetter(GaiaFightParticipant::getUuid),
					BlockPos.CODEC.fieldOf("sourcePos").forGetter(GaiaFightParticipant::getSourcePos)
			).apply(instance, GaiaFightParticipant::new)
	);

	private final UUID uuid;
	private final BlockPos sourcePos;
	@Nullable
	private WeakReference<GaiaGuardianEntity> gaiaGuardianReference;

	public GaiaFightParticipant(UUID uuid, BlockPos sourcePos) {
		this.uuid = uuid;
		this.sourcePos = sourcePos;
	}

	public UUID getUuid() {
		return uuid;
	}

	public BlockPos getSourcePos() {
		return sourcePos;
	}

	public boolean isInBounds(Mob mob) {
		return mob.position().closerThan(this.sourcePos.getCenter(), GaiaGuardianEntity.ARENA_MOB_RANGE);
	}

	@Nullable
	public GaiaGuardianEntity getGaiaGuardian(ServerLevel serverLevel) {
		if (gaiaGuardianReference != null) {
			GaiaGuardianEntity gaiaGuardianEntity = gaiaGuardianReference.get();
			if (gaiaGuardianEntity != null && gaiaGuardianEntity.isAlive()) {
				return gaiaGuardianEntity;
			}
		}
		if (serverLevel.getEntity(uuid) instanceof GaiaGuardianEntity gaiaGuardian) {
			gaiaGuardianReference = new WeakReference<>(gaiaGuardian);
			return gaiaGuardian.isAlive() ? gaiaGuardian : null;
		}
		return null;
	}
}
