package project.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Editar extends AppCompatActivity implements View.OnClickListener {

    private EditText et_usuario,et_password,et_nombre,et_apellido,et_direccion;
    private Button btnActualizar,btnCancelar;
    private String password,nombre,apellido,direccion;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        et_usuario=(EditText)findViewById(R.id.EditUser);
        et_password=(EditText)findViewById(R.id.EditPass);
        et_nombre=(EditText)findViewById(R.id.EditNombre);
        et_apellido=(EditText)findViewById(R.id.EditApellido);
        et_direccion=(EditText)findViewById(R.id.EditDireccion);
        btnActualizar=(Button)findViewById(R.id.btnEditRegistrar);
        btnCancelar=(Button)findViewById(R.id.btnEditCancelar);
        obtenerInfoDB();
        et_usuario.setText(usuario);
        btnActualizar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnEditRegistrar:
                String id = mAuth.getCurrentUser().getUid();
                password=(et_password.getText().toString());
                nombre=(et_nombre.getText().toString());
                apellido=(et_apellido.getText().toString());
                direccion=(et_direccion.getText().toString());

                Map<String, Object> datosActualizacion = new HashMap<>();
                datosActualizacion.put("correo",usuario);
                datosActualizacion.put("password",password);
                datosActualizacion.put("nombre",nombre);
                datosActualizacion.put("apellido",apellido);
                datosActualizacion.put("cedula",cedula);
                datosActualizacion.put("edad",edad);
                datosActualizacion.put("estado",genero);
                datosActualizacion.put("genero",estado);
                datosActualizacion.put("direccion",direccion);
                mDatabase.child("Users").child(id).updateChildren(datosActualizacion);
                Toast.makeText(this,"Datos actualizados", Toast.LENGTH_LONG).show();
                Intent i=new Intent(this,Inicio.class);
                startActivity(i);
                break;

            case R.id.btnEditCancelar:
                Intent i2=new Intent(this,Inicio.class);
                startActivity(i2);
                break;
        }
    }

    String cedula;
    String edad;
    String genero;
    String estado;
    String usuario;
    private void obtenerInfoDB(){
        String id= mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    usuario=snapshot.child("correo").getValue().toString();
                    cedula= snapshot.child("cedula").getValue().toString();
                    edad=snapshot.child("edad").getValue().toString();
                    genero=snapshot.child("genero").getValue().toString();
                    estado=snapshot.child("estado").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}