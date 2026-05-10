package vazkii.botania.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import io.netty.buffer.ByteBuf;

/**
 * A compressed, somewhat lossy representation of a {@link Vec3} as a {@link BlockPos} and some bits of fractional data
 * for each axis. This may not work too well for positions far outside the world, as the vertical limit for encoding a
 * BlockPos is about +/-32767, and the horizontal limit is a good distance beyond the maximum vanilla world border.
 * <p>
 * We are using it anyway as it encodes to 10 bytes for network transfer, rather than the 24 bytes required for
 * losslessly encoding the entire Vec3 to a precision that won't even be relevant to the client.
 */
public record CompressedPosition(BlockPos blockPos, short compressedFractionalPart) {
	private static final StreamCodec<ByteBuf, CompressedPosition> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, CompressedPosition::blockPos,
			ByteBufCodecs.SHORT, CompressedPosition::compressedFractionalPart,
			CompressedPosition::new
	);
	public static final StreamCodec<ByteBuf, Vec3> VEC3_STREAM_CODEC = STREAM_CODEC.map(
			CompressedPosition::decode, CompressedPosition::encode
	);

	private static final int FRAC_BITS = 5;
	private static final int FRAC_MULT = 1 << FRAC_BITS;
	private static final int FRAC_MASK = FRAC_MULT - 1;

	private static final int Y_OFFSET = FRAC_BITS;
	private static final int Z_OFFSET = Y_OFFSET + FRAC_BITS;

	public static CompressedPosition encode(Vec3 pos) {
		int xFrac = (int) (Mth.frac(pos.x) * FRAC_MULT);
		int yFrac = (int) (Mth.frac(pos.y) * FRAC_MULT);
		int zFrac = (int) (Mth.frac(pos.z) * FRAC_MULT);
		short encodedFractionalPart = (short) (xFrac | yFrac << Y_OFFSET | zFrac << Z_OFFSET);

		return new CompressedPosition(BlockPos.containing(pos.x, pos.y, pos.z), encodedFractionalPart);
	}

	public Vec3 decode() {
		double xFrac = (compressedFractionalPart & FRAC_MASK) / (double) FRAC_MULT;
		double yFrac = (compressedFractionalPart >> Y_OFFSET & FRAC_MASK) / (double) FRAC_MULT;
		double zFrac = (compressedFractionalPart >> Z_OFFSET & FRAC_MASK) / (double) FRAC_MULT;

		return new Vec3(blockPos().getX() + xFrac, blockPos().getY() + yFrac, blockPos().getZ() + zFrac);
	}
}
