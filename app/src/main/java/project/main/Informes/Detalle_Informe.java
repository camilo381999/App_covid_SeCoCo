package project.main.Informes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import project.main.Editar;
import project.main.Envio_correo;
import project.main.Inicio;
import project.main.Mostrar;
import project.main.R;
import project.main.Ubicacion;

public class Detalle_Informe extends AppCompatActivity implements View.OnClickListener {

    private TextView nombreUsuarioInfor;
    private TextView cedulaUsuarioInfor;
    private TextView estadoUsuarioInfor;
    private TextView fechaReporteUsuarioInfor;
    private TextView edadUsuarioInfor;
    private TextView genUsuarioInfor;

    private Informe itemDetail;

    public  Informe correo=new Informe();

    private Button btnAtras,btnCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle__informe);
        setTitle(getClass().getSimpleName());

        btnAtras = (Button) findViewById(R.id.btnAtrasInfor);
        btnAtras.setOnClickListener( this);

        btnCorreo = (Button) findViewById(R.id.btnAsignarCita);
        btnCorreo.setOnClickListener( this);


        initViews();
        initValues();

    }

    private void initViews() {
        nombreUsuarioInfor=findViewById(R.id.nombreUsuarioInfor);
        cedulaUsuarioInfor=findViewById(R.id.cedulaUsuarioInfor);
        estadoUsuarioInfor=findViewById(R.id.estadoUsuarioInfor);
        fechaReporteUsuarioInfor=findViewById(R.id.fechaReporteUsuarioInfor);
        edadUsuarioInfor=findViewById(R.id.edadUsuarioInfor);
        genUsuarioInfor=findViewById(R.id.genUsuarioInfor);



    }

    private void initValues() {
        itemDetail= (Informe) getIntent().getExtras().getSerializable("itemDetail");
        nombreUsuarioInfor.setText(itemDetail.getCorreo());
        cedulaUsuarioInfor.setText(itemDetail.getCedula());
        estadoUsuarioInfor.setText(itemDetail.getEstado());
        fechaReporteUsuarioInfor.setText(itemDetail.getFecha());
        edadUsuarioInfor.setText(itemDetail.getEdad());
        genUsuarioInfor.setText(itemDetail.getGenero());

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAtrasInfor:
                Intent i1 = new Intent(Detalle_Informe.this, Mostrar.class);
                startActivity(i1);
                break;

            case R.id.btnAsignarCita:
                Intent i2 = new Intent(Detalle_Informe.this, Envio_correo.class);
                startActivity(i2);
                break;

        }
        }
/*
    public String getCorreoEnvio() {
        return correoEnvio;
    }

    public void setCorreoEnvio(String correoEnvio) {
        this.correoEnvio = correoEnvio;
    }*/

}