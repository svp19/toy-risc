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

		// If not load or store, simply pass aluResult into MA_RW latch
		if(!cu.isLd() && !cu.isSt()) {
			
		} else if(cu.isLd()) {

		} else if(cd.isSt()) {

		}
	}

}
