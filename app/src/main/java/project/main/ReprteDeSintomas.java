package project.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReprteDeSintomas extends AppCompatActivity  implements View.OnClickListener {
    EditText etOtro;
    CheckBox cbFiebre, cbTos, cbDolor, cbRespiracion, cbOlfato, cbOpecho;
    RadioButton siNo;
    Button enviar;
    int puntaje;
    String otro;
    String estado = "";
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reprte_de_sintomas);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        etOtro = (EditText) findViewById(R.id.etOtros);
        cbFiebre = (CheckBox) findViewById(R.id.cbFiebre);
        cbTos = (CheckBox) findViewById(R.id.cbTos);
        cbDolor = (CheckBox) findViewById(R.id.cbDolor);
        cbRespiracion = (CheckBox) findViewById(R.id.cbRespiracion);
        cbOlfato = (CheckBox) findViewById(R.id.cbOlfato);
        cbOpecho = (CheckBox) findViewById(R.id.cbOPecho);
        siNo = (RadioButton) findViewById(R.id.RegRadbtnSi);
        enviar = (Button) findViewById(R.id.btnEnviarForm);
        enviar.setOnClickListener(this);
        obtenerInfoDB();
    }

    @Override
    public void onClick(View v) {

        //Fecha del sistema
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = dateFormat.format(date) + "";

        otro = (etOtro.getText().toString());

        String id = mAuth.getCurrentUser().getUid();
        Map<String, Object> personaMap = new HashMap<>();
        if (checkEstado().equals("Positivo")) {

            personaMap.put("estado", "Positivo");
            mDatabase.child("Users").child(id).updateChildren(personaMap);

            Toast.makeText(this, "Tome cuarentena", Toast.LENGTH_LONG).show();
            Intent i = new Intent(ReprteDeSintomas.this, Inicio.class);
            startActivity(i);
        } else {
            estado ="Negativo";
            personaMap.put("estado", "Negativo");
            mDatabase.child("Users").child(id).updateChildren(personaMap);

            Toast.makeText(this, "No posee riesgo", Toast.LENGTH_LONG).show();
            Intent i = new Intent(ReprteDeSintomas.this, Inicio.class);
            startActivity(i);
        }

        Map<String, Object> datosInforme = new HashMap<>();
        datosInforme.put("cedula", cedula);
        datosInforme.put("correo", correo);
        datosInforme.put("puntaje", puntaje);
        datosInforme.put("fecha", fecha);
        datosInforme.put("estado", estado);
        mDatabase.child("Informes").child(id).updateChildren(datosInforme);
    }

    public String checkEstado() {
        if (revisarSintomas() >= 4) {
            estado = "Positivo";
        }
        return estado;
    }


    String cedula;
    String correo;
    private void obtenerInfoDB(){
        String id= mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    cedula= snapshot.child("cedula").getValue().toString();
                    correo= snapshot.child("correo").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public  int revisarSintomas(){
        puntaje = 0;

        if(cbFiebre.isChecked() == true){
            puntaje++;
        }
        if(cbTos.isChecked() == true){
            puntaje++;
        }
        if(cbDolor.isChecked() == true){
            puntaje++;
        }
        if(cbRespiracion.isChecked() == true){
            puntaje++;
        }
        if(cbOlfato.isChecked() == true){
            puntaje++;
        }
        if(cbOpecho.isChecked() == true){
            puntaje++;
        }
        if(siNo.isChecked() == true){
           puntaje= puntaje+4;
        }
        return puntaje;
    }

}