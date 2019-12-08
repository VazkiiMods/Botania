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

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ILexicon;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.IElvenItem;
import vazkii.botania.common.Botania;
import vazkii.botania.common.advancements.UseItemSuccessTrigger;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.relic.ItemDice;
import vazkii.botania.common.lib.LibGuiIDs;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

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
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) {
		if(player.isSneaking()) {
			Block block = world.getBlockState(pos).getBlock();

			if(block != null) {
				if(block instanceof ILexiconable) {
					ItemStack stack = player.getHeldItem(hand);
					LexiconEntry entry = ((ILexiconable) block).getEntry(world, pos, player, stack);
					if(entry != null && isKnowledgeUnlocked(stack, entry.getKnowledgeType())) {
						Botania.proxy.setEntryToOpen(entry);
						Botania.proxy.setLexiconStack(stack);

						openBook(player, stack, world, false);
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
	public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> list) {
		if(isInCreativeTab(tab)) {
			list.add(new ItemStack(this));
			ItemStack creative = new ItemStack(this);
			for(String s : BotaniaAPI.knowledgeTypes.keySet()) {
				KnowledgeType type = BotaniaAPI.knowledgeTypes.get(s);
				unlockKnowledge(creative, type);
			}
			list.add(creative);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack par1ItemStack, World world, List<String> stacks, ITooltipFlag flags) {
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
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		String force = getForcedPage(stack);
		if(force != null && !force.isEmpty()) {
			LexiconEntry entry = getEntryFromForce(stack);
			if(entry != null)
				Botania.proxy.setEntryToOpen(entry);
			else player.sendMessage(new TextComponentTranslation("botaniamisc.cantOpen").setStyle(new Style().setColor(TextFormatting.RED)));
			setForcedPage(stack, "");
		}

		openBook(player, stack, world, skipSound);
		skipSound = false;

		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
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
		player.openGui(Botania.instance, LibGuiIDs.LEXICON, world, 0, 0, 0);
		if(!world.isRemote) {
			if(!skipSound)
				world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.lexiconOpen, SoundCategory.PLAYERS, 0.5F, 1F);
			UseItemSuccessTrigger.INSTANCE.trigger((EntityPlayerMP) player, stack, (WorldServer) world, player.posX, player.posY, player.posZ);
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		int ticks = getQueueTicks(stack);
		if(ticks > 0 && entity instanceof EntityPlayer) {
			skipSound = ticks < 5;
			if(ticks == 1) {
				if(selected)
					onItemRightClick(world, (EntityPlayer) entity, EnumHand.MAIN_HAND);
				else if(stack == ((EntityPlayer) entity).getHeldItemOffhand())
					onItemRightClick(world, (EntityPlayer) entity, EnumHand.OFF_HAND);
			}

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

	public static String getTitle(ItemStack stack) {
		String title = ModItems.lexicon.getItemStackDisplayName(ItemStack.EMPTY);
		if(!stack.isEmpty())
			title = stack.getDisplayName();
		
		String akashicTomeNBT = "akashictome:displayName";
		title = ItemNBTHelper.getString(stack, akashicTomeNBT, title);
		
		return title;
	}
	
	@Override
	public boolean isElvenItem(ItemStack stack) {
		return isKnowledgeUnlocked(stack, BotaniaAPI.elvenKnowledge);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		// Load and bake the 2D models
		ModelBakery.registerItemVariants(this,
				new ModelResourceLocation("botania:lexicon_default", "inventory"),
				new ModelResourceLocation("botania:lexicon_elven", "inventory"));

		ModelResourceLocation default3dPath = new ModelResourceLocation("botania:lexicon_3d_default", "inventory");
		ModelResourceLocation elven3dPath = new ModelResourceLocation("botania:lexicon_3d_elven", "inventory");

		// smart model will dispatch between 2d/3d appropriately, see LexiconModel
		ModelLoader.setCustomMeshDefinition(this, stack -> isElvenItem(stack) ? elven3dPath : default3dPath);
	}

}
