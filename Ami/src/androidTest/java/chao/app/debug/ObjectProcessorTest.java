package chao.app.debug;

import android.app.Application;

import org.junit.Test;

import java.util.ArrayList;

import chao.app.ami.Ami;
import chao.app.ami.frames.search.ObjectInfo;
import chao.app.ami.frames.search.ObjectProcessor;

/**
 * @author chao.qin
 * @since 2018/8/8
 */

public class ObjectProcessorTest {



    @Test
    public void testParseObject() throws IllegalAccessException {
        ObjectProcessor processor = new ObjectProcessor();
        Application application = new Application();
        ArrayList<ObjectInfo> list = new ArrayList<>();
        processor.startSearch(list, "mDataDir", new ObjectInfo(application), "main");
//        for (ObjectInfo objectInfo: list) {
//            System.out.println();
//            printObject(objectInfo);
//            while (objectInfo.getParent() != null) {
//                printObject(objectInfo.getParent());
//            }
//        }
//        Collections.sort(ObjectProcessor.infoList, new Comparator<ObjectInfo>() {
//            @Override
//            public int compare(ObjectInfo o1, ObjectInfo o2) {
//                return o1.getDeep() - o2.getDeep();
//            }
//        });
        Ami.log(ObjectProcessor.infoList.toString());
    }

    private void printObject(ObjectInfo objectInfo) {
        System.out.println(objectInfo);
    }
}
