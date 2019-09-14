package processor.pipeline;

import processor.Processor;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		//TODO

		ControlUnit cu = containingProcessor.getControlUnit();

		if(cu.isLd()) {
			int ldResult;

			ldResult = containingProcessor.getRegisterFile().getValue(EX_MA_Latch.getALUResult());
			MA_RW_Latch.setLdResult(ldResult);
		} else if(cu.isSt()) {
			int reg = EX_MA_Latch.getALUResult();
			int data = EX_MA_Latch.getOp2();
			
			containingProcessor.getRegisterFile().setValue(reg, data);
		}
	}

}
