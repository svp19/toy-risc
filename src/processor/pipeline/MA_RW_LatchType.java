package processor.pipeline;

import processor.pipeline.ArithmeticLogicUnit;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	int ldResult;

	ArithmeticLogicUnit alu;
	
	public MA_RW_LatchType()
	{
		RW_enable = false;
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

	public ArithmeticLogicUnit getAlu() {
		return this.alu;
	}

	public void setAlu(ArithmeticLogicUnit alu) {
		this.alu = alu;
	}

}
