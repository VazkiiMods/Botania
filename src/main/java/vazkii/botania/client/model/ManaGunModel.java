package vazkii.botania.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import vazkii.botania.common.item.ItemManaGun;

import java.util.List;

public class ManaGunModel implements IBakedModel {

    private final ItemHandler itemHandler = new ItemHandler();

    @Override public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) { return null; }
    @Override public boolean isAmbientOcclusion() { return true; }
    @Override public boolean isGui3d() { return true; }
    @Override public boolean isBuiltInRenderer() { return false; }
    @Override public TextureAtlasSprite getParticleTexture() { return null; }
    @Override public ItemCameraTransforms getItemCameraTransforms() { return ItemCameraTransforms.DEFAULT; }

    @Override
    public ItemOverrideList getOverrides() {
        return itemHandler;
    }

    private class ItemHandler extends ItemOverrideList {
        private ItemHandler() {
            super(ImmutableList.of());
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
            boolean hasClip = ItemManaGun.hasClip(stack);
            if (hasClip) {
                List<ItemStack> lenses = ItemManaGun.getAllLens(stack);
            }
        }
    }
}
