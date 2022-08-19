package vazkii.botania.common.integration.buildcraft;

import buildcraft.api.statements.IStatement;
import buildcraft.api.statements.IStatementParameter;
import net.minecraft.util.IIcon;

public abstract class StatementBase implements IStatement {
    protected IIcon icon;

    @Override
    public IIcon getIcon() {
        return icon;
    }

    @Override
    public int maxParameters() {
        return 0;
    }

    @Override
    public int minParameters() {
        return 0;
    }

    @Override
    public IStatementParameter createParameter(int index) {
        return null;
    }

    @Override
    public IStatement rotateLeft() {
        return this;
    }
}
