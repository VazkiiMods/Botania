package vazkii.botania.common.integration.buildcraft;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
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
	public void registerIcons(IIconRegister iconRegister) {
		icon = IconHelper.forName(iconRegister, "triggers/runeAltarCanCraft");
	}

	@Override
	public String getDescription() {
		return StatCollector.translateToLocal(LibTriggerNames.TRIGGER_RUNE_ALTAR_CAN_CRAFT);
	}

	@Override
	public boolean isTriggerActive(TileEntity target, ForgeDirection side, IStatementContainer source, IStatementParameter[] parameters) {
		if(target instanceof TileRuneAltar) return ((TileRuneAltar) target).hasValidRecipe();
		return false;
	}

}
