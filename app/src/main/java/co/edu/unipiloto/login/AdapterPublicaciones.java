package co.edu.unipiloto.login;

import static co.edu.unipiloto.login.R.id.btnAdd;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.internal.markers.KMutableList;

public class AdapterPublicaciones extends RecyclerView.Adapter<AdapterPublicaciones.PublicacionesViewHolder> {

    Context context;
    List<Publicacion> listaPublicaciones;
    Publicacion publicacionSeleccionada;
    OnConductorButtonClickListener conductorButtonClickListener;

    public interface OnConductorButtonClickListener {
        void onConductorButtonClick(Publicacion publicacion);
    }

    public AdapterPublicaciones(Context context, List<Publicacion> listaPublicaciones, Publicacion publicacionSeleccionada) {
        this.context = context;
        this.listaPublicaciones = listaPublicaciones;
        this.publicacionSeleccionada = publicacionSeleccionada;
    }

    @NonNull
    @Override
    public AdapterPublicaciones.PublicacionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_publicaciones,parent,false);
        return new PublicacionesViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPublicaciones.PublicacionesViewHolder holder, int position) {
        Publicacion  publicacion = listaPublicaciones.get(position);
        holder.bind(publicacion); // Llama al método bind para actualizar las vistas
        holder.btnConductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Manejar el clic en el botón conductor
                if (conductorButtonClickListener != null) {
                    conductorButtonClickListener.onConductorButtonClick(publicacion);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaPublicaciones.size();
    }

    public class PublicacionesViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvOrigen, tvDestino, tvPeso, tvDescripcion, tvFecha;
        ImageButton btnConductor;

        public PublicacionesViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvOrigen = itemView.findViewById(R.id.tvOrigen);
            tvDestino = itemView.findViewById(R.id.tvDestino);
            tvPeso = itemView.findViewById(R.id.tvPeso);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            btnConductor = itemView.findViewById(R.id.btnAdd); // Asumiendo que el ID del botón conductor es "btnAdd"
        }

        // Método para asignar una publicación a este ViewHolder y actualizar las vistas
        public void bind(Publicacion publicacion) {
            // Actualizar las vistas con los datos de la publicación
            tvNombre.setText(publicacion.getNombre());
            tvOrigen.setText(publicacion.getOrigen());
            tvDestino.setText(publicacion.getDestino());
            tvPeso.setText(publicacion.getPeso());
            tvDescripcion.setText(publicacion.getDescripcion());
            tvFecha.setText(publicacion.getFecha());

            // Configurar el clic en el botón conductor
            btnConductor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Manejar el clic en el botón conductor
                    if (conductorButtonClickListener != null) {
                        conductorButtonClickListener.onConductorButtonClick(publicacion);
                    }
                }
            });
        }
    }

    public void setOnConductorButtonClickListener(OnConductorButtonClickListener listener) {
        this.conductorButtonClickListener = listener;
    }
}


