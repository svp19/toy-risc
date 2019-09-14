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
		if(IF_OF_Latch.isOF_enable())		
		{
			//TODO
			int inst = IF_OF_Latch.getInstruction();
			String bin = Integer.toBinaryString(inst);
            if( inst > 0 ){
                bin = String.format("%32s", Integer.toBinaryString(inst)).replace(' ', '0');
            }

			//Calc immx;
			String immxStr = bin.substring(15);
			if(immxStr.charAt(0) == 1){
				immxStr = twosComplement(immxStr);	
				immxStr = "-" + immxStr;
			}
			int immx = Integer.parseInt(immxStr, 2);
            OF_EX_Latch.setImmx(immx);
            // System.out.println(bin);
            // System.out.println(immxStr);
            // System.out.println(immx);

            //Calc branchTarget
            String instStr = bin.substring(15); // CHECK FOR JMP?
            // Error: THIS IS NOT SIMPLERISC
            // // right shift by 2
            // instStr += "00";
            if(instStr.charAt(0) == 1){
				instStr = twosComplement(instStr);	
				instStr = "-" + instStr;
			}
            int instInt = Integer.parseInt(instStr, 2);
            System.out.println("instBranchInt: " + instInt);
            int branchTarget = instInt + containingProcessor.getRegisterFile().getProgramCounter() - 1;
            OF_EX_Latch.setBranchTarget(branchTarget);

            System.out.println("Bin: " + bin);
            // Calc. op1
            int op1Reg = Integer.parseInt(bin.substring(5,10), 2);
            System.out.println("op1Reg: " + op1Reg);
            
            int op1 = containingProcessor.getRegisterFile().getValue(op1Reg);
            OF_EX_Latch.setOp1(op1);

            //Calc. op2 
            int op2Reg = -1;
            
            
            if( containingProcessor.getControlUnit().isSt() ){
                op2Reg = Integer.parseInt(bin.substring(15,20), 2);
            } else {
                op2Reg = Integer.parseInt(bin.substring(10,15), 2);
            }
            System.out.println("op2Reg: " + op2Reg);
            int op2 = containingProcessor.getRegisterFile().getValue(op2Reg);
            OF_EX_Latch.setOp2(op2);

            // // debug
			// Scanner input = new Scanner(System.in);
	    	// System.out.print("Enter an OF integer: ");
    		// int number = input.nextInt();

			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}








	// TO BE IN CONTROL UNIT Print 1's and 2's complement of binary number
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
