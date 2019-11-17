package processor.memorysystem;

public class CacheLine {
    
    static int lineSize;
    int data[];
    int tag;
    boolean isEmpty;

    public CacheLine(){
        CacheLine.lineSize = 1;
        data = new int[lineSize];
        tag = -1;
        isEmpty = true;
    }

    public boolean getIsEmpty() {
        return this.isEmpty;
    }

    public void setIsEmpty(boolean empty) {
        this.isEmpty = empty;
    }

    public int[] getLine() {
        return this.data;
    }

    public void setData(int i, int data) {
        this.data[i] = data;
        isEmpty = false;
    }

    public int getTag() {
        return this.tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
        isEmpty = false;
    }

    public void printLine(){
        System.out.println(data[0] + ", " + tag);
    }
    
}