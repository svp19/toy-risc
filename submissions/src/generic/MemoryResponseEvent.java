package generic;

public class MemoryResponseEvent extends Event {

	int value;
	int address; 
	boolean isResponseForRead;

	public MemoryResponseEvent(long eventTime, Element requestingElement, Element processingElement, int value) {
		super(eventTime, EventType.MemoryResponse, requestingElement, processingElement);
		this.value = value;
		this.isResponseForRead = false;
	}

	public MemoryResponseEvent(long eventTime, Element requestingElement, Element processingElement, int value, int address, boolean isResponseForRead) {
		super(eventTime, EventType.MemoryResponse, requestingElement, processingElement);
		this.value = value;
		this.address = address;
		this.isResponseForRead = isResponseForRead;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}


	public int getAddress() {
		return this.address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public boolean getIsResponseForRead() {
		return this.isResponseForRead;
	}

	public void setIsResponseForRead(boolean isResponseForRead) {
		this.isResponseForRead = isResponseForRead;
	}

}
