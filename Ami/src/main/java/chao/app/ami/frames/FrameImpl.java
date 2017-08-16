package chao.app.ami.frames;

public abstract class FrameImpl implements IFrame {

    private int position;
    private int offset;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
