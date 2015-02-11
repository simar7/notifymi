package com.example.notifymi;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.notification.NotificationListenerService;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    private TextView textView;
    private NotificationReceiver nReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        nReceiver = new NotificationReceiver();
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction("com.example.notifymi.NOTIFICATION_LISTENER_EXAMPLE");
        registerReceiver(nReceiver, iFilter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // This is the magic.
    public void buttonClicked(View v) {
        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder nComp = new NotificationCompat.Builder(this);
        nComp.setContentTitle("Sexy Notification");
        nComp.setContentText("NotificationlistenerServlet Example");
        nComp.setTicker("NotificationListenerServlet Example");
        nComp.setSmallIcon(R.drawable.ic_launcher);
        nComp.setAutoCancel(true);
        nManager.notify((int)System.currentTimeMillis(), nComp.build());
    }

    class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String receivedString = intent.getStringExtra("notification_event") + "\n" + textView.getText();
            textView.setText(receivedString);
        }
    }
}
