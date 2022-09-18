package thiago.neves.appGaleria;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter {
    MainActivity mainActivity;
    List<String> photos;

    public MainAdapter(MainActivity mainActivity, List<String> photos) {
        this.mainActivity = mainActivity;
        this.photos = photos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mainActivity);
        // cria uma view com 'molde' em list_item.xml, apenas com os componentes do android vazios
        View v = inflater.inflate(R.layout.list_item, parent, false);

        // viewHolder guarda a view para ser enviada ao onBindViewHolder
        return new PhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        // método recebe a viewHolder inflada pelo onCreateViewHolder e preenche os componentes com dados

        // retorna image view dentro da viewHolder
        ImageView imPhoto = holder.itemView.findViewById(R.id.imItem);

        // retorna as dimensões de largura e altura definidas em values/dimen.xml
        int w = (int) mainActivity.getResources().getDimension(R.dimen.itemWidth);
        int h = (int) mainActivity.getResources().getDimension(R.dimen.itemHeight);

        // retorna bitmap da foto salva na arrayList, já com dimensões ajustadas
        Bitmap bitmap = Utils.getBitmap(photos.get(position), w, h);

        // define o bitmap da imageView
        imPhoto.setImageBitmap(bitmap);

        // clickListener na imageView para que, quando seja clicada, navegue à photoActivity
        imPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.startPhotoActivity(photos.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }
}
