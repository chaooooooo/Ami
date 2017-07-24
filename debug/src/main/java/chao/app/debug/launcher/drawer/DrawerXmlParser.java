package chao.app.debug.launcher.drawer;

import android.text.TextUtils;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import chao.app.debug.logs.LogHelper;


public class DrawerXmlParser {

    private static final String TAG = DrawerXmlParser.class.getSimpleName();

    private static final String XML_EVENT_ITEM = "item";
    private static final String XML_EVENT_GROUP = "group";
    private static final String XML_EVENT_DRAWER = "drawer";
    private static final String XML_EVENT_EXTRA = "extra";

    private static final String XML_ATTRIBUTE_NAME = "name";
    private static final String XML_ATTRIBUTE_COMPONENT = "component";
    private static final String XML_ATTRIBUTE_FLAGS = "flags";
    private static final String XML_ATTRIBUTE_EXTRA_KEY = "key";
    private static final String XML_ATTRIBUTE_EXTRA_VALUE = "value";
    private static final String XML_ATTRIBUTE_EXTRA_FORMAT = "format";



    private XmlPullParser mPullParser;
    private DrawerXmlParserListener mXmlParserListener;

    public interface DrawerXmlParserListener {
        void onXmlParserDone(DrawerGroup rootNode);
        void onXmlParserFailed(Exception e);
    }

    public void setOnDrawerXmlParserListener(DrawerXmlParserListener listener) {
        mXmlParserListener = listener;
    }

    public DrawerXmlParser() {
        mPullParser = Xml.newPullParser();
    }

    public void parseDrawer (InputStream in, DrawerXmlParserListener listener) {
        mXmlParserListener = listener;
        try {
            mPullParser.setInput(in, "utf-8");
            int eventType = mPullParser.getEventType();
            DrawerGroup root = null;
            DrawerNode current = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                DrawerNode node;
                String nodeName = mPullParser.getName();
                String name = mPullParser.getAttributeValue(null, XML_ATTRIBUTE_NAME);
                switch (eventType) {

                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (XML_EVENT_DRAWER.equalsIgnoreCase(nodeName)) {
                            root = new DrawerGroup();
                            if (TextUtils.isEmpty(name)) {
                                name = "/";
                            }
                            root.setNodeName(name);
                            current = root;
                        } else if (XML_EVENT_GROUP.equalsIgnoreCase(nodeName)) {
                            node = new DrawerGroup();
                            node.setParent((DrawerGroup) current);
                            if (TextUtils.isEmpty(name)) {
                                name = "<unknown>";
                            }
                            node.setNodeName(name);
                            current = node;
                        } else if (XML_EVENT_ITEM.equalsIgnoreCase(nodeName)) {
                            node = new DrawerNode();
                            node.setParent((DrawerGroup) current);
                            if (TextUtils.isEmpty(name)) {
                                name = "<unknown>";
                            }
                            node.setNodeName(name);
                            node.setComponent(mPullParser.getAttributeValue(null, XML_ATTRIBUTE_COMPONENT));
                            node.setFlags(mPullParser.getAttributeValue(null, XML_ATTRIBUTE_FLAGS));
                            current = node;
                        } else if (XML_EVENT_EXTRA.equalsIgnoreCase(nodeName)) {
                            String extraKey = mPullParser.getAttributeValue(null, XML_ATTRIBUTE_EXTRA_KEY);
                            String extraValue = mPullParser.getAttributeValue(null, XML_ATTRIBUTE_EXTRA_VALUE);
                            String extraFormat = mPullParser.getAttributeValue(null, XML_ATTRIBUTE_EXTRA_FORMAT);
                            current.addExtra(extraKey, extraValue, extraFormat);
                        } else {
                            throw new IllegalStateException("Unknown xml node name: " + nodeName);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (current == null) {
                            LogHelper.e(TAG, "current should not be null.");
                            break;
                        }
                        current = current.getParent();
                        break;
                    default:
                        break;
                }
                eventType = mPullParser.next();
            }
            if (mXmlParserListener != null) {
                if (root != null) {
                    mXmlParserListener.onXmlParserDone(root);
                } else {
                    mXmlParserListener.onXmlParserFailed(null);
                }
            }

        } catch (XmlPullParserException e) {
            if (mXmlParserListener != null) {
                mXmlParserListener.onXmlParserFailed(e);
            }
            e.printStackTrace();
        } catch (IOException e) {
            if (mXmlParserListener != null) {
                mXmlParserListener.onXmlParserFailed(e);
            }
            e.printStackTrace();
        }

    }
}
