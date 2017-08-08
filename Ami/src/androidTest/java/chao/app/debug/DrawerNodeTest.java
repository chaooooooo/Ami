package chao.app.debug;

import android.os.Parcel;
import android.os.Parcelable;

import org.junit.Test;

import java.io.Serializable;

import chao.app.ami.launcher.drawer.ComponentNode;

/**
 * @author chao.qin
 * @since 2017/7/23
 */

public class DrawerNodeTest {
    ComponentNode node;
    @Test
    public void testAddExtra() {
        node = new ComponentNode("");

//        node.addExtra("serialize", A.class.getName(), "object");
//        node.addExtra("parcelable", B.class.getName(), "object");

        node.setFlags("FLAG_ACTIVITY_BROUGHT_TO_FRONT|FLAG_ACTIVITY_CLEAR_TASK|0x00001000|abcdefgh");

        log("flags : " + Integer.toHexString(node.getFlags()));

    }

    private static void log(String log) {
        System.out.println(log);
    }

    public class A implements Serializable {
        private int a;
    }

    private static class B implements Parcelable {
        private int b;

        protected B(Parcel in) {
        }

        public static final Creator<B> CREATOR = new Creator<B>() {
            @Override
            public B createFromParcel(Parcel in) {
                return new B(in);
            }

            @Override
            public B[] newArray(int size) {
                return new B[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }
    }
}
