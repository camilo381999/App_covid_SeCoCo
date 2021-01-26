package project.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Registrar extends AppCompatActivity implements View.OnClickListener {

    EditText etUs,etPass,etNom,etAp,etDir,etCed,etEd;
    RadioButton genero;
    Button reg,can;

    String generoS;
    String usu="";
    String pass="";
    String nomb="";
    String ape="";
    String dire="";
    String cedu="";
    String ed="";
    String estado="Negativo";

    FirebaseAuth mAuth;
    DatabaseReference nDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar);

        mAuth=FirebaseAuth.getInstance();
        nDatabase=FirebaseDatabase.getInstance().getReference();

        etUs=(EditText)findViewById(R.id.RegUser);
        etEd=(EditText)findViewById(R.id.RegEdad);
        etCed=(EditText)findViewById(R.id.RegCedula);
        etDir=(EditText)findViewById(R.id.RegDireccion);
        etNom=(EditText)findViewById(R.id.RegNombre);
        etAp=(EditText)findViewById(R.id.RegApellido);
        etPass=(EditText)findViewById(R.id.RegPass);

        genero=(RadioButton)findViewById(R.id.RegRadbtnH);
        reg=(Button)findViewById(R.id.btnRegRegistrar);
        can=(Button)findViewById(R.id.btnRegCancelar);
        reg.setOnClickListener(this);
        can.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegRegistrar:

                //Firebase

                usu=(etUs.getText().toString());
                pass=(etPass.getText().toString());
                nomb=(etNom.getText().toString());
                ape=(etAp.getText().toString());
                dire=(etDir.getText().toString());
                ed=(etEd.getText().toString());
                cedu=(etCed.getText().toString());

                //Obtener dato radioButton
                if (genero.isChecked()==true){
                    generoS="Hombre";

                }else{
                    generoS="Mujer";
                }


                if (!usu.equals("")&&!pass.equals("")&&!nomb.equals("")&&
                        !ape.equals("")&&!dire.equals("")&&!ed.equals("")&&!cedu.equals("")){

                    if (pass.length()>=6){
                        registerUser();
                    }else {
                        Toast.makeText(this,"La contraseña debe tener 6 caracteres", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(this,"Debe completar la información", Toast.LENGTH_LONG).show();
                }

                 break;

            case R.id.btnRegCancelar:
                Intent i=new Intent(Registrar.this,MainActivity.class);
                startActivity(i);
                break;
        }
    }

    private void registerUser() {
        mAuth.createUserWithEmailAndPassword(usu,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Map<String,Object> map = new HashMap<>();
                    map.put("nombre",nomb);
                    map.put("apellido",ape);
                    map.put("genero",generoS);
                    map.put("cedula",cedu);
                    map.put("edad",ed);
                    map.put("correo",usu);
                    map.put("password",pass);
                    map.put("estado",estado);

                    String id= mAuth.getCurrentUser().getUid(); // Obtiene id que da firebase
                    nDatabase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                startActivity(new Intent(Registrar.this,Inicio.class));
                                finish();
                            }else{
                                Toast.makeText(Registrar.this,"No se crearon los datos :c", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(Registrar.this,"No se pudo registrar este usuario", Toast.LENGTH_LONG).show();
                }
            }});
    }

}