package processor.pipeline;
import processor.pipeline.ControlUnit;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	int aluResult;
	int op2;

	//new
	int PC;
	int instruction;
	ControlUnit controlUnit;
	boolean isNop;
	boolean isControlNop;

	public boolean getIsNop() {
		return this.isNop;
	}

	public boolean getIsControlNop() {
		return this.isControlNop;
	}

	public void setIsControlNop(boolean isControlNop) {
		this.isControlNop = isControlNop;
	}

	public void setIsNop(boolean isNop) {
		this.isNop = isNop;
		if(isNop == true){
			this.PC = -1;
			this.instruction = -1;
			this.op2 = -1;
			this.aluResult = -1;
			// controlUnit.setOpCode(-1);
		}
	}

	public EX_MA_LatchType()
	{
		MA_enable = false;
		isNop = false;
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

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

	public int getALUResult() {
		return this.aluResult;
	}

	public void setALUResult(int aluResult) {
		this.aluResult = aluResult;
	}

	public int getOp2() {
		return this.op2;
	}

	public void setOp2(int op2) {
		this.op2 = op2;
	}

}
