package processor.pipeline;
import processor.pipeline.ControlUnit;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	int ldResult;

	//new
	int PC;
	int instruction;
	int ALUResult;
	ControlUnit controlUnit;
	boolean isNop;

	public boolean getIsNop() {
		return this.isNop;
	}

	public void setIsNop(boolean isNop) {
		this.isNop = isNop;
		if(isNop == true){
			this.PC = -1;
			this.instruction = -1;
			this.ALUResult = -1;
		}
	}

	public MA_RW_LatchType()
	{
		RW_enable = false;
		isNop = false;
	}

	public int getPC() {
		return this.PC;
	}

	public void setPC(int PC) {
		this.PC = PC;
	}

	public int getInstruction() {
		return this.instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public int getALUResult() {
		return this.ALUResult;
	}

	public void setALUResult(int aluResult) {
		this.ALUResult = aluResult;
	}

	public ControlUnit getControlUnit() {
		return this.controlUnit;
	}

	public void setControlUnit(ControlUnit controlUnit) {
		this.controlUnit = controlUnit;
	}
	
	public int getLdResult() {
		return this.ldResult;
	}

	public void setLdResult(int ldResult) {
		this.ldResult = ldResult;
	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

}
