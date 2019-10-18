/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 25, 2015, 6:03:28 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.common.advancements.UseItemSuccessTrigger;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.entity.EntityPinkWither;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemPinkinator extends ItemMod {

	public ItemPinkinator(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		int range = 16;
		List<WitherEntity> withers = world.getEntitiesWithinAABB(WitherEntity.class, new AxisAlignedBB(player.posX - range, player.posY - range, player.posZ - range, player.posX + range, player.posY + range, player.posZ + range));
		for(WitherEntity wither : withers)
			if(!world.isRemote && wither.isAlive() && !(wither instanceof EntityPinkWither)) {
				wither.remove();
				EntityPinkWither pink = new EntityPinkWither(world);
				pink.setLocationAndAngles(wither.posX, wither.posY, wither.posZ, wither.rotationYaw, wither.rotationPitch);
				pink.onInitialSpawn(world, world.getDifficultyForLocation(new BlockPos(pink)), SpawnReason.CONVERSION, null, null);
				world.addEntity(pink);
				pink.spawnExplosionParticle();
				pink.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 4F, (1F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
				UseItemSuccessTrigger.INSTANCE.trigger((ServerPlayerEntity) player, stack, (ServerWorld) world, player.posX, player.posY, player.posZ);
				stack.shrink(1);
				return ActionResult.newResult(ActionResultType.SUCCESS, stack);
			}

		return ActionResult.newResult(ActionResultType.PASS, stack);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flags) {
		tooltip.add(new TranslationTextComponent("botaniamisc.pinkinatorDesc").applyTextStyle(TextFormatting.GRAY));
	}

}
