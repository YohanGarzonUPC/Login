package co.edu.unipiloto.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterPublicacionesFavoritos extends RecyclerView.Adapter<AdapterPublicacionesFavoritos.PublicacionesViewHolder> {

    private final Context context;
    private final List<Publicacion> listaPublicacionesFavoritas;

    // Constructor
    public AdapterPublicacionesFavoritos(Context context, List<Publicacion> listaPublicacionesFavoritas) {
        this.context = context;
        this.listaPublicacionesFavoritas = listaPublicacionesFavoritas;
    }

    @NonNull
    @Override
    public PublicacionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_publicaciones, parent, false);
        return new PublicacionesViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicacionesViewHolder holder, int position) {
        Publicacion publicacion = listaPublicacionesFavoritas.get(position);
        holder.tvNombre.setText(publicacion.getNombre());
        holder.tvOrigen.setText(publicacion.getOrigen());
        holder.tvDestino.setText(publicacion.getDestino());
        holder.tvPeso.setText(publicacion.getPeso());
        holder.tvDescripcion.setText(publicacion.getDescripcion());
        holder.tvFecha.setText(publicacion.getFecha());
    }

    @Override
    public int getItemCount() {
        return listaPublicacionesFavoritas.size();
    }

    // ViewHolder para las publicaciones
    public class PublicacionesViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvOrigen, tvDestino, tvPeso, tvDescripcion, tvFecha;

        public PublicacionesViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvOrigen = itemView.findViewById(R.id.tvOrigen);
            tvDestino = itemView.findViewById(R.id.tvDestino);
            tvPeso = itemView.findViewById(R.id.tvPeso);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvFecha = itemView.findViewById(R.id.tvFecha);
        }
    }


}

