package thiago.neves.appGaleria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<String> photos = new ArrayList<>();
    MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setando toolbar para ser actionBar padrão
        Toolbar toolbar = findViewById(R.id.tbMain);
        setSupportActionBar(toolbar);

        // acessa o diretório Pictures
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // acessa as imagens já salvas na pasta
        File[] files = dir.listFiles();

        // adiciona o caminho absoluto da imagem à arrayList de photos
        for(int i = 0; i < files.length; i++) {
            photos.add(files[i].getAbsolutePath());
        }

        mainAdapter = new MainAdapter(MainActivity.this, photos);

        RecyclerView rvGallery = findViewById(R.id.rvGallery);
        rvGallery.setAdapter(mainAdapter);

        // largura de cada coluna da grid
        float w = getResources().getDimension(R.dimen.itemWidth);
        // número de colunas da recyclerView Grid
        int numberOfColumns = Utils.calculateNoOfColumns(MainActivity.this, w);
        // layout manager de grid para a recyclerView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, numberOfColumns);
        rvGallery.setLayoutManager(gridLayoutManager);

    }

    // preenche a toolbar com os items definidos nos arquivos em res/menu
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.main_activity_tb, menu);
        return true;
    }

    // método chamado sempre que um item da Toolbar é selecionado
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // pega o id do item
        switch (item.getItemId()) {
            // caso seja o id do menu item da camêra, intent para abrir camêra é executado
            case R.id.opCamera:
                // dispatchTakePictureIntent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}