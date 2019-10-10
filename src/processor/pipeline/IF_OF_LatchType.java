package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable;
	int instruction;
	
	//new
	int PC;
	boolean isNop;

	public boolean getIsNop() {
		return this.isNop;
	}

	public void setIsNop(boolean isNop) {
		this.isNop = isNop;
		if(isNop == true){
			this.PC = -1;
			this.instruction = -1;
		}
	}
	
	public IF_OF_LatchType()
	{
		OF_enable = false;
		isNop = false;
	}

	public int getPC() {
		return this.PC;
	}

	public void setPC(int PC) {
		this.PC = PC;
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

}
