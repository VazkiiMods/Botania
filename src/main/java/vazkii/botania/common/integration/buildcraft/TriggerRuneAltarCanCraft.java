package vazkii.botania.common.integration.buildcraft;

import buildcraft.api.core.render.ISprite;
import buildcraft.api.statements.IStatementContainer;
import buildcraft.api.statements.IStatementParameter;
import buildcraft.api.statements.ITriggerExternal;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.common.block.tile.TileRuneAltar;
import vazkii.botania.common.lib.LibTriggerNames;

public class TriggerRuneAltarCanCraft extends StatementBase implements ITriggerExternal {

	@Override
	public String getUniqueTag() {
		return "botania:rune_altar_can_craft";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ISprite getSprite() {
		return new TASprite(MiscellaneousIcons.INSTANCE.runeAltarTriggerIcon);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getDescription() {
		return I18n.format(LibTriggerNames.TRIGGER_RUNE_ALTAR_CAN_CRAFT);
	}

	@Override
	public boolean isTriggerActive(TileEntity target, EnumFacing side, IStatementContainer source, IStatementParameter[] parameters) {
		if(target instanceof TileRuneAltar)
		    return ((TileRuneAltar) target).hasValidRecipe();
		return false;
	}

}
