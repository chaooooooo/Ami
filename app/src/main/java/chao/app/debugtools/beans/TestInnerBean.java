package chao.app.debugtools.beans;

import java.io.Serializable;

/**
 * @author qinchao
 * @since 2018/9/4
 */
public class TestInnerBean implements Serializable {

    private boolean bInVar;

    private int iInVar = -1;

    private String sInVar;


    public boolean isbInVar() {
        return bInVar;
    }

    public void setbInVar(boolean bInVar) {
        this.bInVar = bInVar;
    }

    public int getiInVar() {
        return iInVar;
    }

    public void setiInVar(int iInVar) {
        this.iInVar = iInVar;
    }

    public String getsInVar() {
        return sInVar;
    }

    public void setsInVar(String sInVar) {
        this.sInVar = sInVar;
    }

    @Override
    public String toString() {
        return "TestInnerBean{" +
            "bInVar=" + bInVar +
            ", iInVar=" + iInVar +
            ", sInVar='" + sInVar + '\'' +
            '}';
    }
}
