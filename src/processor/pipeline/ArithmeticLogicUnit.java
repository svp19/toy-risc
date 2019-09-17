package processor.pipeline;
import processor.pipeline.ControlUnit;

public class ArithmeticLogicUnit {

    int A;
    int B;
    ControlUnit controlUnit;
    int MAX_INT;

    public ArithmeticLogicUnit() {
        MAX_INT = 2147483647;
    }
    
    public ControlUnit getControlUnit() {
        return this.controlUnit;
    }

    public void setControlUnit(ControlUnit controlUnit) {
        this.controlUnit = controlUnit;
    }

    public int getA() {
        return this.A;
    }

    public void setA(int A) {
        this.A = A;
    }

    public int getB() {
        return this.B;
    }

    public void setB(int B) {
        this.B = B;
    }

    public int getALUResult(){
        
        if(controlUnit.isLd() || controlUnit.isSt()) {  // As [rs1+imm] is needed in Ld/St
            return A + B;
        }

        if(controlUnit.isAdd()){
            long temp = A + B;
            if(temp - MAX_INT > 0) {
                controlUnit.setIsOverflow(true);
                
                String binStr = Long.toString(temp);
                String overflowStr = binStr.substring(0, 32);
                String resultStr = binStr.substring(32);

                controlUnit.setOverflow(Integer.parseInt(overflowStr));
                return Integer.parseInt(resultStr);
            }

            return A + B;
        }

        if(controlUnit.isSub()){
            return A - B;
        }

        if(controlUnit.isMul()){
            long temp = A * B;
            if(temp - MAX_INT > 0) {
                controlUnit.setIsOverflow(true);
                
                String binStr = Long.toString(temp);
                String overflowStr = binStr.substring(0, 32);
                String resultStr = binStr.substring(32);

                controlUnit.setOverflow(Integer.parseInt(overflowStr));
                return Integer.parseInt(resultStr);
            }

            return A * B;
        }

        if(controlUnit.isDiv()){
            return A / B;
        }

        if(controlUnit.isAnd()){
            return A & B;
        }

        if(controlUnit.isOr()){
            return A | B;
        }

        if(controlUnit.isXor()){
            return A ^ B;
        }

        if(controlUnit.isSlt()){
            return A < B ? 1 : 0;
        }

        if(controlUnit.isSll()){
            long temp = A << B;
            if(temp - MAX_INT > 0) {
                controlUnit.setIsOverflow(true);
                
                String binStr = Long.toString(temp);
                String overflowStr = binStr.substring(0, 32);
                String resultStr = binStr.substring(32);

                controlUnit.setOverflow(Integer.parseInt(overflowStr));
                return Integer.parseInt(resultStr);
            }

            return A << B;
        }

        if(controlUnit.isSrl()){
            return A >> B;
        }

        if(controlUnit.isSra()){
            return A >>> B;
        }
        return 0;
    }

    public int getMod(){
        return A % B;
    }

    public boolean getFlag(String type){
        switch(type){
            case "E": return A == B;
            case "GT": return A > B;
            case "LT": return A < B;
            case "NE": return A != B;
        }
        return false;
    }

}