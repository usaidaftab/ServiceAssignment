package com.example.death.serviceassignment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button activate,deactivate;
    EditText timelimit;
    TextView progress;
    String str;
    static int time=1;

    MyReceiver Reciver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activate=(Button)findViewById(R.id.button3);
        deactivate=(Button)findViewById(R.id.button4);
        progress=(TextView)findViewById(R.id.textView);
        timelimit=(EditText)findViewById(R.id.editText2);

        Reciver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyService.update);
        registerReceiver(Reciver, intentFilter);
        super.onStart();

        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str = (timelimit.getText().toString());
                time= Integer.parseInt(str);
                Intent intent=new Intent(MainActivity.this,MyService.class);
                startService(intent);

            }
        });

        deactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this, "Service Stoped", Toast.LENGTH_SHORT).show();
                stopService(new Intent(MainActivity.this,MyService.class));

            }
        });
    }
    @Override
    protected void onStop() {
        unregisterReceiver(Reciver);
        super.onStop();
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MyService.update)) {
                int int_from_service = intent.getIntExtra(MyService.key, 0);
                float percentage=int_from_service;
                percentage=percentage/time*100;
                int k=Math.round(percentage);
                progress.setText(k+"%");
            }
        }
    }
}
