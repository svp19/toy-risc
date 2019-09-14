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
			ldResult = containingProcessor.getMainMemory().getWord(EX_MA_Latch.getALUResult());
			MA_RW_Latch.setLdResult(ldResult);
			System.out.println("getALUResult: " + Integer.toString(EX_MA_Latch.getALUResult()));
			System.out.println("ldResult: " + Integer.toString(ldResult));
		} else if(cu.isSt()) {
			int reg = EX_MA_Latch.getALUResult();
			int data = EX_MA_Latch.getOp2();
			System.out.println("reg: " + Integer.toString(reg));
			System.out.println("data: " + Integer.toString(data));
			containingProcessor.getMainMemory().setWord(reg, data);
		}

		
		// // debug
		// Scanner input = new Scanner(System.in);
		// System.out.print("Enter an MA integer: ");
		// int number = input.nextInt();

		if(EX_MA_Latch.isMA_enable()){ //isBranchTaken is false
			MA_RW_Latch.setRW_enable(true);
		} else {//isBranchTaken is true
			MA_RW_Latch.setRW_enable(false);
		}
		EX_MA_Latch.setMA_enable(false);
		
	}

}
