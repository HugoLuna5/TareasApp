package lunainc.com.mx.notasapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import lunainc.com.mx.notasapp.R;
import lunainc.com.mx.notasapp.db.DbHandler;
import lunainc.com.mx.notasapp.utils.NotificationUtils;

public class CreateNoteActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nameTask)
    TextInputEditText nameTask;

    @BindView(R.id.descTask)
    TextInputEditText descTask;

    @BindView(R.id.actionBtn)
    FloatingActionButton btnAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }


    @Override
    protected void onStart() {
        super.onStart();

        events();

    }


    public void events(){

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameTask.getText().toString();
                String desc = descTask.getText().toString();


                if (name.trim().length() > 0 && desc.trim().length() > 0){

                    DbHandler dbHandler = new DbHandler(CreateNoteActivity.this);
                    dbHandler.insertNotas(name, desc);

                    new NotificationUtils().showBotification(CreateNoteActivity.this, "Tarea creada", "Se agrego con exito una nueva tarea.");
                    goToBack();

                }else{
                    new NotificationUtils().showBotification(CreateNoteActivity.this, "Tarea no creada", "No se agrego su tarea, ya que no lleno todos los campos requeridos.");

                }

            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        /**
         * handle home button pressed
         */
        if (id == android.R.id.home) {

            //Start your main activity here
            goToBack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        goToBack();


    }





    public void goToBack(){
        Intent intent = new Intent(CreateNoteActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
