/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 25, 2014, 2:04:15 PM (GMT)]
 */
package vazkii.botania.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.IRecipeKeyProvider;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.List;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ItemBlockSpecialFlower extends ItemBlockMod {
	private static final Tag<Item> GENERATING = new ItemTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, "generating_special_flowers"));
	private static final Tag<Item> FUNCTIONAL = new ItemTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, "functional_special_flowers"));
	private static final Tag<Item> MISC = new ItemTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, "misc_special_flowers"));

	public ItemBlockSpecialFlower(Block block1, Properties props) {
		super(block1, props);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(@Nonnull ItemStack stack, World world, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flag) {
		if(GENERATING.contains(this)) {
			tooltip.add(new TranslationTextComponent("botania.flowerType.generating").applyTextStyles(TextFormatting.ITALIC, TextFormatting.BLUE));
		} else if(FUNCTIONAL.contains(this)) {
			tooltip.add(new TranslationTextComponent("botania.flowerType.functional").applyTextStyles(TextFormatting.ITALIC, TextFormatting.BLUE));
		} else if(MISC.contains(this)) {
			tooltip.add(new TranslationTextComponent("botania.flowerType.misc").applyTextStyles(TextFormatting.ITALIC, TextFormatting.BLUE));
		}

		if(ConfigHandler.CLIENT.referencesEnabled.get()) {
			String key = getTranslationKey() + ".reference";
			ITextComponent lore = new TranslationTextComponent(key);
			if(!lore.getString().equals(key))
				tooltip.add(lore.applyTextStyles(TextFormatting.ITALIC, TextFormatting.GRAY));
		}
	}
}
