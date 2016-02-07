package com.tx.njuptjsy.redpacketterminator;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity{

    private Button settingButton,jumpWechatBtn,exitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButtons();
        loopThread();
    }

    private void loopThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (MyApplication.getRunning()){}
            }
        }).start();
    }

    private void initButtons() {
        settingButton = (Button)findViewById(R.id.btn_open_setting);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);//打开系统的辅助设置界面
                startActivity(intent);
            }
        });

        jumpWechatBtn = (Button)findViewById(R.id.btn_open_wechat);
        jumpWechatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName componentName = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
                intent.setComponent(componentName);
                startActivity(intent);
            }
        });

        exitBtn = (Button)findViewById(R.id.btn_exit);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.setRunning(false);
                System.exit(0);
            }
        });
    }

}
