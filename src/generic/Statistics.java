package generic;

import java.io.PrintWriter;

public class Statistics {
	
	// TODO add your statistics here
	static int numberOfInstructions;
	static int numberOfCycles;
	static int numOFStalls;
	static int numWrongInstruction;

	public Statistics() {
		numberOfInstructions = 0;
		numberOfCycles = 0;
		numOFStalls = 0;
		numWrongInstruction = 0;
	}

	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);
			
			writer.println("Number of instructions executed = " + numberOfInstructions);
			writer.println("Number of cycles taken = " + numberOfCycles);
			writer.println("Number of OF stalls taken = " + numOFStalls);
			writer.println("Number of Wrong Instructions executed = " + numWrongInstruction);

			
			// TODO add code here to print statistics in the output file
			
			writer.close();
		}
		catch(Exception e)
		{
			Misc.printErrorAndExit(e.getMessage());
		}
	}
	
	// TODO write functions to update statistics
	public void setNumberOfInstructions(int numberOfInstructions) {
		Statistics.numberOfInstructions = numberOfInstructions;
	}

	public void setNumberOfCycles(int numberOfCycles) {
		Statistics.numberOfCycles = numberOfCycles;
	}

	public void setNumOFStalls(int numOFStalls){
		Statistics.numOFStalls = numOFStalls;
	}

	public void setNumWrongInstruction(int numWrongInstruction){
		Statistics.numWrongInstruction = numWrongInstruction;
	}

}
