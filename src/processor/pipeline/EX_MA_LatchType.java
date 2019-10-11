package processor.pipeline;

import processor.pipeline.ArithmeticLogicUnit;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	int aluResult;
	int op2;

	ArithmeticLogicUnit alu;
	
	public EX_MA_LatchType()
	{
		MA_enable = false;
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

	public ArithmeticLogicUnit getAlu() {
		return this.alu;
	}

	public void setAlu(ArithmeticLogicUnit alu) {
		this.alu = alu;
	}

}
