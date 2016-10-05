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

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
	private boolean skipSound = false;

	public ItemLexicon() {
		super(LibItemNames.LEXICON);
		setMaxStackSize(1);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemStack par1ItemStack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) {
		if(player.isSneaking()) {
			Block block = world.getBlockState(pos).getBlock();

			if(block != null) {
				if(block instanceof ILexiconable) {
					LexiconEntry entry = ((ILexiconable) block).getEntry(world, pos, player, par1ItemStack);
					if(entry != null && isKnowledgeUnlocked(par1ItemStack, entry.getKnowledgeType())) {
						Botania.proxy.setEntryToOpen(entry);
						Botania.proxy.setLexiconStack(par1ItemStack);

						openBook(player, par1ItemStack, world, false);
						return EnumActionResult.SUCCESS;
					}
				} else if(world.isRemote) {
					RayTraceResult mop = new RayTraceResult(new Vec3d(par8, par9, par10), side, pos);
					return Botania.proxy.openWikiPage(world, block, mop) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
				}
			}
		}

		return EnumActionResult.PASS;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(@Nonnull Item item, CreativeTabs tab, List<ItemStack> list) {
		list.add(new ItemStack(item));
		ItemStack creative = new ItemStack(item);
		for(String s : BotaniaAPI.knowledgeTypes.keySet()) {
			KnowledgeType type = BotaniaAPI.knowledgeTypes.get(s);
			unlockKnowledge(creative, type);
		}
		list.add(creative);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer player, List<String> stacks, boolean par4) {
		if(GuiScreen.isShiftKeyDown()) {
			String edition = TextFormatting.GOLD + I18n.format("botaniamisc.edition", getEdition());
			if(!edition.isEmpty())
				stacks.add(edition);

			List<KnowledgeType> typesKnown = new ArrayList<>();
			for(String s : BotaniaAPI.knowledgeTypes.keySet()) {
				KnowledgeType type = BotaniaAPI.knowledgeTypes.get(s);
				if(isKnowledgeUnlocked(par1ItemStack, type))
					typesKnown.add(type);
			}

			String format = typesKnown.size() == 1 ? "botaniamisc.knowledgeTypesSingular" : "botaniamisc.knowledgeTypesPlural";
			addStringToTooltip(I18n.format(format, typesKnown.size()), stacks);

			for(KnowledgeType type : typesKnown)
				addStringToTooltip(" \u2022 " + I18n.format(type.getUnlocalizedName()), stacks);

		} else addStringToTooltip(I18n.format("botaniamisc.shiftinfo"), stacks);
	}

	private void addStringToTooltip(String s, List<String> tooltip) {
		tooltip.add(s.replaceAll("&", "\u00a7"));
	}

	@SideOnly(Side.CLIENT)
	public static String getEdition() {
		String version = LibMisc.BUILD;
		int build = version.contains("GRADLE") ? 0 : Integer.parseInt(version);
		return build == 0 ? I18n.format("botaniamisc.devEdition") : MathHelper.numberToOrdinal(build);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack par1ItemStack, World world, EntityPlayer player, EnumHand hand) {
		String force = getForcedPage(par1ItemStack);
		if(force != null && !force.isEmpty()) {
			LexiconEntry entry = getEntryFromForce(par1ItemStack);
			if(entry != null)
				Botania.proxy.setEntryToOpen(entry);
			else player.addChatMessage(new TextComponentTranslation("botaniamisc.cantOpen").setStyle(new Style().setColor(TextFormatting.RED)));
			setForcedPage(par1ItemStack, "");
		}

		openBook(player, par1ItemStack, world, skipSound);
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

	@Nonnull
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

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelBakery.registerItemVariants(this, new ModelResourceLocation("botania:lexicon_default", "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
