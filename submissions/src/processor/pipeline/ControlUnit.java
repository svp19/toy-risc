package processor.pipeline;

public class ControlUnit {

    String opCode;
    int opCodeInt;

    public ControlUnit() {
    }

    public String getOpCode() {
        return this.opCode;
    }

    public void setOpCode(int instruction) {
        String bin = Integer.toBinaryString(instruction);
        if( instruction > 0 ){
            bin = String.format("%32s", Integer.toBinaryString(instruction)).replace(' ', '0');
        }
        this.opCode = bin.substring(0, 5);
        this.opCodeInt = Integer.parseInt(opCode, 2);
        // System.out.println("OPCODE => " + opCode);
    }
    
    public int getOpCodeInt(){
        return this.opCodeInt;
    }

    // Memory Instruction Flags 
    public boolean isSt() {
        if( opCode.equals("10111") ){
            System.out.println("STORE");
            return true;
        }
        return false; 
    }

    public boolean isLd() {
        if( opCode.equals("10110") ){
            return true;
        }
        return false; 
    }

    // Control Flow Instruction Flags
    public boolean isJmp() {
        if( opCode.equals("11000") ){
            return true;
        }
        return false; 
    }

    public boolean isBeq() {
        if( opCode.equals("11001") ){
            return true;
        }
        return false; 
    }

    public boolean isBne() {
        if( opCode.equals("11010") ){
            return true;
        }
        return false; 
    }

    public boolean isBlt() {
        if( opCode.equals("11011") ){
            return true;
        }
        return false; 
    }

    public boolean isBgt() {
        if( opCode.equals("11100") ){
            return true;
        }
        return false; 
    }


    // Artihmetic Instruction Flags
    public boolean isR3(){
        if( opCodeInt < 21 && (opCodeInt % 2 == 0) ) {
            return true;
        }
        return false;
    }

    public boolean isImmediate() {
        
        if( opCodeInt <= 23 && (opCodeInt % 2 == 1) ) {
            return true;
        } else if(opCodeInt == 22) {
            return true;
        }
        return false;
    }

    public boolean isWb() {
        if( opCodeInt >= 23) {
            return false;
        }
        return true; 
    }

    public boolean isAdd(){
        if( opCodeInt <=1 ){
            return true;
        }
        return false;
    }

    public boolean isSub(){
        if( opCodeInt == 2 || opCodeInt == 3 ){
            return true;
        }
        return false;
    }


    public boolean isMul(){
        if( opCodeInt == 4 || opCodeInt == 5 ){
            return true;
        }
        return false;
    }

    public boolean isDiv(){
        if( opCodeInt == 6 || opCodeInt == 7 ){
            return true;
        }
        return false;
    }

    public boolean isAnd(){
        if( opCodeInt == 8 || opCodeInt == 9 ){
            return true;
        }
        return false;
    }

    public boolean isOr(){
        if( opCodeInt == 10 || opCodeInt == 11 ){
            return true;
        }
        return false;
    }

    public boolean isXor(){
        if( opCodeInt == 12 || opCodeInt == 13 ){
            return true;
        }
        return false;
    }

    public boolean isSlt(){
        if( opCodeInt == 14 || opCodeInt == 15 ){
            return true;
        }
        return false;
    }

    public boolean isSll(){
        if( opCodeInt == 16 || opCodeInt == 17 ){
            return true;
        }
        return false;
    }

    public boolean isSrl(){
        if( opCodeInt == 18 || opCodeInt == 19 ){
            return true;
        }
        return false;
    }

    public boolean isSra(){
        if( opCodeInt == 20 || opCodeInt == 21 ){
            return true;
        }
        return false;
    }

    // Special Instruction Flags (end) 
    public boolean isEnd() {
        if( opCode.equals("11101") ){
            return true;
        }
        return false; 
    }
}