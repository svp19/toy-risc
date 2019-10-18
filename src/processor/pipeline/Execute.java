package processor.pipeline;

import processor.Processor;
import java.util.Scanner;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performEX()
	{

		if(OF_EX_Latch.getIsNop()){			
			OF_EX_Latch.setIsNop(false);
			EX_MA_Latch.setIsNop(true);

			if(containingProcessor.getDebugMode().charAt(3) != '0') {
				System.out.println("EX Got NOP");
            }
		}
		
		//Special Handling for "end" instruction
        if(OF_EX_Latch.getInstruction() == -402653184){

            // Update EX_MA Latch
            EX_MA_Latch.setPC(OF_EX_Latch.getPC());
            EX_MA_Latch.setControlUnit(OF_EX_Latch.getControlUnit());    
            EX_MA_Latch.setInstruction(OF_EX_Latch.getInstruction());
        }

		if(OF_EX_Latch.isEX_enable() && !OF_EX_Latch.getIsNop()) {  // Check if there's a NOP
			// Get cu and alu for flags and arithmetic
			ControlUnit cu = OF_EX_Latch.getControlUnit();
			cu.setOpCode(OF_EX_Latch.getInstruction());
			ArithmeticLogicUnit alu = containingProcessor.getALUUnit();
			alu.setControlUnit(cu);
			
			// If end, just update latch and return
			if(cu.isEnd()){
				if(containingProcessor.getDebugMode().charAt(3) != '0') {
					System.out.println("The end of the beginning! The middle of the end!");
				}

				//Update EX_MA Latch
				EX_MA_Latch.setPC(OF_EX_Latch.getPC());
				EX_MA_Latch.setInstruction(OF_EX_Latch.getInstruction());
				EX_MA_Latch.setControlUnit(OF_EX_Latch.getControlUnit());
				EX_MA_Latch.setMA_enable(true);
				return;
			}

			// Set branchPC
			EX_IF_Latch.setBranchPC(OF_EX_Latch.getBranchTarget());

			// Get op1 and op2 from OF_EX latch
			int op1 = OF_EX_Latch.getOp1();
			int op2;
			if(cu.isImmediate()) {
				op2 = OF_EX_Latch.getImmx();

				if(containingProcessor.getDebugMode().charAt(3) != '0') {
					System.out.println("Got immx: " + Integer.toString(op2));
				}
			} else {
				op2 = OF_EX_Latch.getOp2();

				if(containingProcessor.getDebugMode().charAt(3) != '0') {
					System.out.println("Got op2: " + Integer.toString(op2));
				}
			}

			// Execute the ALU part and store result in EX_MA latch
			alu.setA(op1);
			alu.setB(op2);

			int aluResult = alu.getALUResult();
			EX_MA_Latch.setALUResult(aluResult);
			if(containingProcessor.getDebugMode().charAt(3) != '0') {
				System.out.println("aluResult: " + Integer.toString(aluResult));
            }

			// if isDiv write mod to Register x31
			if(cu.isDiv()){
				int mod = alu.getMod();
				containingProcessor.getRegisterFile().setValue(31, mod);
			}

			// Compute isBranchTaken from ALU flags and store it in EX_IF latch
			boolean isBrTak = false;
			if(cu.isJmp()) {
				isBrTak = true;
			} else if(cu.isBeq() && alu.getFlag("E")) {
				isBrTak = true;
			} else if(cu.isBgt() && alu.getFlag("GT")) {
				isBrTak = true;
			} else if(cu.isBlt() && alu.getFlag("LT")) {
				isBrTak = true;
			} else if(cu.isBne() && alu.getFlag("NE")) {
				isBrTak = true;
			}
			
			// Printing to debug
			if(containingProcessor.getDebugMode().charAt(3) != '0') {
				System.out.println("op1: " + Integer.toString(op1));
				System.out.println("op2: " + Integer.toString(op2));
				System.out.println("PC: " + Integer.toString(OF_EX_Latch.getPC()));
				System.out.println("CU_OPCODE: " + OF_EX_Latch.getControlUnit().getOpCode());
            }
			
			if(containingProcessor.getDebugMode().charAt(3) == '2') {
				Scanner input = new Scanner(System.in);
				System.out.print("Enter an EX integer: ");
				int number = input.nextInt();
            }


			//Update EX_MA Latch
			// Set op2 in EX_MA Latch in case of store (where op2 is rd, the value to be stored) <= Get op2 from OP_EX Latch
			EX_MA_Latch.setOp2(OF_EX_Latch.getOp2());
			EX_MA_Latch.setPC(OF_EX_Latch.getPC());
			EX_MA_Latch.setInstruction(OF_EX_Latch.getInstruction());
			EX_MA_Latch.setControlUnit(OF_EX_Latch.getControlUnit());
			
			//Regular enable/disable update
			OF_EX_Latch.setEX_enable(false);
			EX_MA_Latch.setMA_enable(true);

			//if isBranchTaken
			// * Introduce nops in IF_OF & OF_EX and enable IF
			// * Predicted Branch Not Taken
			EX_IF_Latch.setIsBranchTaken(isBrTak);
			if(isBrTak){
				containingProcessor.getIFUnit().IF_OF_Latch.setIsNop(true);
				containingProcessor.getOFUnit().OF_EX_Latch.setIsNop(true);
				containingProcessor.getIFUnit().IF_EnableLatch.setIF_enable(true);
			}
		}
	}
}
