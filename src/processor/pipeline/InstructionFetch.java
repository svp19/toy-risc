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
			// Check isBranchTaken
			if( EX_IF_Latch.getIsBranchTaken() ){

				// Update currentPC if branch taken
				currentPC = EX_IF_Latch.getBranchPC();
				containingProcessor.getRegisterFile().setProgramCounter(
					EX_IF_Latch.getBranchPC()
				);
				System.out.println("[BRANCH] to " + Integer.toString(EX_IF_Latch.getBranchPC()));

				//Reset isBranchTaken
				EX_IF_Latch.setIsBranchTaken(false);

				//Increment numBranchTaken
				containingProcessor.setNumBranchTaken(containingProcessor.getNumBranchTaken() + 1);
				
			}
			
			// Fetch Instruction, For every new inst it fetches, numIns++, numCycles++;
			int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);

			// System.out.println("PC: " + Integer.toString(currentPC) + " ,inst: " + Integer.toString(newInstruction));
			containingProcessor.setNumIns(containingProcessor.getNumIns() + 1);
			
			//TODO IN EXECUTE => Link Control unit to ALU
			
			// Update Latch
			IF_OF_Latch.setInstruction(newInstruction);
			IF_OF_Latch.setPC(currentPC);

			// Check isBranchTaken
			containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);	
			

			// debug
			// Scanner input = new Scanner(System.in);
			System.out.println("Fetched inst: " + Integer.toString(newInstruction));
			System.out.println("PC: " + Integer.toString(currentPC));
	    	System.out.print("Enter an integer: ");
    		// int number = input.nextInt();

			IF_EnableLatch.setIF_enable(false);
			IF_OF_Latch.setOF_enable(true);

			
			if( newInstruction == -402653184 ){ // end instruction
				IF_OF_Latch.setOF_enable(false);
			}
		}
	}

}
