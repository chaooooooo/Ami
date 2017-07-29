///*
// * Copyright (C) 2014 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package android.net.wifi;
//
//import android.net.ProxyInfo;
//import android.os.Build;
//import android.os.Parcel;
//import android.os.Parcelable;
//import android.support.annotation.RequiresApi;
//
//public class IpConfiguration implements Parcelable {
//    private static final String TAG = "IpConfiguration";
//
//    public enum IpAssignment {
//        /* Use statically configured IP settings. Configuration can be accessed
//         * with staticIpConfiguration */
//        STATIC,
//        /* Use dynamically configured IP settigns */
//        DHCP,
//        /* no IP details are assigned, this is used to indicate
//         * that any existing IP settings should be retained */
//        UNASSIGNED
//    }
//
//
//    public enum ProxySettings {
//        /* No proxy is to be used. Any existing proxy settings
//         * should be cleared. */
//        NONE,
//        /* Use statically configured proxy. Configuration can be accessed
//         * with httpProxy. */
//        STATIC,
//        /* no proxy details are assigned, this is used to indicate
//         * that any existing proxy settings should be retained */
//        UNASSIGNED,
//        /* Use a Pac based proxy.
//         */
//        PAC
//    }
//
//    public ProxySettings proxySettings;
//
//    public ProxyInfo httpProxy;
//
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    protected IpConfiguration(Parcel in) {
//        httpProxy = in.readParcelable(ProxyInfo.class.getClassLoader());
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeParcelable(httpProxy, flags);
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public static final Creator<IpConfiguration> CREATOR = new Creator<IpConfiguration>() {
//        @Override
//        public IpConfiguration createFromParcel(Parcel in) {
//            return new IpConfiguration(in);
//        }
//
//        @Override
//        public IpConfiguration[] newArray(int size) {
//            return new IpConfiguration[size];
//        }
//    };
//}
