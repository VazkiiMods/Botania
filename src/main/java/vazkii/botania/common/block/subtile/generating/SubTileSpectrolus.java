/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 30, 2015, 1:37:26 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Particles;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.WorldServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lexicon.LexiconData;

import java.awt.Color;
import java.util.List;

public class SubTileSpectrolus extends SubTileGenerating {

	private static final String TAG_NEXT_COLOR = "nextColor";

	private static final int RANGE = 1;

	private EnumDyeColor nextColor = EnumDyeColor.WHITE;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (supertile.getWorld().isRemote)
			return;

		List<EntityItem> items = supertile.getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(supertile.getPos().add(-RANGE, -RANGE, -RANGE), supertile.getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
		int slowdown = getSlowdownFactor();

		for(EntityItem item : items) {
			ItemStack stack = item.getItem();

			if(!stack.isEmpty() && item.isAlive() && item.age >= slowdown) {
				Block expected = ModBlocks.getWool(nextColor);

				if(expected.asItem() == stack.getItem()) {
					mana = Math.min(getMaxMana(), mana + 2400);
					nextColor = nextColor == EnumDyeColor.BLACK ? EnumDyeColor.WHITE : EnumDyeColor.values()[nextColor.ordinal() + 1];
					sync();

					((WorldServer) supertile.getWorld()).spawnParticle(new ItemParticleData(Particles.ITEM, stack), item.posX, item.posY, item.posZ, 20, 0.1D, 0.1D, 0.1D, 0.05D);
				}

				item.remove();
			}
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), RANGE);
	}

	@Override
	public int getMaxMana() {
		return 16000;
	}

	@Override
	public int getColor() {
		return Color.HSBtoRGB(ticksExisted / 100F, 1F, 1F);
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.spectrolus;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(Minecraft mc) {
		super.renderHUD(mc);

		ItemStack stack = new ItemStack(ModBlocks.getWool(nextColor));
		int color = getColor();

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		if(!stack.isEmpty()) {
			ITextComponent stackName = stack.getDisplayName();
			int width = 16 + mc.fontRenderer.getStringWidth(stackName.getString()) / 2;
			int x = mc.mainWindow.getScaledWidth() / 2 - width;
			int y = mc.mainWindow.getScaledHeight() / 2 + 30;

			mc.fontRenderer.drawStringWithShadow(stackName.getFormattedText(), x + 20, y + 5, color);
			RenderHelper.enableGUIStandardItemLighting();
			mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, x, y);
			RenderHelper.disableStandardItemLighting();
		}

		GlStateManager.disableLighting();
		GlStateManager.disableBlend();
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putInt(TAG_NEXT_COLOR, nextColor.ordinal());
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);
		nextColor = EnumDyeColor.byId(cmp.getInt(TAG_NEXT_COLOR));
	}
}
