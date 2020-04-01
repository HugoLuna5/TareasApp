package lunainc.com.mx.notasapp.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import lunainc.com.mx.notasapp.model.Nota;

public class DbHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "notas_app";
    private static final String TABLE_NOTAS = "notas";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_STATUS = "status";


    public DbHandler(Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NOTAS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_STATUS + " TEXT"+ ")";
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTAS);
        // Create tables again
        onCreate(db);
    }


    /**
     * Crear una nueva nota en la base de datos
     * @param name
     * @param description
     */
    public void insertNotas(String name, String description){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("status", "incomplete");


        long newRow = db.insert(TABLE_NOTAS, null, contentValues);
        db.close();


    }

    public ArrayList<Nota> getAllNotas(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Nota> notasList = new ArrayList<>();

        String query = "SELECT * FROM "+TABLE_NOTAS+ " ORDER BY "+KEY_ID+" DESC";
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(query, null);


        while(cursor.moveToNext()){

            Nota  nota = new Nota(
                    cursor.getString(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(KEY_STATUS))
            );

            notasList.add(nota);

        }


        return notasList;
    }



    public void deleteNota(String nota_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTAS, KEY_ID+" = ?", new String[]{nota_id});
        db.close();
    }

    public int deleteAllNotas(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete (TABLE_NOTAS, String.valueOf (1), null);
    }


    public int updateNota(String nota_id, String name, String description, String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("status", status);

        return db.update(TABLE_NOTAS, contentValues, KEY_ID+" = ?",new String[]{nota_id});
    }

    public int updateStatus(String status, String nota_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        return db.update(TABLE_NOTAS, contentValues, KEY_ID+" = ?",new String[]{nota_id});
    }




}
