package vazkii.botania.client.render.tile;

import com.google.common.base.Preconditions;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IItemProvider;

public class TEISR extends ItemStackTileEntityRenderer {
    private final IItemProvider item;
    private final Class<? extends TileEntity> clazz;

    public TEISR(IItemProvider item, Class<? extends TileEntity> clazz) {
        this.item = Preconditions.checkNotNull(item);
        this.clazz = clazz;
    }

    @Override
    public void renderByItem(ItemStack stack) {
        if(stack.getItem() == item.asItem()) {
            TileEntityRendererDispatcher.instance.getRenderer(clazz)
                    .render(null, 0,0, 0, 0, -1);
        }
    }
}
