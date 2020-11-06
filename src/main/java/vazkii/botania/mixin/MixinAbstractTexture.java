package vazkii.botania.mixin;

import net.minecraft.client.texture.AbstractTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import vazkii.botania.client.core.ExtendedTexture;

@Mixin(AbstractTexture.class)
public abstract class MixinAbstractTexture implements ExtendedTexture {
	@Shadow protected boolean bilinear;

	@Shadow protected boolean mipmap;

	@Shadow public abstract void setFilter(boolean bilinear, boolean mipmap);

	@Unique
	private boolean lastBilinear;

	@Unique
	private boolean lastMipmap;

	@Override
	public void setFilterSave(boolean bilinear, boolean mipmap) {
		this.lastBilinear = this.bilinear;
		this.lastMipmap = this.mipmap;
		setFilter(bilinear, mipmap);
	}

	@Override
	public void restoreLastFilter() {
		setFilter(this.lastBilinear, this.lastMipmap);
	}
}
