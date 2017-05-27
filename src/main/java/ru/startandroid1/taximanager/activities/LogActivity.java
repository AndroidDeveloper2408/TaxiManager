package ru.startandroid1.taximanager.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.startandroid1.taximanager.R;
import ru.startandroid1.taximanager.database.DB;

public class LogActivity extends AppCompatActivity implements View.OnClickListener {

    EditText eTLog;
    EditText eTPass;
    Button btnEnter;

    DB db3;

    String[] sLACheck = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        eTLog = (EditText)findViewById(R.id.eTLog);
        eTPass = (EditText)findViewById(R.id.eTPass);
        btnEnter = (Button)findViewById(R.id.btnEnter);
        btnEnter.setOnClickListener(this);

        // открываем подключение к БД
        db3 = new DB(this);
        db3.open();
    }

    @Override
    public void onClick(View v) {
        if(eTLog.getText().toString().equals("dl") && eTPass.getText().toString().equals("dp"))
        {
            Intent intent = new Intent(getBaseContext(), DriverActivity.class);
            intent.putExtra("LALogin", "dl");
            intent.putExtra("LAName", "Данила");
            startActivity(intent);
            finish();
        }
        else {
            sLACheck = db3.ifCorrect(eTLog.getText().toString(), eTPass.getText().toString());
            if (sLACheck[0] == "Error to get the record") {
                Toast.makeText(getBaseContext(), "Wrong login or password", Toast.LENGTH_SHORT).show();
            } else {

                Intent intent = new Intent(getBaseContext(), UsersActivity.class);
                intent.putExtra("LALogin", sLACheck[0]);
                intent.putExtra("LAName", sLACheck[1]);
                startActivity(intent);
                finish();
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        db3.close();
    }

}
