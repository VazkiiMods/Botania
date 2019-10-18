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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ILexicon;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.IElvenItem;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import vazkii.botania.common.Botania;
import vazkii.botania.common.advancements.UseItemSuccessTrigger;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.relic.ItemDice;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;
import vazkii.patchouli.common.base.PatchouliSounds;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;
import vazkii.patchouli.common.network.NetworkHandler;
import vazkii.patchouli.common.network.message.MessageOpenBookGui;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemLexicon extends ItemMod implements ILexicon, IElvenItem {

    public static final String TAG_ELVEN_UNLOCK = "botania:elven_unlock";
	private static final String TAG_KNOWLEDGE_PREFIX = "knowledge.";

	public ItemLexicon(Properties props) {
		super(props);
		addPropertyOverride(new ResourceLocation(LibMisc.MOD_ID, "elven"), (stack, world, living) -> isElvenItem(stack) ? 1 : 0);
	}

	private static Book getBook() {
		return BookRegistry.INSTANCE.books.get(ModItems.lexicon.getRegistryName());
	}

	@Override
	public void fillItemGroup(@Nonnull ItemGroup tab, @Nonnull NonNullList<ItemStack> list) {
		if(isInGroup(tab)) {
			list.add(new ItemStack(this));
			ItemStack creative = new ItemStack(this);
			creative.getOrCreateTag().putBoolean(TAG_ELVEN_UNLOCK, true);
			for(String s : BotaniaAPI.knowledgeTypes.keySet()) {
				KnowledgeType type = BotaniaAPI.knowledgeTypes.get(s);
				unlockKnowledge(creative, type);
			}
			list.add(creative);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);

		if (Screen.hasShiftDown()) {
			Book book = getBook();
			if(book.contents != null)
				tooltip.add(new StringTextComponent(book.contents.getSubtitle()).applyTextStyle(TextFormatting.GRAY));
		} else {
			tooltip.add(new TranslationTextComponent("botaniamisc.shiftinfo"));
		}
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		Book book = getBook();

		if(playerIn instanceof ServerPlayerEntity) {
			NetworkHandler.sendToPlayer(new MessageOpenBookGui(book.resourceLoc.toString()), (ServerPlayerEntity) playerIn);
			SoundEvent sfx = PatchouliSounds.getSound(book.openSound, PatchouliSounds.book_open);
			worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, sfx, SoundCategory.PLAYERS, 1F, (float) (0.7 + Math.random() * 0.4));
		}

		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	@OnlyIn(Dist.CLIENT)
	public static String getEdition() {
		String version = getBook().version;
		try {
			return MathHelper.numberToOrdinal(Integer.parseInt(version));
		} catch (NumberFormatException e) {
			return I18n.format("botaniamisc.devEdition");
		}
	}

	@Override
	public boolean isKnowledgeUnlocked(ItemStack stack, KnowledgeType knowledge) {
		return knowledge.autoUnlock || ItemNBTHelper.getBoolean(stack, TAG_KNOWLEDGE_PREFIX + knowledge.id, false);
	}

	@Override
	public void unlockKnowledge(ItemStack stack, KnowledgeType knowledge) {
		ItemNBTHelper.setBoolean(stack, TAG_KNOWLEDGE_PREFIX + knowledge.id, true);
	}

	public static ITextComponent getTitle(ItemStack stack) {
		ITextComponent title = stack.getDisplayName();

		String akashicTomeNBT = "akashictome:displayName";
		if(stack.hasTag() && stack.getTag().contains(akashicTomeNBT)) {
			title = new StringTextComponent(stack.getTag().getString(akashicTomeNBT));
		}
		
		return title;
	}
	
	@Override
	public boolean isElvenItem(ItemStack stack) {
	    return stack.hasTag() && stack.getTag().getBoolean(TAG_ELVEN_UNLOCK);
	}
}
