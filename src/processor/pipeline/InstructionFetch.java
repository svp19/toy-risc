package processor.pipeline;

import processor.Clock;
import processor.Processor;
import java.util.Scanner;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.Simulator;
import generic.Event.EventType;

public class InstructionFetch implements Element{
	
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
		// Print Debug
		if(containingProcessor.getDebugMode().charAt(0) != '0') {
			System.out.println("--------IF--------");
		}

		if(IF_EnableLatch.isIF_enable())
		{
			// Discrete Event Simulation - 
			// => MemoryRead [Instruction]
			if(IF_EnableLatch.isIF_busy()) {
				// TODO introduce Nop in cycle
				// if( !IF_OF_Latch.isOF_busy() ){
				// 	IF_OF_Latch.setIsNop(true);
				// }
				return;
			}

			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			// Check isBranchTaken
			if( EX_IF_Latch.getIsBranchTaken() ){

				// Update currentPC if branch taken
				currentPC = EX_IF_Latch.getBranchPC();
				containingProcessor.getRegisterFile().setProgramCounter(
					EX_IF_Latch.getBranchPC()
				);

				if(containingProcessor.getDebugMode().charAt(1) != '0') {
					System.out.println("[BRANCH] to " + Integer.toString(EX_IF_Latch.getBranchPC()));
				}

				//Reset isBranchTaken
				EX_IF_Latch.setIsBranchTaken(false);

				//Increment numBranchTaken
				containingProcessor.setNumBranchTaken(containingProcessor.getNumBranchTaken() + 1);
				
			}
			
			// Fetch Instruction, For every new inst it fetches, numIns++;
			// int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			containingProcessor.setNumIns(containingProcessor.getNumIns() + 1);
			
			// Add Memory Read Event to Queue
			Simulator.getEventQueue().addEvent(
				new MemoryReadEvent(
					Clock.getCurrentTime() + Configuration.mainMemoryLatency,
					this,
					containingProcessor.getMainMemory(),
					currentPC
				)
			);
			IF_EnableLatch.setIF_busy(true);
			// TODO update the ollowing line to if(of not busy)

			// Avoids program crash in first cycles
			IF_OF_Latch.setIsNop(true);

			// Update Latch
			IF_OF_Latch.setPC(currentPC);
			System.out.print("PC set to: " + Integer.toString(IF_OF_Latch.getPC()));

			// Increment PC
			containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);

			// Update the latches
			IF_EnableLatch.setIF_enable(false);
			// IF_OF_Latch.setOF_enable(true);

		}
	}

	@Override
	public void handleEvent(Event e){
		if(IF_OF_Latch.isOF_busy()){
			e.setEventTime(Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(e);
		} else {
			MemoryResponseEvent event = (MemoryResponseEvent) e;

			int newInstruction = event.getValue();
			IF_OF_Latch.setInstruction(newInstruction);
			IF_OF_Latch.setOF_enable(true);
			IF_EnableLatch.setIF_busy(false);

			int currentPC = IF_OF_Latch.getPC();

			// debug
			if(containingProcessor.getDebugMode().charAt(1) != '0') {
				System.out.println("-----------------\n");
				System.out.println("Fetched inst: " + Integer.toString(newInstruction));
				System.out.println("PC: " + Integer.toString(currentPC));
			}
			
			if(containingProcessor.getDebugMode().charAt(1) == '2') {
				Scanner input = new Scanner(System.in);
				System.out.print("Enter an integer: ");
				int number = input.nextInt();
			}

			// Special case for end
			if( newInstruction == -402653184 ){
				IF_OF_Latch.setOF_enable(false);
				IF_EnableLatch.setIF_enable(false);

			}
		}
	}
}
