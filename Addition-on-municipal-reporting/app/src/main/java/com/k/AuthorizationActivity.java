package com.k;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AuthorizationActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnExit, btnReg;
    EditText editTextLogin, editTextPass;

    DBHelper dbHelper;
    SQLiteDatabase db;

    private static final String LOG_TAG = "myLogs";
    public final static String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        editTextLogin = (EditText) findViewById(R.id.editText_login);
        editTextPass = (EditText) findViewById(R.id.editText_pass);
        btnExit = (Button) findViewById(R.id.button_exit);
        btnReg = (Button) findViewById(R.id.button_registration);

        btnExit.setOnClickListener(this);
        btnReg.setOnClickListener(this);


        dbHelper = new AuthorizationActivity.DBHelper(this);
    }

    @Override
    public void onClick(View v) {

        // создаем объект для данных
        Log.d(LOG_TAG, "создаем объект для данных");
        ContentValues cv;

        Cursor c;
        String selection = null,
                userRes = "",
                checkEditLogin = editTextLogin.getText().toString(),
                checkEditPass = editTextPass.getText().toString();
        String[] selectionArgs = null;
        Integer checkLogin = 0;

        // подключаемся к БД
        Log.d(LOG_TAG, "подключаемся к БД");
        db = dbHelper.getWritableDatabase();


        switch (v.getId()) {
            case R.id.button_exit:
                // TODO Call second activity

                if(checkEditLogin.equalsIgnoreCase("") ||
                        checkEditPass.equalsIgnoreCase("") ){
                    Toast.makeText(this, "Заповніть поля!", Toast.LENGTH_LONG).show();
                    // закрываем подключение к БД
                    dbHelper.close();
                    break;
                }
                else {
                    Log.d(LOG_TAG, "--- Check in table_util: ---");

                    selection = "login = ? AND pass = ?";
                    selectionArgs = new String[] { checkEditLogin, checkEditPass };

                    // делаем запрос всех данных из таблицы table_util, получаем Cursor
                    c = db.query("table_user", null, selection, selectionArgs, null, null, null);

                    // ставим позицию курсора на первую строку выборки
                    // если в выборке нет строк, вернется false
                    if (c.moveToFirst()) {
                        do {
                            checkLogin += 1;
                        } while (c.moveToNext());
                    } else{
                        checkLogin = 0;
                    }
                    c.close();
                    Log.d(LOG_TAG, "--- Check " + String.valueOf(checkLogin) + ": ---");

                    if(c.getCount() == 0){
                        Log.d(LOG_TAG, "Користувачів в БД не існує!!!");
                        Toast.makeText(this, "Користувачів в БД не існує!!!", Toast.LENGTH_LONG).show();
                        // закрываем подключение к БД
                        dbHelper.close();
                        break;
                    }
                    else if(checkLogin > 0){
                        Log.d(LOG_TAG, "Ви перейшли у головне меню!!!");
                        Toast.makeText(this, "Ви перейшли у головне меню!!!", Toast.LENGTH_LONG).show();
                        // закрываем подключение к БД
                        dbHelper.close();
                        Intent intentExit = new Intent(this, MainActivity.class);
                        intentExit.putExtra(EXTRA_MESSAGE, checkEditLogin);
                        startActivity(intentExit);
                        break;
                    }
                    else {
                        Log.d(LOG_TAG, "Такий Логін вже існує!!!\nВедіть новий Логін!!!");
                        Toast.makeText(this, "Такий Логін вже існує!!!\nВедіть новий Логін!!!", Toast.LENGTH_LONG).show();
                        // закрываем подключение к БД
                        dbHelper.close();
                        break;
                    }
                }
            case R.id.button_registration:
                // TODO Call second activity

                Toast.makeText(this, "Нажата кнопка Добавити", Toast.LENGTH_LONG).show();

                if(checkEditLogin.equalsIgnoreCase("") ||
                        checkEditPass.equalsIgnoreCase("")){
                    Toast.makeText(this, "Заповніть поля!", Toast.LENGTH_LONG).show();
                    // закрываем подключение к БД
                    dbHelper.close();
                    break;
                }
                else {
                    Log.d(LOG_TAG, "--- Insert in table_util: ---");

                    int loginColIndex = 0, passColIndex = 0;

                    selection = "login = ?";
                    selectionArgs = new String[] { checkEditLogin };

                    Log.d(LOG_TAG, "--- Insert checkEditLogin: " + checkEditLogin + " - " + checkEditPass + " ---");
                    Log.d(LOG_TAG, "--- Insert selectionArgs: ---");
                    // делаем запрос всех данных из таблицы table_util, получаем Cursor
                    c = db.query("table_user", null, selection, selectionArgs, null, null, null);

                    Log.d(LOG_TAG, "--- Insert c = db.query: ---");
                    Log.d(LOG_TAG, "--- Insert c.getCount(): " + String.valueOf(c.getCount()) + " ---");
                    // ставим позицию курсора на первую строку выборки
                    // если в выборке нет строк, вернется false
                    if(c.getCount() != 0){
                        if (c.moveToFirst()) {
                            Log.d(LOG_TAG, "--- Insert c.moveToFirst(): ---");
                            loginColIndex = c.getColumnIndex("login");
                            passColIndex = c.getColumnIndex("pass");
                            do {
                                checkLogin = 1;
                                Log.d(LOG_TAG, "--- Insert checkLogin = 1: ---");
                            } while (c.moveToNext());
                        } else{
                            checkLogin = 0;
                            userRes = c.getString(loginColIndex) +
                                    "  -  " + c.getString(passColIndex);
                            // получаем значения по номерам столбцов и пишем все в лог
                            Log.d(LOG_TAG, userRes);
                        }
                    }
                    c.close();
                    Log.d(LOG_TAG, "--- Insert c.close(): ---");
                    //checkLogin.equals(oneLogin)
                    if(checkLogin == 1){
                        Log.d(LOG_TAG, "Такий користувач вже існує!!!");
                        Toast.makeText(this, "Такий користувач вже існує!!!", Toast.LENGTH_LONG).show();
                        // закрываем подключение к БД
                        dbHelper.close();
                        break;
                    }
                    else {
                        cv = new ContentValues();

                        // подготовим данные для вставки в виде пар: наименование столбца - значение
                        cv.put("login", checkEditLogin);
                        cv.put("pass", checkEditPass);

                        // вставляем запись и получаем ее ContentValues
                        long rowID = db.insert("table_user", null, cv);
                        Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                        Toast.makeText(this, "Запис добавлений!", Toast.LENGTH_LONG).show();

                        // закрываем подключение к БД
                        dbHelper.close();
                        Intent intentReg = new Intent(this, MainActivity.class);
                        startActivity(intentReg);
                        break;
                    }
                }
            default:
                break;
        }
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "DB_Users", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table table_user ("
                    + "id integer primary key autoincrement,"
                    + "login text,"
                    + "pass text" + ");"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
