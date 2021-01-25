package project.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.Session;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Properties;

import project.main.Informes.Detalle_Informe;

public class Envio_correo extends AppCompatActivity implements View.OnClickListener {


    private Button btnAtrasDeta,btnEnviarCorreo;
    EditText tvMensajeoEnvio,tvCorreo;
    TextView asunto;
    Detalle_Informe dI = new Detalle_Informe();
    String correoEnvio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envio_correo);

        btnAtrasDeta = (Button) findViewById(R.id.btnAtrasEnvio);
        btnAtrasDeta.setOnClickListener( this);

        btnEnviarCorreo = (Button) findViewById(R.id.btnConfirmar);
        btnEnviarCorreo.setOnClickListener( this);

        tvCorreo = (EditText) findViewById(R.id.tvCorreoEnvio);
        tvCorreo.setOnClickListener( this);

        asunto = (TextView) findViewById(R.id.asunto);
        asunto.setOnClickListener( this);

        tvMensajeoEnvio = (EditText) findViewById(R.id.tvMensajeoEnvio);
        tvMensajeoEnvio.setOnClickListener( this);



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnAtrasEnvio:
                Intent i1 = new Intent(Envio_correo.this, Detalle_Informe.class);
                startActivity(i1);
                break;

            case R.id.btnConfirmar:
                sendMail();
                break;
        }

    }

    private void sendMail() {

        String recipientList= tvCorreo.getText().toString();
        String asuntoEn= asunto.getText().toString();
        String message=tvMensajeoEnvio.getText().toString();

        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,recipientList);
        intent.putExtra(Intent.EXTRA_SUBJECT,asuntoEn);
        intent.putExtra(Intent.EXTRA_TEXT,message);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"choose an email client"));

    }
}