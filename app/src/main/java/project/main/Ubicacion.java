package project.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ubicacion extends FragmentActivity implements OnMapReadyCallback {

    private int MY_PERMISSION_REQUEST_READ_CONTACTS;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    LocationRequest locationRequest;

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null){
                return;
            }
            for (Location location:locationResult.getLocations()){
                Log.e("Ubicacion",location.toString());
                String id = mAuth.getCurrentUser().getUid();
                a=location.getLatitude();
                b=location.getLongitude();
                Toast.makeText(Ubicacion.this,
                        "latitud: "+a+"Longitud: "+b, Toast.LENGTH_LONG).show();

                //Se meten las coordenadas a la base de datos

                Map<String,Object> latLang = new HashMap<>();
                latLang.put("latitud",a);
                latLang.put("longitud",b);
                latLang.put("estado",estado);
                //mDatabase.child("Ubicacion").push().setValue(latLang);
                mDatabase.child("Ubicacion").child(id).updateChildren(latLang);

                //aca se llama a maps
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(Ubicacion.this);
            }
        }
    };

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    boolean resetViewMap=true;
    double a;
    double b;
    String estado;
    private ArrayList<Marker> tmpRealTimeMarkers = new ArrayList<Marker>();
    private ArrayList<Marker> realTimeMarkers = new ArrayList<Marker>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(20000);
        locationRequest.setFastestInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        obtenerInfoDB();

        //subirLatLongFirebase();


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            checkSettingsAndStartLocationUpdates();
        } else {
            askLocationPermission();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    private void checkSettingsAndStartLocationUpdates() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                //Setting of device are satisfied and we can start location updates
                startLocationUpdates();
            }
        });

        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    try {
                        apiException.startResolutionForResult(Ubicacion.this, 1001);
                    } catch (IntentSender.SendIntentException sendIntentException) {
                        sendIntentException.printStackTrace();
                    }
                }
            }
        });
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates(){
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void askLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Ubicacion.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_REQUEST_READ_CONTACTS);
            return;
        }
        checkSettingsAndStartLocationUpdates();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(resetViewMap==true) {
            Log.e("CONDICION_DE_UNA_VEZ","entra en condicional");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(4.647463, -74.098013), (float) 10.0));
        }
        resetViewMap=false;
        mDatabase.child("Ubicacion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                for (Marker marker: realTimeMarkers){
                    marker.remove();
                }

                for (DataSnapshot snapshot: datasnapshot.getChildren()){
                    Maps1 mp = snapshot.getValue(Maps1.class);
                    Double latitud = mp.getLatitud();
                    Double longitud = mp.getLongitud();
                    String estadoUs = mp.getEstado();

                    if(estadoUs.equals("Negativo")){
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(new LatLng(latitud,longitud));
                        tmpRealTimeMarkers.add(mMap.addMarker
                                (markerOptions.icon(BitmapDescriptorFactory.
                                        defaultMarker(BitmapDescriptorFactory.HUE_GREEN))));
                    }   else if (estadoUs.equals("admin")){
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(new LatLng(latitud,longitud));
                        tmpRealTimeMarkers.add(mMap.addMarker
                                (markerOptions.icon(BitmapDescriptorFactory.
                                        defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));
                    }else {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(new LatLng(latitud, longitud));
                        tmpRealTimeMarkers.add(mMap.addMarker
                                (markerOptions.icon(BitmapDescriptorFactory.
                                        defaultMarker(BitmapDescriptorFactory.HUE_RED))));
                    }


                }
                realTimeMarkers.clear();
                realTimeMarkers.addAll(tmpRealTimeMarkers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*private void subirLatLongFirebase() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Ubicacion.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_REQUEST_READ_CONTACTS);

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            String id = mAuth.getCurrentUser().getUid();
                            a=location.getLatitude();
                            b=location.getLongitude();
                            Toast.makeText(Ubicacion.this,
                                    "latitud: "+a+"Longitud: "+b, Toast.LENGTH_LONG).show();

                            //Se meten las coordenadas a la base de datos

                            Map<String,Object> latLang = new HashMap<>();
                            latLang.put("latitud",a);
                            latLang.put("longitud",b);
                            latLang.put("estado",estado);
                            //mDatabase.child("Ubicacion").push().setValue(latLang);
                            mDatabase.child("Ubicacion").child(id).updateChildren(latLang);

                            //aca se llama a maps
                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.map);
                            mapFragment.getMapAsync(Ubicacion.this);
                        }
                    }
                });
    }*/


    private void obtenerInfoDB(){
        String id= mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    estado=snapshot.child("estado").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


}