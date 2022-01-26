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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String[] utilities = {
            "Централізоване постачання холодної води",
            "Централізоване водовідведення (Каналізація)",
            "Централізоване постачання гарячої води",
            "Централізоване опалення",
            "Електропостачання",
            "Газопостачання",
            "Вивезення побутових відходів",
            "Утримання будинків і споруд та прибудинкових територій"
    };

    String[] month_mas = {
            "№01 Січень",
            "№02 Лютий",
            "№03 Березень",
            "№04 Квітень",
            "№05 Травень",
            "№06 Червень",
            "№07 Липень",
            "№08 Серпень",
            "№09 Вересень",
            "№10 Жовтень",
            "№11 Листопад",
            "№12 Грудень"
    };
    String[] selectionArgs = null;

    String utilName = "",
            utilDateActv = "",
            utilDateNew = "",
            uitlMonthActv = "",
            uitlMonthNew = "",
            uitlYearActv = "",
            uitlYearNew = "",
            selection = null,
            messageActivity = "";

    ListView listUtil;

    TextView textSpinUtil,
            text_activ_date,
            text_new_date;
    EditText editText_sum;
    Button button_main;
    LinearLayout LinearLayoutTextNew,
                LinearLayoutSpinNew,
                LinearLayoutSum;

    LinearLayout.LayoutParams paramsLayoutHeightMin, paramsLayoutHeightMax;

    ArrayAdapter<String> adapterListUtil;

    ArrayList<String> arrayListUtil;

    private static final String LOG_TAG = "myLogs";

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Получаем сообщение из объекта intent
        Intent intent = getIntent();
        messageActivity = intent.getStringExtra(AuthorizationActivity.EXTRA_MESSAGE);

        textSpinUtil = (TextView) findViewById(R.id.textSpinUtil);
        text_activ_date = (TextView) findViewById(R.id.textView);
        text_new_date = (TextView) findViewById(R.id.textView2);
        editText_sum = (EditText) findViewById(R.id.editText_sum);
        button_main = (Button) findViewById(R.id.button_main);

        LinearLayoutTextNew = (LinearLayout) findViewById(R.id.LinearLayoutTextNew);;
        LinearLayoutSpinNew = (LinearLayout) findViewById(R.id.LinearLayoutSpinNew);;
        LinearLayoutSum = (LinearLayout) findViewById(R.id.LinearLayoutSum);;

        paramsLayoutHeightMin = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0
        );

        paramsLayoutHeightMax = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        LinearLayoutTextNew.setLayoutParams(paramsLayoutHeightMin);
        LinearLayoutSpinNew.setLayoutParams(paramsLayoutHeightMin);
        LinearLayoutSum.setLayoutParams(paramsLayoutHeightMin);

        // находим список
        listUtil = (ListView) findViewById(R.id.list_util);

        // присвоим обработчик кнопке button_main
        button_main.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        int year_date = calendar.get(Calendar.YEAR);

        int i = 1991;
        ArrayList<String> list_year = new ArrayList<String>();
        list_year.add(String.valueOf(i));

        while (i < year_date){
            i++;
            list_year.add(String.valueOf(i));
        }

        // адаптер utilities
        ArrayAdapter<String> adapter_util = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, utilities);
        adapter_util.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner_util = (Spinner) findViewById(R.id.utilities);
        spinner_util.setAdapter(adapter_util);
        // заголовок
        spinner_util.setPrompt("Комунальні послуги:");
        // выделяем элемент
        //spinner_util.setSelection(1);
        // устанавливаем обработчик нажатия
        spinner_util.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                utilName = (String)parent.getItemAtPosition(position);
                //textSpinUtil.setText(utilName);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // адаптер month
        ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, month_mas);
        adapter_month.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner_month_activ = (Spinner) findViewById(R.id.act_month_spin);
        spinner_month_activ.setAdapter(adapter_month);
        // заголовок
        spinner_month_activ.setPrompt("Місяць:");
        // устанавливаем обработчик нажатия
        spinner_month_activ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                int counMonthActv = position + 1;
                if(counMonthActv < 10){
                    uitlMonthActv = "0" + String.valueOf(counMonthActv);
                }
                else {
                    uitlMonthActv = String.valueOf(counMonthActv);
                }
                //text_activ_date.setText(uitlMonthActv);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
    });


        Spinner spinner_month_new = (Spinner) findViewById(R.id.new_month_spin);
        spinner_month_new.setAdapter(adapter_month);
        // заголовок
        spinner_month_new.setPrompt("Місяць:");
        // устанавливаем обработчик нажатия
        spinner_month_new.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                int counMonthNew = position + 1;
                if(counMonthNew < 10){
                    uitlMonthNew = "0" + String.valueOf(counMonthNew);
                }
                else {
                    uitlMonthNew = String.valueOf(counMonthNew);
                }
                //text_new_date.setText(uitlMonthNew);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // адаптер year active
        ArrayAdapter<String> adapter_year_actv = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_year);
        adapter_util.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner_act_year = (Spinner) findViewById(R.id.act_year_spin);
        spinner_act_year.setAdapter(adapter_year_actv);
        // заголовок
        spinner_act_year.setPrompt("Рік:");
        // устанавливаем обработчик нажатия
        spinner_act_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                uitlYearActv = (String)parent.getItemAtPosition(position);
                //text_activ_date.setText(uitlYearActv);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // адаптер year new
        ArrayAdapter<String> adapter_year_new = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_year);
        adapter_util.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner_new_year = (Spinner) findViewById(R.id.new_year_spin);
        spinner_new_year.setAdapter(adapter_year_actv);
        // заголовок
        spinner_new_year.setPrompt("Рік:");
        // устанавливаем обработчик нажатия
        spinner_new_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                uitlYearNew = (String)parent.getItemAtPosition(position);
                //text_new_date.setText(uitlYearNew);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        dbHelper = new DBHelper(this);

        selection = "login_util = ?";
        selectionArgs = new String[] { messageActivity };

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("table_util_login", null, selection, selectionArgs, null, null, "date_util");

        arrayListUtil = new ArrayList<String>();

        String utilCreateRes = "";

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int nameUtilColIndex = c.getColumnIndex("name_util");
            int dateUtilColIndex = c.getColumnIndex("date_util");
            int sumUtilColIndex = c.getColumnIndex("sum_util");
            do {
                // получаем значения по номерам столбцов и пишем все в лог
                utilCreateRes = c.getString(dateUtilColIndex) +
                        " - " + c.getString(sumUtilColIndex) +
                        " грн";
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idColIndex) +
                                ", name_util = " + c.getString(nameUtilColIndex) +
                                " " +
                                utilCreateRes);
                arrayListUtil.add(c.getString(nameUtilColIndex));
                arrayListUtil.add(utilCreateRes);
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
            adapterListUtil = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListUtil);
            listUtil.setAdapter(adapterListUtil);
            //listUtil.clearTextFilter();
        } else{
            Log.d(LOG_TAG, "0 rows");
            Toast.makeText(this, "Записів не має!!!", Toast.LENGTH_LONG).show();
        }
        c.close();
        dbHelper.close();
    }

    @Override
    public void onClick(View v) {
        // создаем объект для данных
        Log.d(LOG_TAG, "создаем объект для данных");
        ContentValues cv;

        Cursor c;
        String bufResMonth = "", utilRes = "";


        // подключаемся к БД
        Log.d(LOG_TAG, "подключаемся к БД");
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        utilDateActv = uitlMonthActv + "." + uitlYearActv;

        switch (button_main.getText().toString()) {
            case "Перегляд всіх записів":
                Log.d(LOG_TAG, "--- Rows in table_util_login: ---");
                // делаем запрос всех данных из таблицы table_util_login, получаем Cursor
                selection = "login_util = ?";
                selectionArgs = new String[] { messageActivity };
                c = db.query("table_util_login", null, selection, selectionArgs, null, null, "date_util");

                utilRes = "";
                arrayListUtil = new ArrayList<String>();

                // ставим позицию курсора на первую строку выборки
                // если в выборке нет строк, вернется false
                if (c.moveToFirst()) {
                    // определяем номера столбцов по имени в выборке
                    int idColIndex = c.getColumnIndex("id");
                    int nameUtilColIndex = c.getColumnIndex("name_util");
                    int dateUtilColIndex = c.getColumnIndex("date_util");
                    int sumUtilColIndex = c.getColumnIndex("sum_util");
                    do {
                        utilRes = c.getString(dateUtilColIndex) +
                                " - " + c.getString(sumUtilColIndex) +
                                " грн";
                        // получаем значения по номерам столбцов и пишем все в лог
                        Log.d(LOG_TAG,
                                "ID = " + c.getInt(idColIndex) +
                                        ", name_util = " + c.getString(nameUtilColIndex) +
                                        " " +
                                        utilRes);
                        arrayListUtil.add(c.getString(nameUtilColIndex));
                        arrayListUtil.add(utilRes);
                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (c.moveToNext());
                    adapterListUtil = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListUtil);
                    listUtil.setAdapter(adapterListUtil);
                } else{
                    Log.d(LOG_TAG, "0 rows");
                    Toast.makeText(this, "Записів не має!!!", Toast.LENGTH_LONG).show();
                    break;
                }
                c.close();
                break;
            case "Перегляд записів по місяцям":
                Log.d(LOG_TAG, "--- Перегляд записів по місяцям: ---");
                Toast.makeText(this, "Нажата кнопка Перегляд записів по місяцям", Toast.LENGTH_LONG).show();
                selection = "login_util = ?";
                selectionArgs = new String[] { messageActivity };
                // делаем запрос всех данных из таблицы table_util_login, получаем Cursor
                c = db.query("table_util_login", null, selection, selectionArgs, null, null, "date_util");

                utilRes = "";
                double sumUtilMonth = 0.0;
                arrayListUtil = new ArrayList<String>();

                // ставим позицию курсора на первую строку выборки
                // если в выборке нет строк, вернется false
                if (c.moveToFirst()) {
                    // определяем номера столбцов по имени в выборке
                    int dateUtilColIndex = c.getColumnIndex("date_util");
                    int sumUtilColIndex = c.getColumnIndex("sum_util");
                    bufResMonth = c.getString(dateUtilColIndex);
                    do {
                        if(bufResMonth.equalsIgnoreCase(c.getString(dateUtilColIndex))){
                            sumUtilMonth += c.getDouble(sumUtilColIndex);
                        }
                        else {
                            utilRes = c.getString(dateUtilColIndex) +
                                    " - " + String.valueOf(sumUtilMonth) +
                                    " грн";
                            // получаем значения по номерам столбцов и пишем все в лог
                            Log.d(LOG_TAG, utilRes);
                            arrayListUtil.add(utilRes);
                            bufResMonth = c.getString(dateUtilColIndex);
                            sumUtilMonth = c.getDouble(sumUtilColIndex);
                        }
                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (c.moveToNext());
                    utilRes = bufResMonth +
                            " - " + String.valueOf(sumUtilMonth) +
                            " грн";
                    // получаем значения по номерам столбцов и пишем все в лог
                    Log.d(LOG_TAG, utilRes);
                    arrayListUtil.add(utilRes);
                    adapterListUtil = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListUtil);
                    listUtil.setAdapter(adapterListUtil);
                } else{
                    Log.d(LOG_TAG, "0 rows");
                    Toast.makeText(this, "Записів не має!!!", Toast.LENGTH_LONG).show();
                    break;
                }
                c.close();
                break;
            case "Пошук за типом витрат":
                Log.d(LOG_TAG, "--- Rows in table_util_login: ---");
                // делаем запрос всех данных из таблицы table_util_login, получаем Cursor
                selection = "login_util = ? AND name_util = ?";
                selectionArgs = new String[] { messageActivity, utilName };
                c = db.query("table_util_login", null, selection, selectionArgs, null, null, "date_util");

                utilRes = "";
                arrayListUtil = new ArrayList<String>();

                // ставим позицию курсора на первую строку выборки
                // если в выборке нет строк, вернется false
                if (c.moveToFirst()) {
                    // определяем номера столбцов по имени в выборке
                    int idColIndex = c.getColumnIndex("id");
                    int nameUtilColIndex = c.getColumnIndex("name_util");
                    int dateUtilColIndex = c.getColumnIndex("date_util");
                    int sumUtilColIndex = c.getColumnIndex("sum_util");
                    do {
                        utilRes = c.getString(dateUtilColIndex) +
                                " - " + c.getString(sumUtilColIndex) +
                                " грн";
                        // получаем значения по номерам столбцов и пишем все в лог
                        Log.d(LOG_TAG,
                                "ID = " + c.getInt(idColIndex) +
                                        ", name_util = " + c.getString(nameUtilColIndex) +
                                        " " +
                                        utilRes);
                        arrayListUtil.add(utilRes);
                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (c.moveToNext());
                    adapterListUtil = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListUtil);
                    listUtil.setAdapter(adapterListUtil);
                } else{
                    Log.d(LOG_TAG, "0 rows");
                    Toast.makeText(this, "Записів не має!!!", Toast.LENGTH_LONG).show();
                    break;
                }
                c.close();
                break;
            case "Добавити запис":
                Toast.makeText(this, "Нажата кнопка Добавити", Toast.LENGTH_LONG).show();
                if(editText_sum.getText().toString() != ""){
                    Log.d(LOG_TAG, "--- Insert in table_util_login: ---");

                    selection = "login_util = ? AND name_util = ? AND date_util = ?";
                    selectionArgs = new String[] { messageActivity, utilName, utilDateActv };

                    // делаем запрос всех данных из таблицы table_util_login, получаем Cursor
                    c = db.query("table_util_login", null, selection, selectionArgs, null, null, null);

                    arrayListUtil = new ArrayList<String>();
                    Integer checkUtilDB = 0, oneUtil = 1;

                    //String bufDateMonth = "";
                    //Double countSumMonth = 0.0;

                    // ставим позицию курсора на первую строку выборки
                    // если в выборке нет строк, вернется false
                    if (c.moveToFirst()) {
                        do {
                            checkUtilDB = oneUtil;
                        } while (c.moveToNext());
                    } else{
                        Log.d(LOG_TAG, "0 rows");
                        Toast.makeText(this, "Записів не має!!!", Toast.LENGTH_LONG).show();
                    }
                    c.close();

                    if(checkUtilDB.equals(oneUtil)){
                        Log.d(LOG_TAG, ", (" + String.valueOf(checkUtilDB) + ") ");
                        Toast.makeText(this, "Запис БД існує!!!", Toast.LENGTH_LONG).show();
                        break;
                    }
                    else {
                        cv = new ContentValues();

                        // получаем данные
                        Log.d(LOG_TAG, "получаем данные");
                        String utilSum = editText_sum.getText().toString();

                        // подготовим данные для вставки в виде пар: наименование столбца - значение
                        cv.put("login_util", messageActivity);
                        cv.put("name_util", utilName);
                        cv.put("date_util", utilDateActv);
                        cv.put("sum_util", Double.valueOf(utilSum));
                        // вставляем запись и получаем ее ID
                        long rowID = db.insert("table_util_login", null, cv);
                        Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                        Toast.makeText(this, "Запис добавлений!", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                else {
                    Toast.makeText(this, "Введіть суму!", Toast.LENGTH_LONG).show();
                    break;
                }
            case "Редагувати запис":
                if (utilDateActv.equalsIgnoreCase("")) {
                    break;
                }
                if(editText_sum.getText().toString() != ""){
                    Log.d(LOG_TAG, "--- Update table_util_login: ---");

                    cv = new ContentValues();

                    // получаем данные
                    Log.d(LOG_TAG, "получаем данные");
                    String utilSum = editText_sum.getText().toString();

                    utilDateNew = uitlMonthNew + "." + uitlYearNew;

                    // подготовим значения для обновления
                    cv.put("login_util", messageActivity);
                    cv.put("name_util", utilName);
                    cv.put("date_util", utilDateNew);
                    cv.put("sum_util", Double.valueOf(utilSum));
                                        // обновляем по id
                    int updCount = db.update("table_util_login", cv, "login_util = ? AND name_util = ? AND date_util = ?",
                            new String[] { messageActivity, utilName, utilDateActv });
                    Log.d(LOG_TAG, "updated rows count = " + updCount);
                    Toast.makeText(this, "Запис змінений!", Toast.LENGTH_LONG).show();
                    break;
                }
                else {
                    Toast.makeText(this, "Введіть суму!", Toast.LENGTH_LONG).show();
                    break;
                }
            case "Видалити запис":
                if (utilDateActv.equalsIgnoreCase("")) {
                    break;
                }
                Log.d(LOG_TAG, "--- Delete from table_util_login: ---");
                // удаляем по login_util = ? AND name_util = ? AND date_util
                selection = "login_util = ? AND name_util = ? AND date_util = ?";
                selectionArgs = new String[] { messageActivity, utilName, utilDateActv };
                int delCount = db.delete("table_util_login", selection, selectionArgs);
                Log.d(LOG_TAG, "deleted rows count = " + delCount);
                Toast.makeText(this, "Запис видалений!", Toast.LENGTH_LONG).show();
                break;
            case "Видалити всі записи":
                //this.deleteDatabase("DB_Utilities.db");
                Log.d(LOG_TAG, "--- deleteDatabase: ---");
                /*
                arrayListUtil = new ArrayList<String>();
                adapterListUtil = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListUtil);
                listUtil.setAdapter(adapterListUtil);
                Log.d(LOG_TAG, "--- Delete all table_util_loginl: ---");
                // удаляем все записи
                int clearCount = db.delete("table_util_login", null, null);
                Log.d(LOG_TAG, "deleted rows count = " + clearCount);
                Toast.makeText(this, "Всі запис видалені!", Toast.LENGTH_LONG).show();
                */
                break;
        }
        // закрываем подключение к БД
        dbHelper.close();
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "DB_Utilities", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table table_util_login ("
                    + "id integer primary key autoincrement,"
                    + "login_util text,"
                    + "name_util text,"
                    + "date_util text,"
                    + "sum_util real" + ");"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Button button_main = (Button) findViewById(R.id.button_main);
        switch(id){
            case R.id.show_settings:
                button_main.setText("Перегляд всіх записів");
                LinearLayoutTextNew.setLayoutParams(paramsLayoutHeightMin);
                LinearLayoutSpinNew.setLayoutParams(paramsLayoutHeightMin);
                LinearLayoutSum.setLayoutParams(paramsLayoutHeightMin);
                return true;
            case R.id.show_month_settings:
                button_main.setText("Перегляд записів по місяцям");
                LinearLayoutTextNew.setLayoutParams(paramsLayoutHeightMin);
                LinearLayoutSpinNew.setLayoutParams(paramsLayoutHeightMin);
                LinearLayoutSum.setLayoutParams(paramsLayoutHeightMin);
                return true;
            case R.id.type_util_settings:
                button_main.setText("Пошук за типом витрат");
                LinearLayoutTextNew.setLayoutParams(paramsLayoutHeightMin);
                LinearLayoutSpinNew.setLayoutParams(paramsLayoutHeightMin);
                LinearLayoutSum.setLayoutParams(paramsLayoutHeightMin);
                return true;
            case R.id.save_settings:
                button_main.setText("Добавити запис");
                LinearLayoutTextNew.setLayoutParams(paramsLayoutHeightMin);
                LinearLayoutSpinNew.setLayoutParams(paramsLayoutHeightMin);
                LinearLayoutSum.setLayoutParams(paramsLayoutHeightMax);
                return true;
            case R.id.edit_settings:
                button_main.setText("Редагувати запис");
                LinearLayoutTextNew.setLayoutParams(paramsLayoutHeightMax);
                LinearLayoutSpinNew.setLayoutParams(paramsLayoutHeightMax);
                LinearLayoutSum.setLayoutParams(paramsLayoutHeightMax);
                return true;
            case R.id.delete_settings:
                button_main.setText("Видалити запис");
                LinearLayoutTextNew.setLayoutParams(paramsLayoutHeightMin);
                LinearLayoutSpinNew.setLayoutParams(paramsLayoutHeightMin);
                LinearLayoutSum.setLayoutParams(paramsLayoutHeightMin);
                return true;
            case R.id.delete_all_settings:
                button_main.setText("Видалити всі записи");
                LinearLayoutTextNew.setLayoutParams(paramsLayoutHeightMin);
                LinearLayoutSpinNew.setLayoutParams(paramsLayoutHeightMin);
                LinearLayoutSum.setLayoutParams(paramsLayoutHeightMin);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
