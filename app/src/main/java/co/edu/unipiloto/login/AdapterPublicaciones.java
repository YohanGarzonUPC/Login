package co.edu.unipiloto.login;

import static co.edu.unipiloto.login.R.id.btnAdd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterPublicaciones extends RecyclerView.Adapter<AdapterPublicaciones.PublicacionesViewHolder> {

    Context context;
    List<Publicacion> listaPublicaciones;

    private OnConductorButtonClickListener conductorButtonClickListener;
    public interface OnConductorButtonClickListener{
        void onConductorButtonClick(int position);
    }
    public AdapterPublicaciones(Context context, List<Publicacion> listaPublicaciones) {
        this.context = context;
        this.listaPublicaciones = listaPublicaciones;
    }
    @NonNull
    @Override
    public AdapterPublicaciones.PublicacionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_publicaciones,parent,false);
        return new PublicacionesViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPublicaciones.PublicacionesViewHolder holder, int position) {
        Publicacion publicacion = listaPublicaciones.get(position);
        holder.tvNombre.setText(publicacion.getNombre());
        holder.tvOrigen.setText(publicacion.getOrigen());
        holder.tvDestino.setText(publicacion.getDestino());
        holder.tvPeso.setText(publicacion.getPeso());
        holder.tvDescripcion.setText(publicacion.getDescripcion());
        holder.tvFecha.setText(publicacion.getFecha());
    }

    @Override
    public int getItemCount() {
        return listaPublicaciones.size();
    }

    public class PublicacionesViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvOrigen,tvDestino, tvPeso, tvDescripcion, tvFecha;
        ImageButton btnConductor;
        public PublicacionesViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombre=itemView.findViewById(R.id.tvNombre);
            tvOrigen = itemView.findViewById(R.id.tvOrigen);
            tvDestino= itemView.findViewById(R.id.tvDestino);
            tvPeso = itemView.findViewById(R.id.tvPeso);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            btnConductor=itemView.findViewById(btnAdd);




            btnConductor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (conductorButtonClickListener!=null){
                        int position = getAdapterPosition();
                        if (position!= RecyclerView.NO_POSITION) {

                            conductorButtonClickListener.onConductorButtonClick(getAdapterPosition());

                        }
                    }
                }
            });
        }
    }
    public void setOnConductorButtonClickListener(OnConductorButtonClickListener listener) {
        this.conductorButtonClickListener = listener;
    }
}
