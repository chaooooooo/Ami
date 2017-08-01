package chao.app.ami.proxy;

import android.app.Application;
import android.content.Context;
import android.net.ProxyInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import chao.app.ami.utils.ReflectUtil;

/**
 * @author chao.qin
 * @since 2017/7/27
 */

public class ProxyManager {

    private static ProxyManager mProxyManager;
    private static Application mApp;
    private static ProxyInfo mInfo;

    private ProxyManager(Application app) {
        mApp = app;
    }

    public static void init(Application app) {
        if (mProxyManager != null) {
            return;
        }
        mProxyManager = new ProxyManager(app);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static String[] getProxy() {
        WifiManager wifiManager = (WifiManager) mApp.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration configuration = getCurrentWifiConfiguration(wifiManager);

        if (configuration == null) {
            return null;
        }
        Field WifiConfiguration_mIpConfiguration = null;
        try {
            WifiConfiguration_mIpConfiguration = ReflectUtil.getField(WifiConfiguration.class, "mIpConfiguration");
            Object mIpConfiguration = WifiConfiguration_mIpConfiguration.get(configuration);

            Field httpProxyField = ReflectUtil.getField(mIpConfiguration.getClass(), "httpProxy");
            ProxyInfo httpProxy = (ProxyInfo) httpProxyField.get(mIpConfiguration);
            if (httpProxy == null) {
                return null;
            }

            return new String[]{httpProxy.getHost(), String.valueOf(httpProxy.getPort())};

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setProxy(String host, int port) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        WifiManager wifiManager = (WifiManager) mApp.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration configuration = getCurrentWifiConfiguration(wifiManager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mInfo = ProxyInfo.buildDirectProxy(host, port);
        }
        if (configuration != null){
            Class parmars = Class.forName("android.net.ProxyInfo");
            Method method = WifiConfiguration.class.getMethod("setHttpProxy",parmars);
            method.invoke(configuration, mInfo);


            Field WifiConfiguration_mIpConfiguration = configuration.getClass().getDeclaredField("mIpConfiguration");
            WifiConfiguration_mIpConfiguration.setAccessible(true);
            Object mIpConfiguration = WifiConfiguration_mIpConfiguration.get(configuration);


            Field IpConfiguration_proxySettings = mIpConfiguration.getClass().getDeclaredField("proxySettings");
            IpConfiguration_proxySettings.setAccessible(true);
            IpConfiguration_proxySettings.set(mIpConfiguration, Enum.valueOf((Class<Enum>)IpConfiguration_proxySettings.getType(), "STATIC"));

            WifiConfiguration_mIpConfiguration.set(configuration, mIpConfiguration);
            try {
                configuration = WifiConfiguration.class.getConstructor(WifiConfiguration.class).newInstance(configuration);
            } catch (InstantiationException e) {
                e.printStackTrace();
            }

            wifiManager.saveConfiguration();

            Class<?> class_listener = Class.forName("android.net.wifi.WifiManager$ActionListener");
            Object listener = Proxy.newProxyInstance(wifiManager.getClass().getClassLoader(),new Class[]{class_listener}, new ActionListenerHook());

            Method WifiManager_save = ReflectUtil.getMethod(WifiManager.class, "save", WifiConfiguration.class,class_listener);
            WifiManager_save.invoke(wifiManager, configuration, listener);

            //save the settings
            wifiManager.updateNetwork(configuration);
//            wifiManager.disconnect();
//            wifiManager.reconnect();
        }
    }


    // 获取当前的Wifi连接配置
    private static WifiConfiguration getCurrentWifiConfiguration(WifiManager wifiManager) {
        if (!wifiManager.isWifiEnabled())
            return null;
        List<WifiConfiguration> configurationList = wifiManager.getConfiguredNetworks();
        WifiConfiguration configuration = null;
        int cur = wifiManager.getConnectionInfo().getNetworkId();
        // Log.d("当前wifi连接信息",wifiManager.getConnectionInfo().toString());
        for (int i = 0; i < configurationList.size(); ++i) {
            WifiConfiguration wifiConfiguration = configurationList.get(i);
            if (wifiConfiguration.networkId == cur) {
                configuration = wifiConfiguration;
                return configuration;
            }
        }
        return null;
    }
}
