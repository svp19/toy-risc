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
		// Special Handling for END instruction
		try{
			//TODO see why set OpCode bug
			ControlUnit cu = MA_RW_Latch.getControlUnit();
			cu.setOpCode(MA_RW_Latch.getInstruction());
			// if instruction being processed is an end instruction, 
			//  remember to call Simulator.setSimulationComplete(true);
			if( cu.isEnd() ){
				containingProcessor.setIsEnd(true);
			}
		} catch(Exception e){}
		
		if(MA_RW_Latch.isRW_enable() && !MA_RW_Latch.getIsNop()) {

			// Get cu and alu for flags and arithmetic
			//TODO see why set OpCode bug

			ControlUnit cu = MA_RW_Latch.getControlUnit();
			cu.setOpCode(MA_RW_Latch.getInstruction());

			// Get MA_RW Latch results
			int result = MA_RW_Latch.getALUResult(); 
			if( cu.isLd() ){
				result = MA_RW_Latch.getLdResult();
			}

			if(containingProcessor.getDebugMode().charAt(5) != '0') {
				System.out.println("Result: " + Integer.toString(result));
			}

			// Get 'rd' from instruction
			int currentPC = MA_RW_Latch.getPC();
			int inst = MA_RW_Latch.getInstruction();
			String instStr = Integer.toBinaryString(inst);
            if( inst > 0 ){
                instStr = String.format("%32s", Integer.toBinaryString(inst)).replace(' ', '0');
			}
			
			// Process rd
			String rdStr = instStr.substring(10, 15); // R2I-Type (default)
			
			if(cu.isJmp()){ // RI-Type
				rdStr = instStr.substring(5, 10);
			}

			if(cu.isR3()){ // R3-Type
				rdStr = instStr.substring(15, 20);
			}

			int rd = Integer.parseInt(rdStr, 2);

			if(containingProcessor.getDebugMode().charAt(5) != '0') {
				System.out.println("PC: " + Integer.toString(currentPC));
				System.out.println("rd: " + Integer.toString(rd));
				System.out.println("CU_OPCODE" + MA_RW_Latch.getControlUnit().getOpCode());
			}

			// If isWb then write back to register
			if( cu.isWb() ){

				if(containingProcessor.getDebugMode().charAt(5) != '0') {
					System.out.println("WRITE!!!!!!!!!!!!!!!");
				}
				containingProcessor.getRegisterFile().setValue(rd, result);
			}
			
			if(containingProcessor.getDebugMode().charAt(5) == '2') {
				Scanner input = new Scanner(System.in);
				System.out.print("Enter any integer to continue: ");
				int number = input.nextInt();
			}
				
		}
		
		MA_RW_Latch.setRW_enable(false);
		
		if(MA_RW_Latch.getIsNop()){
			MA_RW_Latch.setIsNop(false);

			if(containingProcessor.getDebugMode().charAt(5) != '0') {
				System.out.println("RW got NOP");
			}
		} else {
			// if not end, then enable IF stage
			if(containingProcessor.getOFUnit().IF_OF_Latch.getInstruction() != -402653184){
				IF_EnableLatch.setIF_enable(true);
			}
		}
		
	}

}
