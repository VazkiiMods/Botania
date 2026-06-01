/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.internal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.attachment.DataHolderId;
import vazkii.botania.common.entity.GaiaGuardianEntity;

import java.lang.ref.WeakReference;
import java.util.UUID;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class GaiaFightParticipant {
	public static final Codec<GaiaFightParticipant> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					UUIDUtil.CODEC.fieldOf("uuid").forGetter(GaiaFightParticipant::getUuid),
					BlockPos.CODEC.fieldOf("sourcePos").forGetter(GaiaFightParticipant::getSourcePos)
			).apply(instance, GaiaFightParticipant::new)
	);
	public static final net.minecraft.resources.ResourceLocation ID = botaniaRL("gaia_fight_participant");
	public static final DataHolderId<GaiaFightParticipant> HOLDER = new DataHolderId<>(ID, CODEC);

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

	public boolean isGaiaGuardianAlive(ServerLevel serverLevel) {
		if (gaiaGuardianReference != null) {
			GaiaGuardianEntity gaiaGuardianEntity = gaiaGuardianReference.get();
			if (gaiaGuardianEntity != null) {
				return gaiaGuardianEntity.isAlive();
			}
		}
		if (serverLevel.getEntity(uuid) instanceof GaiaGuardianEntity gaiaGuardian) {
			gaiaGuardianReference = new WeakReference<>(gaiaGuardian);
			return gaiaGuardian.isAlive();
		}
		return false;
	}
}
