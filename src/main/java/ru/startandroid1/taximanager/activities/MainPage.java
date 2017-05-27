package ru.startandroid1.taximanager.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import ru.startandroid1.taximanager.R;
import ru.startandroid1.taximanager.dialogs.MyDialog;

public class MainPage extends AppCompatActivity {

    TextView tVMainPageEnter;
    TextView tVMainPageReg;
    TextView tVMainPageAdmin;

    MyTask mt;

    int arg = 0;

    DialogFragment dlg1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        dlg1 = new MyDialog();

        tVMainPageEnter = (TextView)findViewById(R.id.tVMainPageEnter);
        tVMainPageReg = (TextView)findViewById(R.id.tVMainPageReg);
        tVMainPageAdmin = (TextView)findViewById(R.id.tVMainPageAdmin);
    }

    public void onBackPressed() {
        // вызываем диалог
        if(arg == 0)
        dlg1.show(getSupportFragmentManager(), "dlg1");
    }

    @Override
    protected void onResume() {
        super.onResume();
        arg = 0;
        tVMainPageEnter.setClickable(true);
        tVMainPageReg.setClickable(true);
        tVMainPageAdmin.setClickable(true);
    }

    public void onClickEnter(View v)
    {
        arg = 1;
        mt = new MyTask();
        mt.execute();
        tVMainPageEnter.setClickable(false);
        tVMainPageReg.setClickable(false);
        tVMainPageAdmin.setClickable(false);
    }

    public void onClickReg(View v)
    {
        arg = 2;
        mt = new MyTask();
        mt.execute();
        tVMainPageEnter.setClickable(false);
        tVMainPageReg.setClickable(false);
        tVMainPageAdmin.setClickable(false);
    }

    public void onClickAdmin(View v)
    {
        arg = 3;
        mt = new MyTask();
        mt.execute();
        tVMainPageEnter.setClickable(false);
        tVMainPageReg.setClickable(false);
        tVMainPageAdmin.setClickable(false);
    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Animation anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.myalpha);
            switch(arg){
                case 1:
                    tVMainPageEnter.startAnimation(anim);
                    break;
                case 2:
                    tVMainPageReg.startAnimation(anim);
                    break;
                case 3:
                    tVMainPageAdmin.startAnimation(anim);
                    break;
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Intent intent;
            switch(arg) {
                case 1:
                    intent = new Intent(getBaseContext(), LogActivity.class);
                    startActivity(intent);
                    break;
                case 2:
                    intent = new Intent(getBaseContext(), RegActivity.class);
                    startActivity(intent);
                    break;
                case 3:
                    intent = new Intent(getBaseContext(), AdminActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}
