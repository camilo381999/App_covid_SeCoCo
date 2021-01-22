package project.main.Informes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.ArrayUtils;

import java.util.ArrayList;

import project.main.R;

public class InformeAdapter extends RecyclerView.Adapter<InformeAdapter.ViewHolder> {

    private int resourse;
    private ArrayList<Informe> informesList;
    private ArrayList<String> informeList2;

    public InformeAdapter(ArrayList<Informe> informesList,int resourse){
        this.informesList=informesList;
        this.resourse=resourse;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resourse,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Informe informes=informesList.get(position);
        holder.textViewInformeCed.setText(informes.getCedula());
        holder.textViewInformeCor.setText(informes.getCorreo());
        holder.textViewInformeEst.setText(informes.getEstado());
        holder.textViewInformeFec.setText(informes.getFecha());
        holder.textViewInformePun.setText(informes.getPuntaje());

    }

    @Override
    public int getItemCount() {
        return informesList.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewInformeCed;
        private TextView textViewInformeCor;
        private TextView textViewInformeEst;
        private TextView textViewInformeFec;
        private TextView textViewInformePun;

        public View view;
        public ViewHolder(View view ){
            super(view);
            this.view=view;
            this.textViewInformeCed=(TextView)view.findViewById(R.id.textViewInformeCed);
            this.textViewInformeCor=(TextView)view.findViewById(R.id.textViewInformeCor);
            this.textViewInformeEst=(TextView)view.findViewById(R.id.textViewInformeEst);
            this.textViewInformeFec=(TextView)view.findViewById(R.id.textViewInformeFec);
            this.textViewInformePun=(TextView)view.findViewById(R.id.textViewInformePun);







        }
    }
}
