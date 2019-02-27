package org.foxail.android.shellprocessor;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileReceiverActivity extends Activity {

    static final String EDITOR_PROGRAM = "vim.sh";
    //static final String EDITOR_PROGRAM = "/data/data/com.termux/files/home/bin/termux-file-editor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Intent intent = getIntent();
        final String action = intent.getAction();
        final String type = intent.getType();
        final String scheme = intent.getScheme();

        if (!"file".equals(scheme)) {
            finish();
        }

        String filePath = intent.getData().getPath();

        Intent execIntent = new Intent();
        execIntent.setAction("com.twofortyfouram.locale.intent.action.FIRE_SETTING");
        execIntent.setClassName("com.termux.tasker", "com.termux.tasker.FireReceiver");

        Bundle extraBundle = new Bundle();
        extraBundle.putString("com.termux.tasker.extra.EXECUTABLE", EDITOR_PROGRAM);
        extraBundle.putInt("com.termux.tasker.extra.VERSION_CODE", 10);
        extraBundle.putString("com.termux.execute.arguments", filePath);
        extraBundle.putBoolean("com.termux.tasker.extra.TERMINAL", Boolean.TRUE); // 默认打开终端界面
        execIntent.putExtra("com.twofortyfouram.locale.intent.extra.BUNDLE", extraBundle);

        sendBroadcast(execIntent);

        finish();
    }

    /*
    @Override
    protected void onResume() {
        super.onResume();

        final Intent intent = getIntent();
        final String action = intent.getAction();
        final String type = intent.getType();
        final String scheme = intent.getScheme();

        if (!"file".equals(scheme)) {
            finish();
        }

        String filePath = intent.getData().getPath();
        String[] cmd = new String[]{
                "su", "-c"
                ,"am start-foreground-service"
                + " -a com.termux.service_execute"
                + " -d file://" + EDITOR_PROGRAM
                + " --esa com.termux.execute.arguments " + filePath
                + " com.termux/.app.TermuxService --user 10177"
        };
        try {
            ProcessBuilder builder = new ProcessBuilder(cmd);
            // 重定向错误流到标准输出流
            builder.redirectErrorStream(true);
            // stdout
            Process process = builder.start();
            // start the process and start a new thread to handle the stream
            // input
            new Thread(new ProcessHandleRunnable(process)).start();
            //process.waitFor(); // wait if needed
        } catch (IOException e) {
            e.printStackTrace();
        }
        finish();
    }

    class ProcessHandleRunnable implements Runnable {
        private Process process;

        public ProcessHandleRunnable(Process process) {
            this.process = process;
        }

        public void run() {
            BufferedReader br = null;
            InputStreamReader reader = null;
            try {
                System.out.println("start run...");
                reader = new InputStreamReader(process.getInputStream());
                br = new BufferedReader(reader);
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                System.out.println("stop run...");
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (br != null)
                        br.close();
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    */
}
