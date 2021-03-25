package project.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText user, pass;
    private Button btnEntrar, btnRegistrar;

    private String usuario ="";
    private String contraseña ="";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user=(EditText)findViewById(R.id.User);
        pass=(EditText)findViewById(R.id.Pass);
        btnEntrar=(Button)findViewById(R.id.btnEntrar);
        btnRegistrar=(Button)findViewById(R.id.btnRegistrar);

        mAuth = FirebaseAuth.getInstance();

        btnEntrar.setOnClickListener(this);
        btnRegistrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        usuario=user.getText().toString();
        contraseña=pass.getText().toString();
        switch (v.getId()){

            case R.id.btnEntrar:

                if((!usuario.equals("")) && (!contraseña.equals(""))){

                    if(usuario.equals("administrador@gmail.com") && contraseña.equals("123456")){
                        Toast.makeText(this,"Administrador", Toast.LENGTH_LONG).show();
                        loginAdmin();
                    }else
                    loginUser();
                }else {
                    Toast.makeText(this,"Error: Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
                }

                /**/

                break;

            case R.id.btnRegistrar:
                i=new Intent(MainActivity.this,Registrar.class);
                startActivity(i);
                break;
        }

    }

    private void loginUser(){
    mAuth.signInWithEmailAndPassword(usuario,contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()){
                startActivity(new Intent(MainActivity.this,Inicio.class));
                finish();
            }else {
                Toast.makeText(MainActivity.this, "Error: Usuario o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        }
    });
    }

    private void loginAdmin(){
        mAuth.signInWithEmailAndPassword(usuario,contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(MainActivity.this,LoginAdmin.class));
                    finish();
                }else {
                    Toast.makeText(MainActivity.this, "Error: Usuario o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}