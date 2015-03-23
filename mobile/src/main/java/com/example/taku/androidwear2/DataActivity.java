package com.example.taku.androidwear2;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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

//>>カウント
    private void Count() {
        PutDataMapRequest dataMapRequest = PutDataMapRequest.create(COUNT_PATH);
        dataMapRequest.getDataMap().putInt(COUNT_KEY, ++count);
        PutDataRequest request = dataMapRequest.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi
                .putDataItem(mGoogleApiClient, request);
        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(DataApi.DataItemResult dataItemResult) {
                Log.d(TAG, "count up:" + count);
            }
        });
    }
//<<カウント

//メッセージ送信>>
    private void sendMessage() {
        Collection<String> nodes = getNodes();
        for (String node : nodes) {
            MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node, START_ACTIVITY_PATH, null).await();
            if (!result.getStatus().isSuccess()) {
                Log.e(TAG, "ERROR: failed to send message: " + result.getStatus());
            }
        }
    }
//<<メッセージ送信

//ノード取得
    private Collection<String> getNodes() {
        HashSet<String> results = new HashSet<String>();
        NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
        for (Node node : nodes.getNodes()) {
            results.add(node.getId());
        }
        return results;
    }

//ローカルノード取得>>
    private String getLocalNodeId() {
        NodeApi.GetLocalNodeResult nodesResult = Wearable.NodeApi.getLocalNode(mGoogleApiClient).await();
        return nodesResult.getNode().getId();
    }
//<<ローカルノード取得



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

//リストア>>
    private void restore() {
        String localNodeId = getLocalNodeId();
        Uri uri = new Uri.Builder().scheme(PutDataRequest.WEAR_URI_SCHEME).authority(localNodeId).path(COUNT_PATH).build();
        Wearable.DataApi.getDataItem(mGoogleApiClient, uri).setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(DataApi.DataItemResult dataItemResult) {
                DataItem dataItem = dataItemResult.getDataItem();
                if (dataItem != null) {
                    DataMap dataMap = DataMapItem.fromDataItem(dataItemResult.getDataItem()).getDataMap();
                    count = dataMap.getInt(COUNT_KEY);
                    Log.d(TAG, "restore count:" + count);
                }
            }
        });
    }
//<<リストア

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
