package project.main;

import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Ubicacion extends FragmentActivity implements OnMapReadyCallback {

    private int MY_PERMISSION_REQUEST_READ_CONTACTS;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
       mDatabase = FirebaseDatabase.getInstance().getReference();
        subirLatLongFirebase();

        setContentView(R.layout.activity_ubicacion);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    double a;
    double b;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(a, b);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Ubicación"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void subirLatLongFirebase() {
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
                            a=location.getLatitude();
                            b=location.getLongitude();
                            Toast.makeText(Ubicacion.this, "latitud: "+a+"Longitud: "+b, Toast.LENGTH_LONG).show();
                            //Se meten las coordenadas a la base de datos
                            Map<String,Object> latLang = new HashMap<>();
                            latLang.put("latitud",a);
                            latLang.put("longitud",b);
                            mDatabase.child("Ubicacion").push().setValue(latLang);

                            //aca se llama a maps
                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.map);
                            mapFragment.getMapAsync(Ubicacion.this);
                            //Log.e("latitud: ",location.getLatitude()+"Longitud: "+location.getLongitude());
                        }
                    }
                });
    }
}