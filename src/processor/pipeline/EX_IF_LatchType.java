package processor.pipeline;

public class EX_IF_LatchType {
	
	boolean IF_enable;
	boolean isBranchTaken;
	Integer branchPC;

	public EX_IF_LatchType() {
		
	}

	public boolean getIF_enable() {
		return this.IF_enable;
	}

	public void setIF_enable(boolean IF_enable) {
		this.IF_enable = IF_enable;
	}

	public boolean getIsBranchTaken() {
		return this.isBranchTaken;
	}

	public void setIsBranchTaken(boolean isBranchTaken) {
		this.isBranchTaken = isBranchTaken;
	}

	public Integer getBranchPC() {
		return this.branchPC;
	}

	public void setBranchPC(Integer branchPC) {
		this.branchPC = branchPC;
	}

}
