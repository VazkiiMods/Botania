/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 25, 2014, 6:11:10 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.ICreativeManaProvider;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.item.ModItems;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class HUDHandler {

	private static final ResourceLocation manaBar = new ResourceLocation(LibResources.GUI_MANA_HUD);
	private static final RenderItem itemRender = new RenderItem();

	@SubscribeEvent
	public void onDrawScreen(RenderGameOverlayEvent.Post event) {
		if(event.type == ElementType.ALL) {
			Minecraft mc = Minecraft.getMinecraft();
			MovingObjectPosition pos = mc.objectMouseOver;
			if(pos != null && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() == ModItems.twigWand) {
				Block block = mc.theWorld.getBlock(pos.blockX, pos.blockY, pos.blockZ);
				if(block instanceof IWandHUD)
					((IWandHUD) block).renderHUD(mc, event.resolution, mc.theWorld, pos.blockX, pos.blockY, pos.blockZ);
			}

			else if(pos != null && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() == ModItems.lexicon) {
				Block block = mc.theWorld.getBlock(pos.blockX, pos.blockY, pos.blockZ);
				if(block instanceof ILexiconable) {
					LexiconEntry entry = ((ILexiconable) block).getEntry(mc.theWorld, pos.blockX, pos.blockY, pos.blockZ, mc.thePlayer, mc.thePlayer.getCurrentEquippedItem());
					if(entry != null)
						drawLexiconGUI(entry, event.resolution);
				}
			}
		} else if(event.type == ElementType.EXPERIENCE) {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			int totalMana = 0;
			int totalMaxMana = 0;
			boolean anyRequest = false;
			boolean creative = false;

			IInventory mainInv = player.inventory;
			IInventory baublesInv = PlayerHandler.getPlayerBaubles(player);

			int invSize = mainInv.getSizeInventory();
			int size = invSize;
			if(baublesInv != null)
				size += baublesInv.getSizeInventory();

			for(int i = 0; i < size; i++) {
				boolean useBaubles = i >= invSize;
				IInventory inv = useBaubles ? baublesInv : mainInv;
				ItemStack stack = inv.getStackInSlot(i - (useBaubles ? invSize : 0));

				if(stack != null) {
					Item item = stack.getItem();
					if(item instanceof IManaUsingItem)
						anyRequest = anyRequest || ((IManaUsingItem) item).usesMana(stack);

					if(item instanceof IManaItem) {
						if(!((IManaItem) item).isNoExport(stack)) {
							totalMana += ((IManaItem) item).getMana(stack);
							totalMaxMana += ((IManaItem) item).getMaxMana(stack);
						}
					}

					if(item instanceof ICreativeManaProvider && ((ICreativeManaProvider) item).isCreative(stack))
						creative = true;
				}
			}

			if(anyRequest)
				renderManaInvBar(event.resolution, creative, totalMana, totalMaxMana);
		}
	}

	private void renderManaInvBar(ScaledResolution res, boolean hasCreative, int totalMana, int totalMaxMana) {
		int width = 182;
		int x = res.getScaledWidth() / 2 - width / 2;
		int y = res.getScaledHeight() - 29;

		if(!hasCreative) {
			if(totalMaxMana == 0)
				width = 0;
			else width *= (double) totalMana / (double) totalMaxMana;
		}

		if(width == 0) {
			if(totalMana > 0)
				width = 1;
			else return;
		}

		Minecraft mc = Minecraft.getMinecraft();

		Color color = new Color(Color.HSBtoRGB(0.55F, (float) Math.min(1F, Math.sin(System.currentTimeMillis() / 200D) * 0.5 + 1F), 1F));
		GL11.glColor4ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(), (byte) (255 - color.getRed()));
		mc.renderEngine.bindTexture(manaBar);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderHelper.drawTexturedModalRect(x, y, 0, 0, 251, width, 5);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void drawLexiconGUI(LexiconEntry entry, ScaledResolution res) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Minecraft mc = Minecraft.getMinecraft();
		int x = res.getScaledWidth() / 2  + 8;
		int y = res.getScaledHeight() / 2 - 4;

		int color = 0x6600FF00;

		mc.fontRenderer.drawStringWithShadow(StatCollector.translateToLocal(entry.getUnlocalizedName()), x + 18, y, color);
		mc.fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("botaniamisc.shiftToRead"), x + 18, y + 11, color);
		itemRender.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack(ModItems.lexicon), x, y);
		GL11.glDisable(GL11.GL_LIGHTING);
		mc.fontRenderer.drawStringWithShadow("?", x + 10, y + 8, 0xFFFFFF);

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1F, 1F, 1F, 1F);
	}

	public static void drawSimpleManaHUD(int color, int mana, int maxMana, String name, ScaledResolution res) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Minecraft mc = Minecraft.getMinecraft();
		int x = res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(name) / 2;
		int y = res.getScaledHeight() / 2 + 10;

		mc.fontRenderer.drawStringWithShadow(name, x, y, color);

		x = res.getScaledWidth() / 2 - 51;
		y += 10;

		renderManaBar(x, y, color, 0.5F, mana, maxMana);

		if(mana < 0) {
			String text = StatCollector.translateToLocal("botaniamisc.statusUnknown");
			x = res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(text) / 2;
			y -= 1;
			mc.fontRenderer.drawStringWithShadow(text, x, y, color);
		}

		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void renderManaBar(int x, int y, int color, float alpha, int mana, int maxMana) {
		Minecraft mc = Minecraft.getMinecraft();

		GL11.glColor4f(1F, 1F, 1F, alpha);
		mc.renderEngine.bindTexture(manaBar);
		RenderHelper.drawTexturedModalRect(x, y, 0, 0, 0, 102, 5);

		int manaPercentage = Math.max(0, (int) ((double) mana / (double) maxMana * 100));

		if(manaPercentage == 0 && mana > 0)
			manaPercentage = 1;

		RenderHelper.drawTexturedModalRect(x + 1 + manaPercentage, y + 1, 0, 0, 5, 100 - manaPercentage, 3);

		Color color_ = new Color(color);
		GL11.glColor4ub((byte) color_.getRed(), (byte) color_.getGreen(),(byte) color_.getBlue(), (byte) (255F * alpha));
		RenderHelper.drawTexturedModalRect(x + 1, y + 1, 0, 0, 5, manaPercentage, 3);
	}
}
