package project.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import project.main.Informes.Detalle_Informe;
import project.main.Informes.Informe;
import project.main.Informes.InformeAdapter;

public class Mostrar extends AppCompatActivity implements View.OnClickListener {

    private InformeAdapter iAdapter;
    private RecyclerView nRecyclerView;
    private ArrayList<Informe> nInformesList=new ArrayList<>();
    private DatabaseReference nDatabase;
    private FirebaseAuth nAuth;
    private Button btnAtras;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar);

        btnAtras = (Button) findViewById(R.id.btnAtrasMost);
        btnAtras.setOnClickListener( this);

        nDatabase= FirebaseDatabase.getInstance().getReference();
        nAuth = FirebaseAuth.getInstance();

        nRecyclerView=(RecyclerView)findViewById(R.id.recyclerViewInforme);
        nRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getInformeFromFirebase();

    }

    private void getInformeFromFirebase(){
        nDatabase.child("Informes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    nInformesList.clear();
                    for (DataSnapshot ds: snapshot.getChildren()){
                        String cedula =ds.child("cedula").getValue().toString();
                        String correo =ds.child("correo").getValue().toString();
                        String estado =ds.child("estado").getValue().toString();
                        String fecha =ds.child("fecha").getValue().toString();
                        String puntaje =ds.child("puntaje").getValue().toString();
                        String edad=ds.child("edad").getValue().toString();
                        String genero=ds.child("genero").getValue().toString();
                        String direccion=ds.child("direccion").getValue().toString();

                        nInformesList.add(new Informe(cedula,correo,estado,fecha,puntaje,edad,genero,direccion));
                    }
                    iAdapter =new InformeAdapter(nInformesList,R.layout.informe);
                    nRecyclerView.setAdapter(iAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAtrasMost:
                Intent i1 = new Intent(Mostrar.this, LoginAdmin.class);
                startActivity(i1);
                break;
    }
}
}