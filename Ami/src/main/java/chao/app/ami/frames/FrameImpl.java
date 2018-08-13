package chao.app.ami.frames;

public abstract class FrameImpl implements IFrame {

    private Object source;

    public FrameImpl(Object object){
        source = object;
    }

    private int position;
    private int offset;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public Object getSource() {
        return source;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
