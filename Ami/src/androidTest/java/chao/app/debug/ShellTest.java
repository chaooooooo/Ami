package chao.app.debug;

import android.os.SystemClock;
import chao.app.ami.Ami;
import chao.app.ami.utils.command.CommandResult;
import chao.app.ami.utils.command.Shell;
import chao.app.ami.utils.command.StreamGobbler;
import org.junit.Test;

/**
 * @author qinchao
 * @since 2018/9/5
 */
public class ShellTest {

    @Test
    public void testShellRun() {
        CommandResult result = Shell.run("id");
        Ami.log("successful: " + result.isSuccessful());
        Ami.log("err: " + result.stderr);
        Ami.log("std:" + result.stdout);

        SystemClock.sleep(2000);
    }

    @Test
    public void testShellShRun() {
        Shell.Builder builder = new Shell.Builder();
        builder.addCommand("logcat -v threadtime")
            .setOnStdoutLineListener(new StreamGobbler.OnLineListener() {

                @Override
                public void onLine(String line) {
                    if (line.contains("AMI")) {
                        return;
                    }
                    Ami.log("std: " + line);
                }
            })
            .setOnStderrLineListener(new StreamGobbler.OnLineListener() {
                @Override
                public void onLine(String line) {
                    if (line.contains("AMI")) {
                        return;
                    }
                    Ami.log("err: " + line);
                }
            })
            .open();
             SystemClock.sleep(2000);


    }

}
