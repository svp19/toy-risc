package processor.memorysystem;

import processor.Processor;
import generic.Event;
import generic.Element;
import java.lang.Math;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.MemoryWriteEvent;
import generic.Simulator;
import generic.Event.EventType;
import processor.Clock;
import configuration.Configuration;

public class Cache implements Element{
    static int associativity = 2;
    Processor containingProcessor;
    int cacheSize;
    int latency;
    int numLines;
    int numSetIndexBits;
    CacheLine cacheLine[];
    boolean isL1i;
    int LRU[];  // Stores LR used index of a set (as assoc = 2)
    
    public Cache(){
        cacheSize = latency = numLines = -1;
    }

    public Cache(Processor containingProcessor, int _latency, int _numLines, boolean isL1i){
        this.cacheSize = _numLines * 4 ;
        this.latency = _latency;
        this.numLines = _numLines;
        this.numSetIndexBits = (int)(Math.log(numLines/2)/Math.log(2));
        this.isL1i = isL1i;
        this.LRU = new int[128];
        this.containingProcessor = containingProcessor;
        this.cacheLine = new CacheLine[1024];

        for(int i = 0; i < 1024; i++) {
            cacheLine[i] = new CacheLine();
        }

        for(int i = 0; i<128; i++) {
            this.LRU[i] = 0;
        }
    }

    /* cacheRead
        offset => 0 bits(assumed only 1 value in line)
        setIndex => log(numLines/2)
        tag => (remaining)
    */
    
    public String to32BitString(int address){
        //convert to bit string
        String binary = String.format("%32s", Integer.toBinaryString(address)).replace(' ', '0');
        System.out.println(binary);
        return binary;
    }
    
    public int findTag(String binary){
        //find tag
        String tagStr = binary.substring(0, 32-numSetIndexBits);
        int tag = Integer.parseInt(tagStr);
        System.out.println("tag: " + tagStr);
        return tag;
    }

    public int findSetIndex(String binary){
        //find setIndex
        int setIndex = 0;
        if(numSetIndexBits != 0){
            String setIndexStr = binary.substring(32-numSetIndexBits, 32);
            System.out.println("setIndex: " + setIndexStr);
            setIndex = Integer.parseInt(setIndexStr, 2);
        }
        return setIndex;
    }

    public void cacheRead(int address, Element requestingElement){
        
        String binary = to32BitString(address);
        int tag =findTag(binary);
        int setIndex = findSetIndex(binary);
        
        
        for(int index=setIndex; index<setIndex+associativity; ++index){
            System.out.println(index);
            if(!cacheLine[index].getIsEmpty()){
                System.out.println(cacheLine[index]);
                if(cacheLine[index].getTag() == tag){
                    int value = cacheLine[index].getLine()[0];  // For now return line[0] as only one word a line

                    // Add Memory Response Event to Queue for IF Unit
                    Simulator.getEventQueue().addEvent(
                        new MemoryResponseEvent(
                            Clock.getCurrentTime() + latency,
                            this,
                            requestingElement,
                            value,
                            address
                        )
                    );
                }
            }
        }

        // address not present in cache
        handleCacheMiss(address);
        return;
    }

    public void handleCacheMiss(int address){
        // address => currentPC for L1i and location for L1d
        // Add Memory Read Event to Queue
        Simulator.getEventQueue().addEvent(
            new MemoryReadEvent(Clock.getCurrentTime() + Configuration.mainMemoryLatency, this,containingProcessor.getMainMemory(),address)
        );  
        return;
    }

    // @Override
	public void handleEvent(Event e) {
        if(isL1i){
            // forward MemoryResponse for Instruction Fetch
            MemoryResponseEvent event = (MemoryResponseEvent) e;
            event.setEventTime(Clock.getCurrentTime() + latency);
            event.setRequestingElement(this);
            event.setProcessingElement(containingProcessor.getIFUnit());
            Simulator.getEventQueue().addEvent(event);

            // insert Value of Memory Response into Cache
            int address = event.getAddress();
            String binary = to32BitString(address);
            int tag = findTag(binary);
            int setIndex = findSetIndex(binary);
            int writeIndex = LRU[setIndex];

            // update LRU 
            LRU[setIndex] = 1 - LRU[setIndex];

            // write
            cacheLine[setIndex + writeIndex].setData(0, event.getValue());
            cacheLine[setIndex + writeIndex].setTag(tag);
        }
        
	}
}