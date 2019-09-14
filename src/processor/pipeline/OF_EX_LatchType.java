package processor.pipeline;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	int immx;
	int branchTarget;

	public OF_EX_LatchType()
	{
		EX_enable = false;
	}

	public boolean isEX_enable() {
		return this.EX_enable;
	}

	public boolean getEX_enable() {
		return this.EX_enable;
	}

	public void setEX_enable(boolean EX_enable) {
		this.EX_enable = EX_enable;
	}

	public int getImmx() {
		return this.immx;
	}

	public void setImmx(int immx) {
		this.immx = immx;
	}

	public int getBranchTarget() {
		return this.branchTarget;
	}

	public void setBranchTarget(int branchTarget) {
		this.branchTarget = branchTarget;
	}
	
}
