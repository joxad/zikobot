/*package com.startogamu.zikobot.core.notification;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.orhanobut.logger.Logger;
import com.startogamu.zikobot.core.model.Track;
import com.startogamu.zikobot.core.utils.ZikoUtils;


public enum GoogleApiManager implements
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    INSTANCE;
    private String TAG = GoogleApiManager.class.getSimpleName();
    private GoogleApiClient googleApiClient;

    public void init(Context context) {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Logger.d("onConnected: " + connectionHint);
                        // Now you can use the Data Layer API
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        Logger.d("onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(result -> Log.d(TAG, "onConnectionFailed: " + result))
                // Request access only to the Wearable API
                .addApi(Wearable.API)
                .build();
        googleApiClient.connect();

    }


    public void sendTrack(Track track, Bitmap bitmap) {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/track");
        putDataMapReq.setUrgent();
        putDataMapReq.getDataMap().putString("track_name", track.getName());
        putDataMapReq.getDataMap().putString("track__artist_name", track.getArtistName());
        putDataMapReq.getDataMap().putAsset("track_img", ZikoUtils.createAssetFromBitmap(bitmap));

        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(googleApiClient, putDataReq);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Wearable.DataApi.addListener(googleApiClient, this);
        Logger.d("Connected");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/track") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();

                    //updateCount(dataMap.getInt(COUNT_KEY));
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }
}
*/