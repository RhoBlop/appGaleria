package thiago.neves.appGaleria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setando toolbar para ser actionBar padrão
        Toolbar toolbar = findViewById(R.id.tbMain);
        setSupportActionBar(toolbar);
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