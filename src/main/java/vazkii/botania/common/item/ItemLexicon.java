/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 14, 2014, 5:53:00 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ILexicon;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.IElvenItem;
import vazkii.botania.api.sound.BotaniaSoundEvents;
import vazkii.botania.common.Botania;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.relic.ItemDice;
import vazkii.botania.common.lib.LibGuiIDs;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

public class ItemLexicon extends ItemMod implements ILexicon, IElvenItem {

	private static final String TAG_KNOWLEDGE_PREFIX = "knowledge.";
	private static final String TAG_FORCED_MESSAGE = "forcedMessage";
	private static final String TAG_QUEUE_TICKS = "queueTicks";
	boolean skipSound = false;

	public ItemLexicon() {
		super();
		setMaxStackSize(1);
		setUnlocalizedName(LibItemNames.LEXICON);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) {
		if(par2EntityPlayer.isSneaking()) {
			Block block = par3World.getBlockState(pos).getBlock();

			if(block != null) {
				if(block instanceof ILexiconable) {
					LexiconEntry entry = ((ILexiconable) block).getEntry(par3World, pos, par2EntityPlayer, par1ItemStack);
					if(entry != null && isKnowledgeUnlocked(par1ItemStack, entry.getKnowledgeType())) {
						Botania.proxy.setEntryToOpen(entry);
						Botania.proxy.setLexiconStack(par1ItemStack);

						openBook(par2EntityPlayer, par1ItemStack, par3World, false);
						return EnumActionResult.SUCCESS;
					}
				} else if(par3World.isRemote) {
					RayTraceResult mop = new RayTraceResult(new Vec3d(par8, par9, par10), side, pos);
					return Botania.proxy.openWikiPage(par3World, block, mop) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
				}
			}
		}

		return EnumActionResult.PASS;
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
		list.add(new ItemStack(item));
		ItemStack creative = new ItemStack(item);
		for(String s : BotaniaAPI.knowledgeTypes.keySet()) {
			KnowledgeType type = BotaniaAPI.knowledgeTypes.get(s);
			unlockKnowledge(creative, type);
		}
		list.add(creative);
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) {
		if(GuiScreen.isShiftKeyDown()) {
			String edition = TextFormatting.GOLD + String.format(I18n.translateToLocal("botaniamisc.edition"), getEdition());
			if(!edition.isEmpty())
				par3List.add(edition);

			List<KnowledgeType> typesKnown = new ArrayList<>();
			for(String s : BotaniaAPI.knowledgeTypes.keySet()) {
				KnowledgeType type = BotaniaAPI.knowledgeTypes.get(s);
				if(isKnowledgeUnlocked(par1ItemStack, type))
					typesKnown.add(type);
			}

			String format = typesKnown.size() == 1 ? "botaniamisc.knowledgeTypesSingular" : "botaniamisc.knowledgeTypesPlural";
			addStringToTooltip(String.format(I18n.translateToLocal(format), typesKnown.size()), par3List);

			for(KnowledgeType type : typesKnown)
				addStringToTooltip(" \u2022 " + I18n.translateToLocal(type.getUnlocalizedName()), par3List);

		} else addStringToTooltip(I18n.translateToLocal("botaniamisc.shiftinfo"), par3List);
	}

	private void addStringToTooltip(String s, List<String> tooltip) {
		tooltip.add(s.replaceAll("&", "\u00a7"));
	}

	public static String getEdition() {
		String version = LibMisc.BUILD;
		int build = version.contains("GRADLE") ? 0 : Integer.parseInt(version.replace("[^\\d]", ""));
		return build == 0 ? I18n.translateToLocal("botaniamisc.devEdition") : MathHelper.numberToOrdinal(build);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, EnumHand hand) {
		String force = getForcedPage(par1ItemStack);
		if(force != null && !force.isEmpty()) {
			LexiconEntry entry = getEntryFromForce(par1ItemStack);
			if(entry != null)
				Botania.proxy.setEntryToOpen(entry);
			else par3EntityPlayer.addChatMessage(new TextComponentTranslation("botaniamisc.cantOpen").setChatStyle(new Style().setColor(TextFormatting.RED)));
			setForcedPage(par1ItemStack, "");
		}

		openBook(par3EntityPlayer, par1ItemStack, par2World, skipSound);
		skipSound = false;

		return ActionResult.newResult(EnumActionResult.SUCCESS, par1ItemStack);
	}

	public static void openBook(EntityPlayer player, ItemStack stack, World world, boolean skipSound) {
		ILexicon l = (ILexicon) stack.getItem();

		Botania.proxy.setToTutorialIfFirstLaunch();

		if(!l.isKnowledgeUnlocked(stack, BotaniaAPI.relicKnowledge) && l.isKnowledgeUnlocked(stack, BotaniaAPI.elvenKnowledge))
			for(ItemStack rstack : ItemDice.relicStacks) {
				Item item = rstack.getItem();
				if(PlayerHelper.hasItem(player, s -> s != null && s.getItem() == item)) {
					l.unlockKnowledge(stack, BotaniaAPI.relicKnowledge);
					break;
				}
			}

		Botania.proxy.setLexiconStack(stack);
		player.addStat(ModAchievements.lexiconUse, 1);
		player.openGui(Botania.instance, LibGuiIDs.LEXICON, world, 0, 0, 0);
		if(!world.isRemote && !skipSound)
			world.playSound(null, player.posX, player.posY, player.posZ, BotaniaSoundEvents.lexiconOpen, SoundCategory.PLAYERS, 0.5F, 1F);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int idk, boolean something) {
		int ticks = getQueueTicks(stack);
		if(ticks > 0 && entity instanceof EntityPlayer) {
			skipSound = ticks < 5;
			if(ticks == 1)
				onItemRightClick(stack, world, (EntityPlayer) entity, EnumHand.MAIN_HAND);

			setQueueTicks(stack, ticks - 1);
		}
	}

	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.UNCOMMON;
	}

	@Override
	public boolean isKnowledgeUnlocked(ItemStack stack, KnowledgeType knowledge) {
		return knowledge.autoUnlock || ItemNBTHelper.getBoolean(stack, TAG_KNOWLEDGE_PREFIX + knowledge.id, false);
	}

	@Override
	public void unlockKnowledge(ItemStack stack, KnowledgeType knowledge) {
		ItemNBTHelper.setBoolean(stack, TAG_KNOWLEDGE_PREFIX + knowledge.id, true);
	}

	public static void setForcedPage(ItemStack stack, String forced) {
		ItemNBTHelper.setString(stack, TAG_FORCED_MESSAGE, forced);
	}

	public static String getForcedPage(ItemStack stack) {
		return ItemNBTHelper.getString(stack, TAG_FORCED_MESSAGE, "");
	}

	private static LexiconEntry getEntryFromForce(ItemStack stack) {
		String force = getForcedPage(stack);

		for(LexiconEntry entry : BotaniaAPI.getAllEntries())
			if(entry.getUnlocalizedName().equals(force))
				if(entry != null && ((ItemLexicon) stack.getItem()).isKnowledgeUnlocked(stack, entry.getKnowledgeType()))
					return entry;

		return null;
	}

	public static int getQueueTicks(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_QUEUE_TICKS, 0);
	}

	public static void setQueueTicks(ItemStack stack, int ticks) {
		ItemNBTHelper.setInt(stack, TAG_QUEUE_TICKS, ticks);
	}

	@Override
	public boolean isElvenItem(ItemStack stack) {
		return isKnowledgeUnlocked(stack, BotaniaAPI.elvenKnowledge);
	}

}
