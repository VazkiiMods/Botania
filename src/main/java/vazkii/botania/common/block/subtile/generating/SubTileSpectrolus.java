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
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

import java.awt.Color;
import java.util.List;

public class SubTileSpectrolus extends TileEntityGeneratingFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":spectrolus")
	public static TileEntityType<SubTileSpectrolus> TYPE;

	private static final String TAG_NEXT_COLOR = "nextColor";

	private static final int RANGE = 1;

	private DyeColor nextColor = DyeColor.WHITE;

	public SubTileSpectrolus() {
		super(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isRemote)
			return;

		List<ItemEntity> items = getWorld().getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(getPos().add(-RANGE, -RANGE, -RANGE), getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
		int slowdown = getSlowdownFactor();

		for(ItemEntity item : items) {
			ItemStack stack = item.getItem();

			if(!stack.isEmpty() && item.isAlive() && item.age >= slowdown) {
				Block expected = ModBlocks.getWool(nextColor);

				if(expected.asItem() == stack.getItem()) {
					mana = Math.min(getMaxMana(), mana + 2400);
					nextColor = nextColor == DyeColor.BLACK ? DyeColor.WHITE : DyeColor.values()[nextColor.ordinal() + 1];
					sync();

					((ServerWorld) getWorld()).spawnParticle(new ItemParticleData(ParticleTypes.ITEM, stack), item.posX, item.posY, item.posZ, 20, 0.1D, 0.1D, 0.1D, 0.05D);
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
	public void writeToPacketNBT(CompoundNBT cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putInt(TAG_NEXT_COLOR, nextColor.ordinal());
	}

	@Override
	public void readFromPacketNBT(CompoundNBT cmp) {
		super.readFromPacketNBT(cmp);
		nextColor = DyeColor.byId(cmp.getInt(TAG_NEXT_COLOR));
	}
}
