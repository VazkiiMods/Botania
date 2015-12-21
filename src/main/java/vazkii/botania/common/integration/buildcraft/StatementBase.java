package vazkii.botania.common.integration.buildcraft;


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
