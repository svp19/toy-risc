package processor.pipeline;

import processor.Processor;
import java.util.Scanner;

public class InstructionFetch {
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performIF()
	{
		if(IF_EnableLatch.isIF_enable())
		{
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			IF_OF_Latch.setInstruction(newInstruction);
			System.out.println("PC: " + Integer.toString(currentPC) + " ,inst: " + Integer.toString(newInstruction));
			

			// Instruction to Control Unit
			containingProcessor.getControlUnit().setOpCode(newInstruction);

			// Link Control unit to ALU
			containingProcessor.getALUUnit().setControlUnit(
				containingProcessor.getControlUnit()
			);
			
			// Check isBranchTaken
			if( EX_IF_Latch.getIsBranchTaken() ){

				// Get PC that was accidentally incremented
				currentPC = EX_IF_Latch.getBranchPC();
				containingProcessor.getRegisterFile().setProgramCounter(
					EX_IF_Latch.getBranchPC()
				);
				System.out.println("BranchPC: " + Integer.toString(currentPC));
				// Update with new instruction after Branch
				currentPC = containingProcessor.getRegisterFile().getProgramCounter();
				newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
				IF_OF_Latch.setInstruction(newInstruction);
				System.out.println("[BRANCH] PC: " + Integer.toString(currentPC) + " ,inst: " + Integer.toString(newInstruction));
			} else {
				containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);	
			}

			// // debug
			// Scanner input = new Scanner(System.in);
	    	// System.out.print("Enter an integer: ");
    		// int number = input.nextInt();

			IF_EnableLatch.setIF_enable(false);
			IF_OF_Latch.setOF_enable(true);
		}
	}

}
