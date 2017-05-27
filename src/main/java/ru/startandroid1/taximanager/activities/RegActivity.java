package ru.startandroid1.taximanager.activities;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.startandroid1.taximanager.R;
import ru.startandroid1.taximanager.database.DB;
import ru.startandroid1.taximanager.dialogs.MyDialog;

public class RegActivity extends AppCompatActivity implements View.OnClickListener {

    DB db2;

    EditText eTRegLog;
    EditText eTRegPass;
    EditText eTRegName;
    EditText eTRegPhone;

    Button btnRegReg;

    DialogFragment dlg1;

    Boolean rACheck = false;
    Boolean rACheck2 = false;

    long rANewRowId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        dlg1 = new MyDialog();

        eTRegLog = (EditText)findViewById(R.id.eTRegLog);
        eTRegPass = (EditText)findViewById(R.id.eTRegPass);
        eTRegName = (EditText)findViewById(R.id.eTRegName);
        eTRegPhone = (EditText)findViewById(R.id.eTRegPhone);

        btnRegReg = (Button)findViewById(R.id.btnRegReg);
        btnRegReg.setOnClickListener(this);
        btnRegReg.setEnabled(false);

        // открываем подключение к БД
        db2 = new DB(this);
        db2.open();

        eTRegLog.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(rACheck2 == false) {
                        Toast.makeText(getBaseContext(), "Введите ваши данные", Toast.LENGTH_SHORT).show();
                        rACheck2 = true;
                    }
                } else {
                    if (!eTRegLog.getText().toString().isEmpty()) {
                        try {
                            rACheck = db2.ifExisting(eTRegLog.getText().toString());
                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(), "Ошибка при проверке " + e, Toast.LENGTH_LONG).show();
                        }
                        if(rACheck == false ) {
                            Toast.makeText(getBaseContext(), "Запись свободна", Toast.LENGTH_SHORT).show();
                            btnRegReg.setEnabled(true);
                        }
                        else
                        {
                            Toast.makeText(getBaseContext(), "Запись занята", Toast.LENGTH_SHORT).show();
                            btnRegReg.setEnabled(false);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        if(eTRegLog.getText().toString().isEmpty() || eTRegPass.getText().toString().isEmpty() || eTRegName.getText().toString().isEmpty()
                || eTRegPhone.getText().toString().isEmpty()) {
            Toast.makeText(this, "You did not fill in all fields", Toast.LENGTH_SHORT).show();
        }

        else {
            if (rACheck == null) {
                Toast.makeText(this, "Что-то пошло не так", Toast.LENGTH_SHORT).show();
            } else if (rACheck == false) {

                rANewRowId = db2.addRecToUsers(R.mipmap.ic_launcher,
                    eTRegLog.getText().toString(),
                    eTRegPass.getText().toString(),
                        "1",
                    eTRegName.getText().toString(),
                    eTRegPhone.getText().toString());

                // Выводим сообщение в успешном случае или при ошибке
                if (rANewRowId == -1) {
                    // Если ID  -1, значит произошла ошибка
                    Toast.makeText(this, "Ошибка при регистрации", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Пользователь зарегистрирован под номером: " + rANewRowId, Toast.LENGTH_SHORT).show();
                }

            }
            finish();
        }
    }

    public void onBackPressed() {
        // вызываем диалог
        dlg1.show(getSupportFragmentManager(), "dlg1");
    }

    protected void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        db2.close();
    }
}