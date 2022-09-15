package thiago.neves.appGaleria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class PhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        // define toolbar para ser actionBar padrão
        Toolbar toolbar = findViewById(R.id.tbMain);
        setSupportActionBar(toolbar);

        // adiciona seta para voltar à MainActivity na toolbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    // preenche a toolbar com os items definidos nos arquivos em res/menu
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.photo_activity_tb, menu);
        return true;
    }

    // método chamado sempre que um item da Toolbar é selecionado
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // pega o id do item
        switch (item.getItemId()) {
            // caso seja o id do menu item de share, intent para compartilhar imagem é executado
            case R.id.opShare:
                // sharePhoto();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}