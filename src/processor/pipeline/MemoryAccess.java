package processor.pipeline;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.MemoryWriteEvent;
import generic.Simulator;
import generic.Event.EventType;

import java.util.Scanner;
import processor.Clock;
import processor.Processor;

public class MemoryAccess implements Element{
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
		if(EX_MA_Latch.isMA_busy()){
			return;
		}
		// Print Debug
		if(containingProcessor.getDebugMode().charAt(0) != '0') {
			System.out.println("--------MA--------");
		}

		//Special Handling for "end" instruction
        if(EX_MA_Latch.getInstruction() == -402653184){

            // Update MA_RW Latch
            MA_RW_Latch.setPC(EX_MA_Latch.getPC());
            MA_RW_Latch.setControlUnit(EX_MA_Latch.getControlUnit());    
            MA_RW_Latch.setInstruction(EX_MA_Latch.getInstruction());
        }

		// if isNop
		if(EX_MA_Latch.getIsNop()){
			EX_MA_Latch.setIsNop(false);
			MA_RW_Latch.setIsNop(true);

			if(containingProcessor.getDebugMode().charAt(4) != '0') {
				System.out.println("MA got NOP");
            }
		}

		// Start main part of MA
		if(EX_MA_Latch.isMA_enable()) {
			ControlUnit cu = EX_MA_Latch.getControlUnit();
			cu.setOpCode(EX_MA_Latch.getInstruction());

			if(cu.isLd()) {
				int ldResult;
				// ldResult = containingProcessor.getMainMemory().getWord();
				
				Simulator.getEventQueue().addEvent(
					new MemoryReadEvent(
						Clock.getCurrentTime() + Configuration.mainMemoryLatency,
						this,
						containingProcessor.getMainMemory(),
						EX_MA_Latch.getALUResult() // payload
					)
				);

				// Set EX busy, Set RW nop and return
				containingProcessor.getOFUnit().OF_EX_Latch.setEX_busy(true);
				MA_RW_Latch.setIsNop(true);

				//set MA busy
				EX_MA_Latch.setMA_busy(true);
				
				return;

			} else if(cu.isSt()) {
				
				int location = EX_MA_Latch.getALUResult();
				int data = EX_MA_Latch.getOp2();
				if(containingProcessor.getDebugMode().charAt(4) != '0') {
					System.out.println("location: " + Integer.toString(location));
					System.out.println("data: " + Integer.toString(data));
				}

				// Create MemoryWrite Event
				Simulator.getEventQueue().addEvent(
					new MemoryWriteEvent(
						Clock.getCurrentTime() + Configuration.mainMemoryLatency,
						this,
						containingProcessor.getMainMemory(),
						location,
						data
					)
				);

				// Set EX busy, Set RW nop and return
				containingProcessor.getOFUnit().OF_EX_Latch.setEX_busy(true);
				MA_RW_Latch.setIsNop(true);
				
				//set MA busy
				EX_MA_Latch.setMA_busy(true);

				return;
			}
	
			if(containingProcessor.getDebugMode().charAt(4) == '2') {
				Scanner input = new Scanner(System.in);
				System.out.print("Enter an MA integer: ");
				int number = input.nextInt();
			}
			
			//for other instructions

			// Update MA_RW Latch
			MA_RW_Latch.setPC(EX_MA_Latch.getPC());
			MA_RW_Latch.setInstruction(EX_MA_Latch.getInstruction());
			MA_RW_Latch.setALUResult(EX_MA_Latch.getALUResult());

			// Printing debug
			if(containingProcessor.getDebugMode().charAt(4) != '0') {
				System.out.println("ALUResult: " + Integer.toString(EX_MA_Latch.getALUResult()));
				System.out.println("CU_OPCODE: " + cu.getOpCode());
				System.out.println("PC: " + EX_MA_Latch.getPC());
			}
		
			// Update latches
			MA_RW_Latch.setControlUnit(cu);
			
			EX_MA_Latch.setMA_enable(false);
			MA_RW_Latch.setRW_enable(true);

			if(containingProcessor.getDebugMode().charAt(4) == '2') {
				Scanner input = new Scanner(System.in);
				System.out.println("Enter an integer: ");
				int number = input.nextInt();
			}
			
			
			
		}
				
	}

	@Override
	public void handleEvent(Event e){
		
		ControlUnit cu = EX_MA_Latch.getControlUnit();
		cu.setOpCode(EX_MA_Latch.getInstruction());
		
		EX_MA_Latch.setMA_busy(false);

		// set EX busy false
		containingProcessor.getOFUnit().OF_EX_Latch.setEX_busy(false);

		// Memory Read
		if(e.getEventType() == EventType.MemoryResponse){
			MemoryResponseEvent event = (MemoryResponseEvent) e;
			int ldResult = event.getValue();
			
			if(ldResult != -1){
				// Update Latch
				MA_RW_Latch.setLdResult(ldResult);
				
				// Debug Prints
				if(containingProcessor.getDebugMode().charAt(4) != '0') {
					System.out.println("getALUResult: " + Integer.toString(EX_MA_Latch.getALUResult()));
					System.out.println("ldResult: " + Integer.toString(ldResult));
				}	
			}
		}
		
		
		// Update MA_RW Latch
		MA_RW_Latch.setPC(EX_MA_Latch.getPC());
		MA_RW_Latch.setInstruction(EX_MA_Latch.getInstruction());
		MA_RW_Latch.setALUResult(EX_MA_Latch.getALUResult());

		// Printing debug
		if(containingProcessor.getDebugMode().charAt(4) != '0') {
			System.out.println("ALUResult: " + Integer.toString(EX_MA_Latch.getALUResult()));
			System.out.println("CU_OPCODE: " + cu.getOpCode());
			System.out.println("PC: " + EX_MA_Latch.getPC());
		}
	
		// Update latches
		MA_RW_Latch.setControlUnit(cu);
		
		EX_MA_Latch.setMA_enable(false);
		MA_RW_Latch.setRW_enable(true);

		if(containingProcessor.getDebugMode().charAt(4) == '2') {
			Scanner input = new Scanner(System.in);
			System.out.println("Enter an integer: ");
			int number = input.nextInt();
		}
	}

}
