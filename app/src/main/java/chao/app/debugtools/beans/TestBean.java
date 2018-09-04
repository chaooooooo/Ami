package chao.app.debugtools.beans;

import java.io.Serializable;

/**
 * @author qinchao
 * @since 2018/9/4
 */
public class TestBean implements Serializable {

    private boolean bVar = false;

    private int iVar = -1;

    private String sVar;

    private TestInnerBean innerBean;

    public boolean isbVar() {
        return bVar;
    }

    public void setbVar(boolean bVar) {
        this.bVar = bVar;
    }

    public int getiVar() {
        return iVar;
    }

    public void setiVar(int iVar) {
        this.iVar = iVar;
    }

    public String getsVar() {
        return sVar;
    }

    public void setsVar(String sVar) {
        this.sVar = sVar;
    }

    public TestInnerBean getInnerBean() {
        return innerBean;
    }

    public void setInnerBean(TestInnerBean innerBean) {
        this.innerBean = innerBean;
    }

    @Override
    public String toString() {
        return "TestBean{" +
            "bVar=" + bVar +
            ", iVar=" + iVar +
            ", sVar='" + sVar + '\'' +
            ", innerBean=" + innerBean +
            '}';
    }
}
