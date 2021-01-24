package project.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Inicio extends AppCompatActivity implements View.OnClickListener {

    private Button btnEditar, btnEliminar, btnRepSintomas, btnSalir;
    private FirebaseAuth nAuth;
    private TextView textViewName;
    private DatabaseReference nDatabase;

    double latitud=0;
    double longitud=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);

        nAuth = FirebaseAuth.getInstance();
        nDatabase = FirebaseDatabase.getInstance().getReference();

        btnRepSintomas = (Button) findViewById(R.id.btnReportar);
        btnRepSintomas.setOnClickListener(this);

        btnEliminar = (Button) findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(this);

        btnSalir = (Button) findViewById(R.id.btnSalir);
        btnSalir.setOnClickListener(this);

        btnEditar = (Button) findViewById(R.id.btnEditar);
        btnEditar.setOnClickListener(this);

        textViewName = (TextView) findViewById(R.id.nombreUsuario);
        textViewName.setOnClickListener(this);


        obtenerInfoDB();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEditar:
                Intent i1 = new Intent(Inicio.this, Editar.class);
                startActivity(i1);
                break;

            case R.id.btnEliminar:

                Intent i3 = new Intent(Inicio.this, Ubicacion.class);
                startActivity(i3);
                break;

            case R.id.btnReportar:
                Intent i2 = new Intent(Inicio.this, ReprteDeSintomas.class);
                startActivity(i2);
                break;

            case R.id.btnSalir:
                nAuth.signOut();
                startActivity(new Intent(Inicio.this, MainActivity.class));
                finish();
                break;
        }
    }

    private void obtenerInfoDB() {
        String id = nAuth.getCurrentUser().getUid();
        nDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    //Obtiene valores de la base de datos
                    String name = snapshot.child("nombre").getValue().toString();

                    textViewName.setText(name);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }





}