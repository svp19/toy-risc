package generic;

import java.io.*;
import java.util.Scanner;
import processor.Clock;
import processor.Processor;
import processor.memorysystem.MainMemory;
import generic.Statistics;
import generic.EventQueue;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	static EventQueue eventQueue;
	static Statistics stats;
	static String debug;

	public static void setEventQueue() {
		Simulator.eventQueue = new EventQueue();
	}

	public static void setDebugMode(String debug) {
		// Bring to form 011101 (example)
		Simulator.debug = debug;
	}
	
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		simulationComplete = false;
		stats = new Statistics();
	}

	public static EventQueue getEventQueue(){
		return eventQueue;
	}
	
	static void loadProgram(String assemblyProgramFile)
	{
		/*
		 * DESCRIPTION
		 * 1. load the program into memory accordisg to the program layout described
		 *    in the ISA specification
		 * 2. set PC to the address of the first instruction in the main
		 * 3. set the following registers:
		 *     x0 = 0
		 *     x1 = 65535
		 *     x2 = 65535
		 */

		/*1.  Load program into memory*/
		// Fetch mainMemory from processor
		MainMemory mainMemory = processor.getMainMemory();

		try
		{
			// Create Stream to read input file
			FileInputStream fin = new FileInputStream(assemblyProgramFile);
			DataInputStream dis = new DataInputStream(fin);			
			
			// Read PC
			int main = dis.readInt();

			// Read Int from file
			int read4Bytes = 0, i = 0;
			while(dis.available() > 0)
			{
				read4Bytes = dis.readInt();
				mainMemory.setWord(i, read4Bytes);

				if(debug.charAt(0) != '0') {
					System.out.println(read4Bytes);
				}

				i ++ ;
			}
			dis.close();

			// Set updated memory for processor
			processor.setMainMemory(mainMemory);

			/* 2. Set PC to main */
			processor.getRegisterFile().setProgramCounter(main);
			if(debug.charAt(0) != '0') {
				System.out.println("MAIN PC: " + Integer.toString(main));
			}
			
			/*3. Set x0, x1, x2*/
			processor.getRegisterFile().setValue(0, 0);
			processor.getRegisterFile().setValue(1, 65535);
			processor.getRegisterFile().setValue(2, 65535);

			// Debugging - Print Memory
			System.out.println(mainMemory.getContentsAsString(0, 30));

			// Set Debug config for processor
			processor.setDebugMode(debug);
		}
		catch(FileNotFoundException fe)
		{ 
			System.out.println("FileNotFoundException : " + fe);
		}
		catch(IOException ioe)
		{
			System.out.println("IOException : " + ioe);
		}
	}
	
	public static void simulate()
	{
		while(simulationComplete == false)
		{
			
			processor.getRWUnit().performRW();
			processor.getMAUnit().performMA();
			processor.getEXUnit().performEX();
			eventQueue.processEvents();
			processor.getOFUnit().performOF();
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			
			//Increment Number of Cycles
			processor.setNumCycles(processor.getNumCycles() + 1);

			// debug input
			if(debug.charAt(0) == '2') {
				Scanner input = new Scanner(System.in);
				System.out.println("NEXT CYCLE: ");
				int number = input.nextInt();
			}

			// debug print
			if(debug.charAt(0) != '0') {
				System.out.println("--------  --------");
				System.out.println("\n\n");
				System.out.println("--------------------- CACHE ----------------");
				processor.getL1i_cache().printCache();
			}

			// set end state for processorAdded in Q: generic.MemoryReadEvent@73a8dfcc
			setSimulationComplete(processor.getIsEnd());
		}
		
		// set statistics
		stats.setNumberOfInstructions(processor.getNumIns());
		stats.setNumberOfCycles(processor.getNumCycles());
		stats.setNumOFStalls(processor.getNumOFStalls());
		stats.setNumWrongInstruction(processor.getNumBranchTaken() * 2);

	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
