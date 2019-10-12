package generic;

import java.io.*;
import java.util.Scanner;
import processor.Clock;
import processor.Processor;
import processor.memorysystem.MainMemory;
import generic.Statistics;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	static Statistics stats;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
		stats = new Statistics();
	}
	
	static void loadProgram(String assemblyProgramFile)
	{
		/*
		 * TODO
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
			System.out.println(main);

			// Read Int from file
			int read4Bytes = 0, i = 0;
			while(dis.available() > 0)
			{
				read4Bytes = dis.readInt();
				mainMemory.setWord(i, read4Bytes);
				System.out.println(read4Bytes);
				i ++ ;
			}
			dis.close();

			// Set updated memory for processor
			processor.setMainMemory(mainMemory);

			System.out.println("Nearly Done");

			/* 2. Set PC to main */
			processor.getRegisterFile().setProgramCounter(main);
			System.out.println("MAIN PC: " + Integer.toString(main));
			
			/*3. Set x0, x1, x2*/
			processor.getRegisterFile().setValue(0, 0);
			processor.getRegisterFile().setValue(1, 65535);
			processor.getRegisterFile().setValue(2, 65535);

			// Debugging - Print Memory
			System.out.println(mainMemory.getContentsAsString(0, 30));

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
			
			System.out.println("--------RW--------");
			processor.getRWUnit().performRW();
			// Clock.incrementClock();
			System.out.println("--------MA--------");
			processor.getMAUnit().performMA();
			// Clock.incrementClock();
			System.out.println("--------EX--------");
			processor.getEXUnit().performEX();
			// Clock.incrementClock();
			
			System.out.println("--------OF--------");
			processor.getOFUnit().performOF();
			// Clock.incrementClock();
			System.out.println("--------IF--------");
			processor.getIFUnit().performIF();
			
			//Increment Number of Cycles
			processor.setNumCycles(processor.getNumCycles() + 1);

			Clock.incrementClock();
			// Scanner input = new Scanner(System.in);
			// System.out.println("NEXT CYCLE: ");
			// int number = input.nextInt();
			// processor.propagateNops();

			System.out.println("--------  --------");
			System.out.println("\n\n");
			setSimulationComplete(processor.getIsEnd());
		}
		
		// TODO
		// set statistics
		stats.setNumberOfInstructions(processor.getNumIns());
		stats.setNumberOfCycles(processor.getNumCycles());
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
