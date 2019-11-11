package processor;

import processor.memorysystem.MainMemory;
import processor.memorysystem.Cache;
import processor.pipeline.EX_IF_LatchType;
import processor.pipeline.EX_MA_LatchType;
import processor.pipeline.Execute;
import processor.pipeline.IF_EnableLatchType;
import processor.pipeline.IF_OF_LatchType;
import processor.pipeline.InstructionFetch;
import processor.pipeline.MA_RW_LatchType;
import processor.pipeline.MemoryAccess;
import processor.pipeline.OF_EX_LatchType;
import processor.pipeline.OperandFetch;
import processor.pipeline.RegisterFile;
import processor.pipeline.RegisterWrite;
import processor.pipeline.ControlUnit;
import processor.pipeline.ArithmeticLogicUnit;
import configuration.Configuration;
import generic.Simulator;

public class Processor {
	
	RegisterFile registerFile;
	MainMemory mainMemory;
	Cache L1i_cache;
	
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	InstructionFetch IFUnit;
	OperandFetch OFUnit;
	Execute EXUnit;
	MemoryAccess MAUnit;
	RegisterWrite RWUnit;

	ControlUnit controlUnit;
	ArithmeticLogicUnit arithmeticLogicUnit;
	boolean isEnd;
	
	// Stats
	int numIns;
	int numCycles;
	int numOFStalls;
	int numBranchTaken;

	// Pipeline Bools
	Boolean OF_EX_Nop;
	Boolean EX_MA_Nop;
	Boolean MA_RW_Nop;

	// Debug Config
	String debug;

	//Simulator
	Simulator simulator;
	
	public Processor()
	{
		registerFile = new RegisterFile();
		mainMemory = new MainMemory();
		L1i_cache = new Cache(this, Configuration.L1i_latency, Configuration.L1i_numberOfLines, true);
		
		IF_EnableLatch = new IF_EnableLatchType();
		IF_OF_Latch = new IF_OF_LatchType();
		OF_EX_Latch = new OF_EX_LatchType();
		EX_MA_Latch = new EX_MA_LatchType();
		EX_IF_Latch = new EX_IF_LatchType();
		MA_RW_Latch = new MA_RW_LatchType();
		
		IFUnit = new InstructionFetch(this, IF_EnableLatch, IF_OF_Latch, EX_IF_Latch);
		OFUnit = new OperandFetch(this, IF_OF_Latch, OF_EX_Latch);
		EXUnit = new Execute(this, OF_EX_Latch, EX_MA_Latch, EX_IF_Latch);
		MAUnit = new MemoryAccess(this, EX_MA_Latch, MA_RW_Latch);
		RWUnit = new RegisterWrite(this, MA_RW_Latch, IF_EnableLatch);

		controlUnit = new ControlUnit();
		arithmeticLogicUnit = new ArithmeticLogicUnit();
		isEnd = false;

		numIns = 0;
		numCycles = 0;
		numOFStalls = 0;
		numBranchTaken = 0;

		debug = "000000";
	}

	public Cache getL1i_cache() {
		return this.L1i_cache;
	}

	public void setL1i_cache(Cache L1i_cache) {
		this.L1i_cache = L1i_cache;
	}

	public void setSimulator(Simulator s){
		this.simulator = s;
	}

	public Simulator getSimulator(Simulator s){
		return this.simulator;
	}

	public void setDebugMode(String debug) {
		this.debug = debug;
	}

	public String getDebugMode() {
		return this.debug;
	}
	
	public void printState(int memoryStartingAddress, int memoryEndingAddress)
	{
		System.out.println(registerFile.getContentsAsString());
		
		System.out.println(mainMemory.getContentsAsString(memoryStartingAddress, memoryEndingAddress));		
	}
	
	public RegisterFile getRegisterFile() {
		return registerFile;
	}

	public void setRegisterFile(RegisterFile registerFile) {
		this.registerFile = registerFile;
	}

	public MainMemory getMainMemory() {
		return mainMemory;
	}

	public void setMainMemory(MainMemory mainMemory) {
		this.mainMemory = mainMemory;
	}

	public ControlUnit getControlUnit() {
		return controlUnit;
	}

	public void setControlUnit(ControlUnit controlUnit) {
		this.controlUnit = controlUnit;
	}

	public ArithmeticLogicUnit getALUUnit() {
		return this.arithmeticLogicUnit;
	}

	public void setALUUnit(ArithmeticLogicUnit arithmeticLogicUnit) {
		this.arithmeticLogicUnit = arithmeticLogicUnit;
	}

	public InstructionFetch getIFUnit() {
		return IFUnit;
	}

	public OperandFetch getOFUnit() {
		return OFUnit;
	}

	public Execute getEXUnit() {
		return EXUnit;
	}

	public MemoryAccess getMAUnit() {
		return MAUnit;
	}

	public RegisterWrite getRWUnit() {
		return RWUnit;
	}

	public boolean getIsEnd() {
		return this.isEnd;
	}

	public void setIsEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}

	public int getNumIns() {
		return this.numIns;
	}

	public void setNumIns(int numIns) {
		this.numIns = numIns;
	}

	public int getNumOFStalls() {
		return this.numOFStalls;
	}

	public void setNumOFStalls(int numOFStalls) {
		this.numOFStalls = numOFStalls;
	}

	public int getNumBranchTaken() {
		return this.numBranchTaken;
	}

	public void setNumBranchTaken(int numBranchTaken) {
		this.numBranchTaken = numBranchTaken;
	}

	public int getNumCycles() {
		return this.numCycles;
	}

	public void setNumCycles(int numCycles) {
		this.numCycles = numCycles;
	}

	public int getRd(int instruction){
        
		ControlUnit cu  = new ControlUnit();
		cu.setOpCode(instruction);
		// Get rd
        String instStr = Integer.toBinaryString(instruction);
        if( instruction > 0 ){
            instStr = String.format("%32s", Integer.toBinaryString(instruction)).replace(' ', '0');
        }
        String rdStr = instStr.substring(10, 15); // R2I-Type (default)
        if(cu.isJmp()){ // memoryAccess(this, EXRI-Type
            rdStr = instStr.substring(5, 10);
        }
        if(cu.isR3()){// R3-Type
            rdStr = instStr.substring(15, 20);
        }
        int rd = Integer.parseInt(rdStr, 2);
        return rd;    
    }

	public int getOpCode(int instruction){
		String bin = Integer.toBinaryString(instruction);
        if( instruction > 0 ){
            bin = String.format("%32s", Integer.toBinaryString(instruction)).replace(' ', '0');
        }
        String opCode = bin.substring(0, 5);
        return Integer.parseInt(opCode, 2);
	}

}
