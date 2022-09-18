package thiago.neves.appGaleria;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    MainAdapter mainAdapter;
    // lista das fotos salvas na pasta Pictures
    List<String> photos = new ArrayList<>();

    // atributos usados para Intent da câmera
    static int RESULT_TAKE_PICTURE = 1;
    String currentPhotoPath;

    // atributos usados para pedir permissão à câmera do celular
    static int RESULT_REQUEST_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // deleta todas as imagens já salvas em Pictures
        // File picturesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // deleteRecursive(picturesDir);

        // definindo toolbar para ser actionBar padrão
        Toolbar toolbar = findViewById(R.id.tbMain);
        setSupportActionBar(toolbar);

        // checa pelas permissões de câmera da app
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.CAMERA);
        checkForPermissions(permissions);

        // acessa o diretório "Pictures", já padrão das app Android
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


    // método para debugging
    // deleta tudo dentro de um diretório
    private void deleteRecursive(File fileOrDir) {
        if (fileOrDir.isDirectory()) {
            for (File child : fileOrDir.listFiles()) {
                deleteRecursive(child);
            }
        }
        fileOrDir.delete();
    }


    private void checkForPermissions(List<String> permissions) {
        List<String> permissionsNotGranted = new ArrayList<>();

        for (String permission : permissions) {
            if (!hasPermission(permission)) {
                permissionsNotGranted.add(permission);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsNotGranted.size() > 0) {
                requestPermissions(permissionsNotGranted.toArray( new String[permissionsNotGranted.size()] ), RESULT_REQUEST_PERMISSION);
            }
        }
    }


    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_GRANTED;
        }

        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        final List<String> permissionsRejected = new ArrayList<>();
        if (requestCode == RESULT_REQUEST_PERMISSION) {
            for (String permission : permissions) {
                if (!hasPermission(permission)) {
                    permissionsRejected.add(permission);
                }
            }
        }

        if (permissionsRejected.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Para usar essa app é preciso conceder essas permissões")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissionsRejected.toArray( new String[permissionsRejected.size()] ), RESULT_REQUEST_PERMISSION);
                                }
                            }).create().show();
                }
            }
        }
    }

    private void dispatchTakePictureIntent() {
        File emptyFile = null;
        try {
            // tenta criar um arquivo vazio para a imagem que será tirada pela câmera
            emptyFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "Não foi possível criar o arquivo", Toast.LENGTH_SHORT).show();
            return;
        }
        currentPhotoPath = emptyFile.getAbsolutePath();

        if (emptyFile != null) {
            // pega uma URI do arquivo vazio criado com o fileProvider para que a câmera possa escrever nesse arquivo
            Uri fileUri = FileProvider.getUriForFile(MainActivity.this, "thiago.neves.appGaleria.fileprovider", emptyFile);

            // intent que abre a câmera para tirar uma foto, enviando juntamente a URI do fileProvider
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(i, RESULT_TAKE_PICTURE);
        }
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format( new Date() );
        // imagem é salva com extensão da imagem + timestamp em que foi tirada
        String imageFileName = "JPEG_" + timeStamp;

        // recupera o diretório padrão do android Picture
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File f = File.createTempFile(imageFileName, ".jpg", storageDir);

        return f;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // executado quando após a câmera retornar sus resposta (foto ou cancelar)
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_TAKE_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                // adiciona a foto na lista de fotos da recyclerView e notifica essa alteração ao adapter
                photos.add(currentPhotoPath);
                mainAdapter.notifyItemInserted(photos.size()-1);
            } else {
                // caso o usuário tenha cancelado a ação da câmera, deleta o arquivo de imagem temporário criado em Pictures
                File f = new File(currentPhotoPath);
                f.delete();
            }
        }
    }


    public void startPhotoActivity(String photoPath) {
        Intent i = new Intent(MainActivity.this, PhotoActivity.class);
        i.putExtra("photo_path", photoPath);
        startActivity(i);
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
            // caso seja o id do item de menu da camêra, intent para abrir camêra é executado
            case R.id.opCamera:
                dispatchTakePictureIntent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}