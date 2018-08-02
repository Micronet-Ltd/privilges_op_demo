package com.micronet.test3usb.testapp4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    protected final static String TAG = "SETPROP";
    Class SystemProperties;
    Method setProp = null;
    Method getProp = null;
    String propName = "op.se_dom_ex";
    String val = "/data/script.sh";
    String scriptFinishProperty = "script.finish";
    TextView status;
    Button button;
    CheckThread checkThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        status = (TextView) findViewById(R.id.status);
        status.setText("Click to run the script");
    }

    private void getSystemPropertyMethods() {
        try {

            @SuppressWarnings("rawtypes")
            Class sp = Class.forName("android.os.SystemProperties");
            SystemProperties = sp;

            setProp = SystemProperties.getMethod("set", new Class[]{String.class, String.class});
            getProp = SystemProperties.getMethod("get", new Class[]{String.class});

        } catch (IllegalArgumentException iAE) {
            throw iAE;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onClick(View view) {
        button = (Button) findViewById(R.id.button);
        button.setEnabled(false);
        status.setText("Script running");
        getSystemPropertyMethods();
        try {
            //set property value, set scriptFinish value
            setProp.invoke(SystemProperties, new Object[]{scriptFinishProperty, "0"});
            setProp.invoke(SystemProperties, new Object[]{propName, val});
            checkThread = new CheckThread();
            checkThread.start();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public class CheckThread extends Thread {

        public void run() {
            while (!isInterrupted()) {
                try {
                    String prop = "";
                    prop = (String) getProp
                            .invoke(SystemProperties, new Object[]{scriptFinishProperty});
                    //wait to property script.finish will be 1 (script finish)
                    while (!prop.equalsIgnoreCase("1")) {
                        prop = (String) getProp
                                .invoke(SystemProperties, new Object[]{scriptFinishProperty});
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            status = (TextView) findViewById(R.id.status);
                            button = (Button) findViewById(R.id.button);
                            status.setText("script finished succesfully");
                            button.setEnabled(true);
                        }
                    });
                    setProp.invoke(SystemProperties, new Object[]{scriptFinishProperty, "0"});
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
