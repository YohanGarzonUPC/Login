package co.edu.unipiloto.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterPublicaciones extends RecyclerView.Adapter<AdapterPublicaciones.PublicacionesViewHolder> {

    Context context;
    List<String> listaPublicaciones = new ArrayList<>();

    public AdapterPublicaciones(Context context, List<String> listaUsuarios) {
        this.context = context;
        this.listaPublicaciones = listaUsuarios;
    }
    @NonNull
    @Override
    public AdapterPublicaciones.PublicacionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_publicaciones,parent,false);
        return new PublicacionesViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPublicaciones.PublicacionesViewHolder holder, int position) {
        holder.tvNombre.setText(listaPublicaciones.get(position));
    }

    @Override
    public int getItemCount() {
        return listaPublicaciones.size();
    }

    public class PublicacionesViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre;
        public PublicacionesViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombre=itemView.findViewById(R.id.tvNombre);
        }
    }
}
