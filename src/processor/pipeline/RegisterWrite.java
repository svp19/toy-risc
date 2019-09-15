package processor.pipeline;

import generic.Simulator;
import processor.Processor;
import java.util.Scanner;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performRW()
	{
		if(MA_RW_Latch.isRW_enable())
		{
		//TODO
		// Get cu and alu for flags and arithmetic
			ControlUnit cu = containingProcessor.getControlUnit();
			ArithmeticLogicUnit alu = containingProcessor.getALUUnit();

			// Todo: Optimization => get ALU Result from EX_MA_Latch
			int result = alu.getALUResult(); 
			if( cu.isLd() ){
				result = MA_RW_Latch.getLdResult();
			}
			System.out.println("Result: " + Integer.toString(result));

		//Get 'rd' from instruction. 
			//	Can get instruction from IF_OF_Latch instead
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			int inst = containingProcessor.getMainMemory().getWord(currentPC-1);// Notice -1
			String instStr = Integer.toBinaryString(inst);
            if( inst > 0 ){
                instStr = String.format("%32s", Integer.toBinaryString(inst)).replace(' ', '0');
            }
			
			String rdStr = instStr.substring(10, 15); // R2I-Type (default)
			
			if(cu.isJmp()){ // RI-Type
				rdStr = instStr.substring(5, 10);
			}

			if(cu.isR3()){// R3-Type
				rdStr = instStr.substring(15, 20);
			}
			int rd = Integer.parseInt(rdStr, 2);
			System.out.println("rd: " + Integer.toString(rd));
		// If isWb then write back to register
			if( cu.isWb() ){
				containingProcessor.getRegisterFile().setValue(rd, result);
			}
			
			// if instruction being processed is an end instruction, remember to call Simulator.setSimulationComplete(true);
			if( cu.isEnd() ){
				containingProcessor.setIsEnd(true);
			}

			// // debug
			// Scanner input = new Scanner(System.in);
	    	// System.out.print("Enter any integer to continue: ");
    		// int number = input.nextInt();
				
		}
		
		MA_RW_Latch.setRW_enable(false);
		IF_EnableLatch.setIF_enable(true);
		
	}

}
