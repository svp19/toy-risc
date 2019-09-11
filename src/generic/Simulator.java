package generic;

import java.io.*;
import java.util.Scanner;
import processor.Clock;
import processor.Processor;
import processor.memorysystem.MainMemory;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
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

		// Load program into memory

		// Fetch mainMemory from processor
		MainMemory mainMemory = processor.getMainMemory();

		try
		{
			// Create Stream to read input file
			FileInputStream fin = new FileInputStream(assemblyProgramFile);
			DataInputStream dis = new DataInputStream(fin);			
			
			// Read PC
			int PC = dis.readInt();
			System.out.println(i);

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

		// Set updated memory for processor
		processor.setMainMemory(mainMemory);

		System.out.println("Nearly Done");

	}
	
	public static void simulate()
	{
		while(simulationComplete == false)
		{
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			processor.getOFUnit().performOF();
			Clock.incrementClock();
			processor.getEXUnit().performEX();
			Clock.incrementClock();
			processor.getMAUnit().performMA();
			Clock.incrementClock();
			processor.getRWUnit().performRW();
			Clock.incrementClock();
		}
		
		// TODO
		// set statistics
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
