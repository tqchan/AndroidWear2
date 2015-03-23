package com.example.taku.androidwear2;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import static android.view.View.OnClickListener;


public class DataActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = "DataActivity";
    private static final String COUNT_KEY = "COUNT_KEY";
    private static final String COUNT_PATH = "/count";
    private static final String START_ACTIVITY_PATH = "/start/MainActivity";

    private int count = 0;
    private GoogleApiClient mGoogleApiClient;

    Button count_button;
    Button send_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        count_button = (Button) findViewById(R.id.count_button);
        send_button = (Button) findViewById(R.id.send_message_button);

        count_button.setOnClickListener(countClickListener);
        send_button.setOnClickListener(sendClickListener);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onRestart(){
        super.onRestart();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

//ボタン>>
    OnClickListener countClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Count();
        }
    };

    OnClickListener sendClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    sendMessage();
                    return null;
                }
            }.execute();
        }
    };
//<<ボタン


    private void Count() {

    }

    private void sendMessage() {
    }

//接続>>
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "connected");
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids){
                restore();
                return null;
            }
        }.execute();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
//<<接続

    private void restore() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data, menu);
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

}
