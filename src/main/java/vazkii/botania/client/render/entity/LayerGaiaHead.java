package vazkii.botania.client.render.entity;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.render.tile.RenderTileGaiaHead;
import vazkii.botania.common.block.tile.TileGaiaHead;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class LayerGaiaHead implements LayerRenderer<EntityPlayer> {

	private final ModelRenderer modelRenderer;

	public LayerGaiaHead(ModelRenderer modelRenderer)
	{
		this.modelRenderer = modelRenderer;
	}

	// Copied from LayerCustomHead, edits noted
	@Override
	public void doRenderLayer(@Nonnull EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		ItemStack itemstack = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

		if (!itemstack.isEmpty() && itemstack.getItem() == ModItems.gaiaHead) // Botania - check for head
		{
			GlStateManager.pushMatrix();

			if (player.isSneaking())
			{
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}

			/*boolean flag = player instanceof EntityVillager || player instanceof EntityZombie && ((EntityZombie)player).isVillager();

            if (player.isChild() && !(player instanceof EntityVillager))
            {
                float f = 2.0F;
                float f1 = 1.4F;
                GlStateManager.translate(0.0F, 0.5F * scale, 0.0F);
                GlStateManager.scale(f1 / f, f1 / f, f1 / f);
                GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
            } Botania - N/A */

			modelRenderer.postRender(0.0625F);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			float f2 = 1.1875F;
			GlStateManager.scale(f2, -f2, -f2);

			// Botania - remove if check for skull and else branch. Check done above.

			/*if (flag)
            {
                GlStateManager.translate(0.0F, 0.0625F, 0.0F);
            } Botania - N/A */

			/*GameProfile gameprofile = null;

            if (itemstack.hasTagCompound())
            {
                NBTTagCompound nbttagcompound = itemstack.getTagCompound();

                if (nbttagcompound.hasKey("SkullOwner", 10))
                {
                    gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
                }
                else if (nbttagcompound.hasKey("SkullOwner", 8))
                {
                    String s = nbttagcompound.getString("SkullOwner");

                    if (!StringUtils.isNullOrEmpty(s))
                    {
                        gameprofile = TileEntitySkull.updateGameprofile(new GameProfile((UUID)null, s));
                        nbttagcompound.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
                    }
                }
            } Botania - Don't do skin stuff */

			// Botania - use gaia TESR
			ShaderHelper.useShader(ShaderHelper.doppleganger, RenderDoppleganger.defaultCallback);
			((RenderTileGaiaHead) (TileEntitySpecialRenderer) TileEntityRendererDispatcher.instance.getRenderer(TileGaiaHead.class))
			.renderSkull(-0.5F, 0.0F, -0.5F, EnumFacing.UP, 180.0F, itemstack.getMetadata(), null, -1, limbSwing);
			ShaderHelper.releaseShader();

			GlStateManager.popMatrix();
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}
