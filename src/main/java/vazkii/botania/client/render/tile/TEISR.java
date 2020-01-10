package vazkii.botania.client.render.tile;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IItemProvider;

public class TEISR extends ItemStackTileEntityRenderer {
    private final IItemProvider item;
    private final TileEntity dummy;

    public TEISR(IItemProvider item, TileEntityType<?> type) {
        this.item = Preconditions.checkNotNull(item);
        this.dummy = type.create();
    }

    @Override
    public void render(ItemStack stack, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
        if(stack.getItem() == item.asItem()) {
            TileEntityRendererDispatcher.instance.getRenderer(dummy)
                    .render(null, 0, ms, buffers, light, overlay);
        }
    }
}
