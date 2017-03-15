package com.example.matt.placedetector;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;


public class MainActivity
        extends ActionBarActivity
        implements
        GoogleApiClient.OnConnectionFailedListener {


    private static final String LOG_TAG = "PlacesAPIActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private GoogleApiClient mGoogleApiClient, nGoogleApiClient;
    private static final int PERMISSION_REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button currentButton = (Button) findViewById(R.id.currentButton);
        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .build();


        nGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)

                .addOnConnectionFailedListener(this)
                .build();

        currentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGoogleApiClient.isConnected()) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                PERMISSION_REQUEST_CODE);
                        TextView mTextView = (TextView) findViewById(R.id.textView);
                        mTextView.setText("nope");
                    } else {
                       callPlaceDetectionApi();
 //                       pCraft();
                    }

                }
            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPlaceDetectionApi();
 //                   pCraft();
                }
                break;
        }
    }

    private void pCraft(){
        String placeId = "ChIJO_jYFvpzhlQR1tl-Z2MuPtU";

        Places.GeoDataApi.getPlaceById(nGoogleApiClient, placeId)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess()) {
                            final Place myPlace = places.get(0);
                            //Log.i(TAG, "Place found: " + myPlace.getName());
                            TextView mTextView = (TextView) findViewById(R.id.textView);
                            mTextView.setText("didnt work");
                        } else {
                            //Log.e(TAG, "Place not found");
                            TextView mTextView = (TextView) findViewById(R.id.textView);
                            mTextView.setText("didnt work");
                        }
                        places.release();
                    }
                });
    }


    private void callPlaceDetectionApi() throws SecurityException {
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
     //           for (PlaceLikelihood placeLikelihood : likelyPlaces) {
      //              Log.i(LOG_TAG, String.format("Place '%s' with " +
      //                              "likelihood: %g",
      //                      placeLikelihood.getPlace().getName(),
      //                      placeLikelihood.getLikelihood()));
//
//                }
//                likelyPlaces.release();
//                String content = "";
//                if (likelyPlaces.getCount() == 0) {
//                    TextView mTextView = (TextView) findViewById(R.id.textView);
//                    mTextView.setText("You are not at any places");
//                }
//                if (likelyPlaces.getCount() != 0) {
//                    PlaceLikelihood placeLikelihood = likelyPlaces.get(0);
//                    if (placeLikelihood != null && placeLikelihood.getPlace() != null && !TextUtils.isEmpty(placeLikelihood.getPlace().getName()))
//                        content = "Most likely place: " + placeLikelihood.getPlace().getName() + "\n";
//                    if (placeLikelihood != null)
//                        content += "Percent change of being there: " + (int) (placeLikelihood.getLikelihood() * 100) + "%";
//                    TextView mTextView = (TextView) findViewById(R.id.textView);
//                    mTextView.setText(content);
//                }

                if (!likelyPlaces.getStatus().isSuccess()) {
                    // Request did not complete successfully
                    TextView mTextView = (TextView) findViewById(R.id.textView);
                    mTextView.setText("Request did not complete successfully");
                    likelyPlaces.release();
                    return;
                }
                String likelyHood = (likelyPlaces.get(0).getLikelihood() * 100) + "%";
                String placeName = String.format("%s", likelyPlaces.get(0).getPlace().getName());
                String placeAttributuion = String.format("%s", likelyPlaces.get(0).getPlace().getAddress());
                TextView mTextView = (TextView) findViewById(R.id.textView);
                mTextView.setText(placeName + " " + likelyHood);
                likelyPlaces.release();










            }
        });
    }



}
