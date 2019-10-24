package processor.pipeline;
import processor.pipeline.ControlUnit;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	int immx;
	int branchTarget;
	int op1;
	int op2;

	//new
	int PC;
	int instruction;
	ControlUnit controlUnit;
	boolean isNop;
	boolean EX_busy;

	public boolean getIsNop() {
		return this.isNop;
	}

	public void setIsNop(boolean isNop) {
		this.isNop = isNop;
		if(isNop == true){
			this.PC = -1;
			this.instruction = -1;
			this.immx = -1;
			this.branchTarget = -1;
			this.op1 = -1;
			this.op2 = -1;
		}
	}

	public boolean isEX_busy(){
		return this.EX_busy;
	}

	public void setEX_busy(boolean eX_busy){
		this.EX_busy = eX_busy;
	}

	public int getPC() {
		return this.PC;
	}

	public void setPC(int pc) {
		this.PC = pc;
	}

	public int getInstruction() {
		return this.instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public ControlUnit getControlUnit() {
		return this.controlUnit;
	}

	public void setControlUnit(ControlUnit controlUnit) {
		this.controlUnit = controlUnit;
	}
	

	public int getOp1() {
		return this.op1;
	}

	public void setOp1(int op1) {
		this.op1 = op1;
	}

	public int getOp2() {
		return this.op2;
	}

	public void setOp2(int op2) {
		this.op2 = op2;
	}

	public OF_EX_LatchType()
	{
		EX_enable = false;
		isNop = false;
		EX_busy = false;
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
