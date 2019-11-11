package generic;

public class MemoryReadEvent extends Event {

	int addressToReadFrom;
	int newValue;
	
	public MemoryReadEvent(long eventTime, Element requestingElement, Element processingElement, int address) {
		super(eventTime, EventType.MemoryRead, requestingElement, processingElement);
		this.addressToReadFrom = address;
		this.newValue = -1;
	}

	public MemoryReadEvent(long eventTime, Element requestingElement, Element processingElement, int address, int newValue) { // cacheWrite
		super(eventTime, EventType.MemoryRead, requestingElement, processingElement);
		this.addressToReadFrom = address;
		this.newValue = newValue;
	}

	public int getAddressToReadFrom() {
		return addressToReadFrom;
	}

	public void setAddressToReadFrom(int addressToReadFrom) {
		this.addressToReadFrom = addressToReadFrom;
	}

	public int getNewValue() {
		return this.newValue;
	}

	public void setNewValue(int newValue) {
		this.newValue = newValue;
	}

}
