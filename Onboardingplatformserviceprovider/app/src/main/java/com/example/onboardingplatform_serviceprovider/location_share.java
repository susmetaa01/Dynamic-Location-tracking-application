package com.example.onboardingplatform_serviceprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class location_share extends AppCompatActivity {

    FusedLocationProviderClient mFusedLocationClient;


    // Initializing other items
    // from layout file
    TextView latitudeTextView, longitTextView, distTextView;
    int PERMISSION_ID = 44;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");
    DatabaseReference myRef1 = database.getReference("success").child("name");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_share);
        Button calc_dist = findViewById(R.id.calc_dist);
        latitudeTextView = findViewById(R.id.latTextView);
        longitTextView = findViewById(R.id.lonTextView);
        distTextView = findViewById(R.id.dist);
        System.out.println();


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();

        calc_dist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdata();

            }


        });
    }

    private void getdata() {

        // calling add value event listener method
        // for getting the values from database.
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String value = snapshot.getValue().toString();
                System.out.println(">>>>>>>>><<<<<<<<<<<<>>>>>>>>>>>><<<<<<<"+value);
                if(value=="value1"){
                    Toast toast = Toast.makeText(getApplicationContext(), "No match yet", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    distTextView.setText(value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(location_share.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }




            private void getLastLocation(){
                if (checkPermissions()) {


                    if (isLocationEnabled()) {


                        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {


                                    latitudeTextView.setText(location.getLatitude() + "");
                                    longitTextView.setText(location.getLongitude() + "");
                                    System.out.println("!!!!+++++++++++++++++++++++++++++++++++++++++++++"+location);
                                    String loc = location.toString();
                                    Double latt = location.getLatitude();
                                    Double lonn = location.getLongitude();
                                    //myRef.setValue(loc);

                                    /*
                                    myRef.child("userx"+currUser).child("latitude").setValue(latt);
                                    myRef.child("userx"+currUser).child("longitude").setValue(lonn);

*/
                                    //myRef.child("hello").setValue("hii");

                                    for(int i = 0;i<2;i++){
                                        myRef.child("userxx"+i).child("latitude").setValue(latt);
                                        myRef.child("userxx"+i).child("longitude").setValue(lonn);
                                    }


                                     /*

                                        myRef.child("user5").child("latitude").setValue(latt);
                                        myRef.child("user5").child("longitude").setValue(lonn);

*/

                                    System.out.println("===+++++++++++++++++++++++++++++++++++++++++++++"+loc);




                                    Location startPoint=new Location("");
                                    startPoint.setLatitude(77.372102);
                                    startPoint.setLongitude(98.484196);

                                    System.out.println("###############################"+startPoint);
                                    /*

                                    Location endPoint=new Location("");
                                    endPoint.setLatitude(27.375775);
                                    endPoint.setLongitude(43.469218);

                                    System.out.println("##############################"+endPoint);
*/
                                    float[] results = new float[1];
                                    Location.distanceBetween(startPoint.getLatitude(), startPoint.getLongitude(), location.getLatitude(), location.getLongitude(), results);
                                    float distance = results[0];

                /*
                //double distance = mLastLocation.distanceTo(endPoint);
                double distance = startPoint.distanceTo(endPoint);
                System.out.println("###############################"+distance);
                distTextView.setText("distance is"+distance);

                 */
                                    System.out.println("###############################"+distance);
                                    //distTextView.setText("distance is"+distance);

                                }
                            }
                        });




                    } else {
                        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                } else {
                    // if permissions aren't available,
                    // request for permissions
                    requestPermissions();
                }
            }

            private void requestNewLocationData(){
                LocationRequest mLocationRequest = new LocationRequest();
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationRequest.setInterval(5);
                mLocationRequest.setFastestInterval(0);
                mLocationRequest.setNumUpdates(1);

                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

            }

            private LocationCallback mLocationCallback = new LocationCallback(){
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);


                    Location mLastLocation = locationResult.getLastLocation();
                    latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
                    longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");

                    System.out.println("Changed+++++++++++++++++++++++++++++++++++++++++++++"+mLastLocation);




                }
            };

            private boolean checkPermissions() {
                return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;


            }

            // method to request for permissions
            private void requestPermissions() {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
            }

            // method to check
            // if location is enabled
            private boolean isLocationEnabled() {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }

            // If everything is alright then
            @Override
            public void
            onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

                if (requestCode == PERMISSION_ID) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        getLastLocation();
                    }
                }
            }

            @Override
            public void onResume() {
                super.onResume();
                if (checkPermissions()) {
                    getLastLocation();
                }
            }



}