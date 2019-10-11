package processor.pipeline;
import processor.pipeline.ArithmeticLogicUnit;

public class IF_OF_LatchType {
	
	boolean OF_enable;
	int instruction;

	// Additions after pipelining
	ArithmeticLogicUnit alu;  // ALU has its own CU
	
	public IF_OF_LatchType()
	{
		OF_enable = false;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public ArithmeticLogicUnit getAlu() {
		return this.alu;
	}

	public void setAlu(ArithmeticLogicUnit alu) {
		this.alu = alu;
	}

}
