package com.example.notifymi;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.notification.NotificationListenerService;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bionym.ncl.Ncl;
import com.bionym.ncl.NclCallback;
import com.bionym.ncl.NclEvent;
import com.bionym.ncl.NclEventInit;
import com.bionym.ncl.NclMode;
import com.bionym.ncl.NclProvision;

public class MainActivity extends ActionBarActivity {

    static final String LOG_TAG = "notifymi_LOGGER";
    static boolean nclInitialized = false;

    boolean connectNymi = true;
    int nymiHandle = Ncl.NYMI_HANDLE_ANY;
    NclProvision provision;

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

    /* *********** Custom methods *********** */

    // This is the magic.
    public void buttonClicked(View v) {
        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder nComp = new NotificationCompat.Builder(this);
        nComp.setContentTitle("notifymiTestNotification");
        nComp.setContentText("NotificationlistenerServlet Example");
        nComp.setTicker("NotificationListenerServlet Example");
        nComp.setSmallIcon(R.drawable.ic_launcher);
        nComp.setAutoCancel(true);
        nManager.notify((int)System.currentTimeMillis(), nComp.build());
    }

    //TODO: Define xml layout things
    protected void nclInitialized() {
        View selectLibraryContainer = findViewById(R.id.selectLibContainer);
        selectLibraryContainer.setVisibility(View.GONE);
    }

    protected boolean initializeNclforNymi() {
        if(!nclInitialized) {
            NclCallback nclCallback = new MyNclCallback();
            boolean result = Ncl.init(nclCallback, null, "notifymi", NclMode.NCL_MODE_DEFAULT, this);

            if(!result) {
                Toast.makeText(MainActivity.this, "Failed to initialize NCL library!", Toast.LENGTH_LONG).show();
                return false;
            }
            nclInitialized = true;
            nclInitialized();
        }
        return true;
    }

    protected void initializeNcl() {
        if(!nclInitialized) {
            if(connectNymi) {
                initializeNclforNymi();
            }
        }
    }


    /* *********** Custom classes *********** */
    class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String receivedString = intent.getStringExtra("notification_event") + "\n" + textView.getText();
            textView.setText(receivedString);
        }
    }

    class MyNclCallback implements NclCallback {
        @Override
        public void call(NclEvent event, Object userData) {
            Log.d(LOG_TAG, this.toString() + ": " + event.getClass().getName());
            if(event instanceof NclEventInit) {
                if(!((NclEventInit) event).success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Failed to initialize NCL library!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }
    }
}
