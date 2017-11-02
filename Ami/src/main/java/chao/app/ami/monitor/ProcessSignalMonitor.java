package chao.app.ami.monitor;

import android.content.Context;
import android.os.Bundle;
import android.os.Process;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import chao.app.ami.Ami;
import chao.app.ami.monitor.annotations.Action;

/**
 * Created by qinchao on 2017/9/29.
 */

@Action("service.monitor.signal")
public class ProcessSignalMonitor extends AbsMonitor {

    private static final String TRACES_FILE = "/data/anr/traces.txt";
    private static final String RM_COMMAND = "rm -rf " + TRACES_FILE;
    private static final String TOUCH_COMMAND = "touch " + TRACES_FILE;
    private static final String CAT_COMMAND = "cat " + TRACES_FILE;

    public ProcessSignalMonitor(Context context) {
        super(context);
    }

    @Override
    public void doMonitorAction(Bundle bundle) {

        File traceFile = new File("/data/anr/traces.txt");
//        try {
//            FileWriter fw = new FileWriter(traceFile);
//            fw.write("");
//            fw.flush();
//            fw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        executeCmd(TOUCH_COMMAND);
        Ami.log("traces.txt exists: " + traceFile.exists());
        Ami.log("readable: " + traceFile.canRead() + ",  writable: " + traceFile.canWrite());


        int pid = bundle.getInt("pid", Process.myPid());
        int signal = bundle.getInt("signal", Process.SIGNAL_QUIT);
        Ami.log("pid: " + pid + ", signal : " + signal);


        Process.sendSignal(pid, signal);

        executeCmd(CAT_COMMAND);
    }

    private boolean executeCmd(String cmd) {
        java.lang.Process process;

        try {
            process = Runtime.getRuntime().exec(cmd);
            InputStream is = process.getInputStream();
            InputStream err = process.getErrorStream();
            Scanner scanner = new Scanner(is);
            while (scanner.hasNextLine()) {
                Ami.log(scanner.nextLine());
            }
            is.close();
            scanner.close();
            scanner = new Scanner(err);
            while (scanner.hasNextLine()) {
                Ami.log(scanner.nextLine());
            }
            err.close();
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
