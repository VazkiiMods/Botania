package vazkii.botania.common.integration.buildcraft;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.client.core.handler.RenderEventHandler;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.tile.TileRuneAltar;
import vazkii.botania.common.lib.LibTriggerNames;
import buildcraft.api.statements.IStatementContainer;
import buildcraft.api.statements.IStatementParameter;
import buildcraft.api.statements.ITriggerExternal;

public class TriggerRuneAltarCanCraft extends StatementBase implements ITriggerExternal {

	@Override
	public String getUniqueTag() {
		return "botania:runeAltarCanCraft";
	}

	@Override
	public TextureAtlasSprite getGuiSprite() {
		return RenderEventHandler.INSTANCE.runeAltarTriggerIcon;
	}

	@Override
	public String getDescription() {
		return StatCollector.translateToLocal(LibTriggerNames.TRIGGER_RUNE_ALTAR_CAN_CRAFT);
	}

	@Override
	public boolean isTriggerActive(TileEntity target, EnumFacing side, IStatementContainer source, IStatementParameter[] parameters) {
		if(target instanceof TileRuneAltar) return ((TileRuneAltar) target).hasValidRecipe();
		return false;
	}

}
