package com.crossbowffs.debugkit.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import com.crossbowffs.debugkit.R;

public class NotificationInfoActivity extends Activity {
    public static final String ACTION = "com.crossbowffs.debugkit.SHOW_NOTIFICATION_INFO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_info);

        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        if (ACTION.equals(intent.getAction())) {
            String[] stacktrace = intent.getStringArrayExtra("__stacktrace");
            for (String s : stacktrace) {
                textView.setText(textView.getText() + s + '\n');
            }
        }
    }
}
