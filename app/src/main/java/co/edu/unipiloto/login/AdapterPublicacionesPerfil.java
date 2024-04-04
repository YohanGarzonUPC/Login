package co.edu.unipiloto.login;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterPublicacionesPerfil extends RecyclerView.Adapter<AdapterPublicacionesPerfil.PublicacionesPerfilViewHolder> {

    Context context;
    List<Publicacion> listaPublicaciones;

    private OnConductorButtonClickListener conductorButtonClickListener;
    public interface OnConductorButtonClickListener{
        void onConductorButtonClick(int position);
    }
    public AdapterPublicacionesPerfil(Context context, List<Publicacion> listaPublicaciones) {
        this.context = context;
        this.listaPublicaciones = listaPublicaciones;
    }
    @NonNull
    @Override
    public AdapterPublicacionesPerfil.PublicacionesPerfilViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_perfil_publicaciones,parent,false);
        return new PublicacionesPerfilViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPublicacionesPerfil.PublicacionesPerfilViewHolder holder, int position) {
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

    public class PublicacionesPerfilViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvOrigen,tvDestino, tvPeso, tvDescripcion, tvFecha;
        public PublicacionesPerfilViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombre=itemView.findViewById(R.id.tvNombre);
            tvOrigen = itemView.findViewById(R.id.tvOrigen);
            tvDestino= itemView.findViewById(R.id.tvDestino);
            tvPeso = itemView.findViewById(R.id.tvPeso);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvFecha = itemView.findViewById(R.id.tvFecha);
        }
    }

    public void updateData(List<Publicacion> newData) {
        Log.d("Adapter", "Actualizando datos. Nueva cantidad de publicaciones: " + newData.size());
        this.listaPublicaciones = newData;
        notifyDataSetChanged();
    }


}
