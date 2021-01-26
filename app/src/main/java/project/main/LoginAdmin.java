package project.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class LoginAdmin extends AppCompatActivity  implements View.OnClickListener {


    Button btnMostrarZonas,btnVerUsers,btnSalirAdmin;
    private FirebaseAuth nAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        nAuth=FirebaseAuth.getInstance();

        btnMostrarZonas=(Button)findViewById(R.id.btnZonasAdmin);
        btnMostrarZonas.setOnClickListener(this);

        btnSalirAdmin=(Button)findViewById(R.id.btnSalirAdmin);
        btnSalirAdmin.setOnClickListener(this);

        btnVerUsers=(Button)findViewById(R.id.btnVerUsuarios);
        btnVerUsers.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnZonasAdmin:
                Intent i1=new Intent(LoginAdmin.this,Ubicacion.class);
                startActivity(i1);
                break;

            case R.id.btnVerUsuarios:
                Intent i2=new Intent(LoginAdmin.this,Mostrar.class);
                startActivity(i2);
                break;


            case R.id.btnSalirAdmin:
                nAuth.signOut();
                startActivity(new Intent(LoginAdmin.this,MainActivity.class));
                finish();
                break;
        }
    }

    }
