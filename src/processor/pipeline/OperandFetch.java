package processor.pipeline;

import processor.Processor;
import java.util.Scanner;


public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}
	
	public void performOF()
	{
        //Special Handling for "end" instruction
        if(IF_OF_Latch.getInstruction() == -402653184){  // if end instruction

            // Update OF_EX Latch
            OF_EX_Latch.setPC(IF_OF_Latch.getPC());
            OF_EX_Latch.setInstruction(IF_OF_Latch.getInstruction());
            OF_EX_Latch.setControlUnit(containingProcessor.getControlUnit());    
        }

        if(IF_OF_Latch.isOF_enable())		
		{
            if(checkRAW(IF_OF_Latch.getInstruction())){

                // Disable IF-OF stages and enable EX stage
                System.out.println("Found RAW at PC: " + Integer.toString(IF_OF_Latch.getPC()));
                this.containingProcessor.getIFUnit().IF_EnableLatch.setIF_enable(false);
                OF_EX_Latch.setEX_enable(true);
                OF_EX_Latch.setIsNop(true);

                //Increment OF Stall
                containingProcessor.setNumOFStalls(containingProcessor.getNumOFStalls() + 1);

            // debug
                // Scanner input = new Scanner(System.in);
                // System.out.print("Enter an OF integer: ");
                // int number = input.nextInt();

                return;
            } else { // IF Stage should fetch next instruction when data hazard cleared
                //if end don't enable IF Stage
                if( IF_OF_Latch.getInstruction() != -402653184 ){
                    this.containingProcessor.getIFUnit().IF_EnableLatch.setIF_enable(true);
                }
            }
            
            // Set Instruction to Control Unit
			containingProcessor.getControlUnit().setOpCode(IF_OF_Latch.getInstruction());
            
            // Get the instruction and pre process
			int inst = IF_OF_Latch.getInstruction();
			String bin = Integer.toBinaryString(inst);
            if( inst > 0 ){
                bin = String.format("%32s", Integer.toBinaryString(inst)).replace(' ', '0');
            }
            System.out.println("Binary Instruction: " + bin);

			//Calc immx
			String immxStr = bin.substring(15);
			if(immxStr.charAt(0) == '1'){
				immxStr = twosComplement(immxStr);	
				immxStr = "-" + immxStr;
			}
			int immx = Integer.parseInt(immxStr, 2);            

            //Calc branchTarget
            String instStr = bin.substring(15); 
            if( containingProcessor.getControlUnit().isJmp()){// CHECK FOR JMP - 22 bits
                instStr = bin.substring(10);
            }
            
            //If negative, format with '-' sign'
            if(instStr.charAt(0) == '1'){
				instStr = twosComplement(instStr);	
				instStr = "-" + instStr;
			}
            int instInt = Integer.parseInt(instStr, 2);
            int branchTarget = instInt + IF_OF_Latch.getPC();
            OF_EX_Latch.setBranchTarget(branchTarget);
            
            
            // Calc. op1
            int op1Reg = Integer.parseInt(bin.substring(5,10), 2);
            
            //Calc. op2 
            int op2Reg = -1;
            op2Reg = Integer.parseInt(bin.substring(10,15), 2);
            if( containingProcessor.getControlUnit().isSt() ){  //if Store

                // switch op1 and op2, need to calc. rd + imm
                int temp = op1Reg;
                op1Reg = op2Reg;
                op2Reg = temp; 
            }
            
            int op1 = containingProcessor.getRegisterFile().getValue(op1Reg);
            int op2 = containingProcessor.getRegisterFile().getValue(op2Reg);
            
            // ******* Printing to debug
            System.out.println("PC: " + Integer.toString(IF_OF_Latch.getPC()));
            System.out.println("op1Reg: " + op1Reg + " ,op1: " + Integer.toString(op1));
            System.out.println("op2Reg: " + op2Reg + " ,op2: " + Integer.toString(op2));
            System.out.println("offSet: " + instStr);
            System.out.println("branchTarget: " + instInt);
            System.out.println("CU_OPCODE: " + containingProcessor.getControlUnit().getOpCode());

        // debug
			// Scanner input = new Scanner(System.in);
	    	// System.out.print("Enter an OF integer: ");
    		// int number = input.nextInt();

            // Update Latch
            OF_EX_Latch.setPC(IF_OF_Latch.getPC());
            OF_EX_Latch.setInstruction(IF_OF_Latch.getInstruction());
            OF_EX_Latch.setControlUnit(containingProcessor.getControlUnit());
            OF_EX_Latch.setImmx(immx);
            OF_EX_Latch.setOp1(op1);
            OF_EX_Latch.setOp2(op2);

			IF_OF_Latch.setOF_enable(false);
            OF_EX_Latch.setEX_enable(true);
		}
    }
        

    public boolean checkRAW(int inst) {
        try{
            // Get op1 and op2
            String bin = Integer.toBinaryString(inst);
            if( inst > 0 ){
                bin = String.format("%32s", Integer.toBinaryString(inst)).replace(' ', '0');
            }
            
            // Get registers for op1 and op2
            int op1Reg = Integer.parseInt(bin.substring(5,10), 2);
            int op2Reg = -1;
            op2Reg = Integer.parseInt(bin.substring(10,15), 2);
            int opCode = containingProcessor.getOpCode(inst);
            if(opCode % 2 == 1 && opCode < 22){  //Don't have to check op2 for R2I type
                op2Reg = -1; 
            }

            //if load, ignore op2(rd)
            if(opCode == 22){
                op2Reg = -1;
            }            

            // Get instructions from other latches
            int OF_EX_inst, EX_MA_inst, MA_RW_inst;

            OF_EX_inst = OF_EX_Latch.getInstruction();
            EX_MA_inst = containingProcessor.getMAUnit().EX_MA_Latch.getInstruction();
            MA_RW_inst = containingProcessor.getRWUnit().MA_RW_Latch.getInstruction();

            // Instructions till opcode 22 only write back, hence only they contribute to RAW hazards
            int opCode_OFEX = containingProcessor.getOpCode(OF_EX_inst);

            // Checking for OF_EX hazard
            if(opCode_OFEX <= 22 && !containingProcessor.getOFUnit().OF_EX_Latch.getIsNop()) {
                int rdReg = containingProcessor.getRd(OF_EX_inst);
                if(op1Reg == rdReg || op2Reg == rdReg) {
                    System.out.println("**OF_EX** op1Reg: " + Integer.toString(op1Reg) + "\t" + "op2Reg: " + Integer.toString(op2Reg) + "\t" +"rdReg: " + Integer.toString(rdReg));
                    return true;
                }
            }

            // Checking for EX_MA hazard
            if(containingProcessor.getOpCode(EX_MA_inst) <= 22 && !containingProcessor.getEXUnit().EX_MA_Latch.getIsNop()){
                int rdReg = containingProcessor.getRd(EX_MA_inst);
                System.out.println("CHECKING EX_MA** op1Reg: " + Integer.toString(op1Reg) + "\t" + "op2Reg: " + Integer.toString(op2Reg) + "\t" +"rdReg: " + Integer.toString(rdReg));
                if(op1Reg == rdReg || op2Reg == rdReg) {
                    System.out.println("**EX_MA** op1Reg: " + Integer.toString(op1Reg) + "\t" + "op2Reg: " + Integer.toString(op2Reg) + "\t" +"rdReg: " + Integer.toString(rdReg));
                    return true;
                }
            }

            // Checking for MA_RW hazard
            if(containingProcessor.getOpCode(MA_RW_inst) <= 22 && !containingProcessor.getMAUnit().MA_RW_Latch.getIsNop()) {
                int rdReg = containingProcessor.getRd(MA_RW_inst);
                if(op1Reg == rdReg || op2Reg == rdReg) {
                    System.out.println("**MA_RW** op1Reg: " + Integer.toString(op1Reg) + "\t" + "op2Reg: " + Integer.toString(op2Reg) + "\t" +"rdReg: " + Integer.toString(rdReg));
                    return true;
                }
            }

        } catch(StringIndexOutOfBoundsException e){
            return false;
        }
        return false;
    }


	// Print 1's and 2's complement of binary number
    public static char flip(char c) {
        return (c == '0') ? '1' : '0';
    }
	public String twosComplement(String bin) {
        int n = bin.length();
        int i;

        String ones = "", twos = "";
        ones = twos = "";

        // for ones complement flip every bit
        for (i = 0; i < n; i++)
        {
            ones += flip(bin.charAt(i));
        }

        twos = ones;
        for (i = n - 1; i >= 0; i--)
        {
            if (ones.charAt(i) == '1')
            {
                twos = twos.substring(0, i) + '0' + twos.substring(i + 1);
            }
            else
            {
                twos = twos.substring(0, i) + '1' + twos.substring(i + 1);
                break;
            }
        }

        // If No break : all are 1 as in 111 or 11111;
        // in such case, add extra 1 at beginning
        if (i == -1)
        {
            twos = '1' + twos;
        }
        return twos;
    }

}
