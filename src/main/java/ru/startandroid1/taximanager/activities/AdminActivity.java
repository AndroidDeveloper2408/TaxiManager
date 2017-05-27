package ru.startandroid1.taximanager.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ru.startandroid1.taximanager.R;
import ru.startandroid1.taximanager.database.TableFeedBacksActivity;
import ru.startandroid1.taximanager.database.TableRequestsActivity;
import ru.startandroid1.taximanager.database.TableUsersActivity;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAdminPageTU;
    Button btnAdminPageTR;
    Button btnAdminPageTF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnAdminPageTU = (Button)findViewById(R.id.btnAdminPageTU);
        btnAdminPageTR = (Button)findViewById(R.id.btnAdminPageTR);
        btnAdminPageTF = (Button)findViewById(R.id.btnAdminPageTF);

        btnAdminPageTU.setOnClickListener(this);
        btnAdminPageTR.setOnClickListener(this);
        btnAdminPageTF.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btnAdminPageTU:
                intent = new Intent(getBaseContext(), TableUsersActivity.class);
                startActivity(intent);
                break;
            case R.id.btnAdminPageTR:
                intent = new Intent(getBaseContext(), TableRequestsActivity.class);
                startActivity(intent);
                break;
            case R.id.btnAdminPageTF:
                intent = new Intent(getBaseContext(), TableFeedBacksActivity.class);
                startActivity(intent);
                break;
        }
    }
}
