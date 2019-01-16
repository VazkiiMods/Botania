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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.lexicon.LexiconData;

import java.awt.Color;
import java.util.List;

public class SubTileSpectrolus extends SubTileGenerating {

	private static final String TAG_NEXT_COLOR = "nextColor";

	private static final int RANGE = 1;

	int nextColor;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (supertile.getWorld().isRemote)
			return;

		Item wool = Item.getItemFromBlock(Blocks.WOOL);

		List<EntityItem> items = supertile.getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(supertile.getPos().add(-RANGE, -RANGE, -RANGE), supertile.getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
		int slowdown = getSlowdownFactor();

		for(EntityItem item : items) {
			ItemStack stack = item.getItem();

			if(!stack.isEmpty() && stack.getItem() == wool && !item.isDead && item.age >= slowdown) {
				int meta = stack.getItemDamage();
				if(meta == nextColor) {
					mana = Math.min(getMaxMana(), mana + 2400);
					nextColor = nextColor == 15 ? 0 : nextColor + 1;
					sync();

					((WorldServer) supertile.getWorld()).spawnParticle(EnumParticleTypes.ITEM_CRACK, false, item.posX, item.posY, item.posZ, 20, 0.1D, 0.1D, 0.1D, 0.05D, Item.getIdFromItem(stack.getItem()), stack.getItemDamage());
				}

				item.setDead();
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

	@SideOnly(Side.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		super.renderHUD(mc, res);

		ItemStack stack = new ItemStack(Blocks.WOOL, 1, nextColor);
		int color = getColor();

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		if(!stack.isEmpty()) {
			String stackName = stack.getDisplayName();
			int width = 16 + mc.fontRenderer.getStringWidth(stackName) / 2;
			int x = res.getScaledWidth() / 2 - width;
			int y = res.getScaledHeight() / 2 + 30;

			mc.fontRenderer.drawStringWithShadow(stackName, x + 20, y + 5, color);
			RenderHelper.enableGUIStandardItemLighting();
			mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
			RenderHelper.disableStandardItemLighting();
		}

		GlStateManager.disableLighting();
		GlStateManager.disableBlend();
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);
		cmp.setInteger(TAG_NEXT_COLOR, nextColor);
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);
		nextColor = cmp.getInteger(TAG_NEXT_COLOR);
	}

}
