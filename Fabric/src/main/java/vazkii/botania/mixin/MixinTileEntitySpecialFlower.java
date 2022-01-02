package vazkii.botania.mixin;

import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import vazkii.botania.api.block.IFloatingFlower;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;

@Mixin(value = TileEntitySpecialFlower.class, remap = false)
public abstract class MixinTileEntitySpecialFlower extends BlockEntity implements RenderAttachmentBlockEntity {
	@Shadow
	public abstract boolean isFloating();

	@Shadow
	@Final
	private IFloatingFlower floatingData;

	private MixinTileEntitySpecialFlower(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}

	@Nullable
	@Override
	public Object getRenderAttachmentData() {
		if (isFloating()) {
			return floatingData.getIslandType();
		}
		return null;
	}
}
