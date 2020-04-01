package lunainc.com.mx.notasapp.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import lunainc.com.mx.notasapp.R;
import lunainc.com.mx.notasapp.adapter.NoteAdapter;
import lunainc.com.mx.notasapp.db.DbHandler;
import lunainc.com.mx.notasapp.model.Nota;
import lunainc.com.mx.notasapp.utils.NotificationUtils;

public class DetailActivity extends AppCompatActivity {


    @BindView(R.id.parentContainer)
    RelativeLayout parentContainer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nameTask)
    TextInputEditText nameTask;

    @BindView(R.id.descTask)
    TextInputEditText descTask;

    @BindView(R.id.statusSwitch)
    SwitchMaterial statusSwitch;

    @BindView(R.id.btnAdd)
    FloatingActionButton btnAdd;

    private DbHandler db;

    private String idTaskStr = "";
    private String nameTaskStr = "";
    private String descTaskStr = "";
    private String statusTaskStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        db = new DbHandler(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
        setData();
        events();


    }

    public void getData(){
        idTaskStr = getIntent().getStringExtra("idTask");
        nameTaskStr = getIntent().getStringExtra("nameTask");
        descTaskStr = getIntent().getStringExtra("descTask");
        statusTaskStr = getIntent().getStringExtra("statusTask");
    }

    @SuppressLint("SetTextI18n")
    public void setData(){
        toolbar.setTitle(nameTaskStr.toUpperCase());

        nameTask.setText(nameTaskStr);
        descTask.setText(descTaskStr);


        if (statusTaskStr.equals("complete")){
            statusSwitch.setChecked(true);
        }


    }


    public void events(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String statusFinal = "incomplete";
                if (statusSwitch.isChecked()){
                    statusFinal = "complete";
                }

                String newName = nameTask.getText().toString();
                String newDesc= descTask.getText().toString();

                if (newName.trim().length() > 0 && newDesc.trim().length() > 0){


                    db.updateNota(idTaskStr, newName, newDesc, statusFinal);
                    new NotificationUtils().showBotification(DetailActivity.this, "Tarea actualizada", "Se ha actualizado una tarea con exito");


                }else{
                    new NotificationUtils().showBotification(DetailActivity.this, "Tarea no actualizada", "No se actualizo su tarea, ya que no lleno todos los campos requeridos.");

                }



            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_delete:

                alertDeleteItem(idTaskStr);

                return true;
            case R.id.action_info:
                Snackbar.make(parentContainer, "Desarrollada por Hugo Dario Luna Cruz", Snackbar.LENGTH_LONG)
                        .show();

                return true;

            case android.R.id.home:
                goToBack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }





    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        goToBack();


    }


    public void alertDeleteItem(String nota_id){

        new AlertDialog.Builder(this)
                .setTitle(R.string.title_alert_dialog)
                .setMessage(R.string.message_alert_dialog)
                .setPositiveButton(R.string.button_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        db.deleteNota(nota_id);
                        new NotificationUtils().showBotification(DetailActivity.this, "Tarea "+nameTaskStr+" eliminada", "Se ha eliminado la tarea "+nameTaskStr+" con exito");
                        goToBack();
                    }
                })
                .setNegativeButton(R.string.button_negative, null)
                .setIcon(R.drawable.ic_alert)
                .show();

    }


    public void goToBack(){
        Intent intent = new Intent(DetailActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }



}
