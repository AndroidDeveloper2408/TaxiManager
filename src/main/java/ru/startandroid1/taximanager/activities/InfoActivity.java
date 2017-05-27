package ru.startandroid1.taximanager.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ru.startandroid1.taximanager.R;
import ru.startandroid1.taximanager.database.DB;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tVCustomer;
    TextView tVState;
    TextView tVRouteFrom;
    TextView tVRouteTo;
    TextView tVDistance;
    TextView tVDriver;
    TextView tVCosts;
    TextView tVTime;

    Button btnAcceptRequest;
    Button btnCancelRequest;
    Button btnDeleteRequest;
    Button btnCompleteRequest;
    Button btnShowRoute;

    DB db;

    long arg;

    String fragment = "";
    String driver = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // открываем подключение к БД
        db = new DB(this);
        db.open();

        tVCustomer = (TextView)findViewById(R.id.tVCustomer);
        tVState = (TextView)findViewById(R.id.tVState);
        tVRouteFrom = (TextView)findViewById(R.id.tVRouteFrom);
        tVRouteTo = (TextView)findViewById(R.id.tVRouteTo);
        tVDistance = (TextView)findViewById(R.id.tVDistance);
        tVDriver = (TextView)findViewById(R.id.tVDriver);
        tVCosts = (TextView)findViewById(R.id.tVCosts);
        tVTime = (TextView)findViewById(R.id.tVTime);

        btnAcceptRequest = (Button)findViewById(R.id.btnAcceptRequest);
        btnCancelRequest = (Button)findViewById(R.id.btnCancelRequest);
        btnDeleteRequest = (Button)findViewById(R.id.btnDeleteRequest);
        btnCompleteRequest = (Button)findViewById(R.id.btnCompleteRequest);
        btnShowRoute = (Button)findViewById(R.id.btnShowRoute);

        btnAcceptRequest.setOnClickListener(this);
        btnCancelRequest.setOnClickListener(this);
        btnDeleteRequest.setOnClickListener(this);
        btnCompleteRequest.setOnClickListener(this);
        btnShowRoute.setOnClickListener(this);

        Intent intent = getIntent();
        driver = intent.getStringExtra("driver");
        tVCustomer.setText(intent.getStringExtra("customer"));
        tVState.setText(intent.getStringExtra("state"));
        tVRouteFrom.setText(intent.getStringExtra("routefrom"));
        tVRouteTo.setText(intent.getStringExtra("routeto"));
        tVDistance.setText(intent.getStringExtra("distance"));
        tVDriver.setText(driver);
        tVCosts.setText(intent.getStringExtra("cost"));
        tVTime.setText(intent.getStringExtra("time"));
        arg =  intent.getLongExtra("id", 10);
        fragment = intent.getStringExtra("fragment");


        switch (fragment){
            case "cc":
                btnAcceptRequest.setVisibility(View.GONE);
                btnDeleteRequest.setVisibility(View.GONE);
                btnCompleteRequest.setVisibility(View.GONE);
                break;
            case "ca":
                btnAcceptRequest.setVisibility(View.GONE);
                btnCancelRequest.setVisibility(View.GONE);
                btnCompleteRequest.setVisibility(View.GONE);
                break;
            case "dn":
                btnDeleteRequest.setVisibility(View.GONE);
                btnCompleteRequest.setVisibility(View.GONE);
                break;
            case "dc":
                btnAcceptRequest.setVisibility(View.GONE);
                btnDeleteRequest.setVisibility(View.GONE);
                break;
            case "da":
                btnCompleteRequest.setVisibility(View.GONE);
                btnAcceptRequest.setVisibility(View.GONE);
                btnCancelRequest.setVisibility(View.GONE);
                break;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        db.close();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnShowRoute:
                Intent intent = new Intent(this, RouteActivity.class);
                intent.putExtra("from", tVRouteFrom.getText().toString());
                intent.putExtra("to", tVRouteTo.getText().toString());
                startActivity(intent);
                break;
            case R.id.btnAcceptRequest:
                db.acceptRequestFromCustomer(String.valueOf(arg), driver);
                Toast.makeText(this, "Request by number " + arg + " is accepted", Toast.LENGTH_LONG).show();
                finish();
                break;
            case R.id.btnCompleteRequest:
                db.cmpRequestFromUsers(String.valueOf(arg));
                Toast.makeText(this, "Request by number " + arg + " is completed", Toast.LENGTH_LONG).show();
                finish();
                break;
            case R.id.btnCancelRequest:
                db.updRequestFromUsers(String.valueOf(arg));
                Toast.makeText(this, "Request by number " + arg + " is canceled", Toast.LENGTH_LONG).show();
                finish();
                break;
            case R.id.btnDeleteRequest:
                db.delRecFromRequests(arg);
                Toast.makeText(this, "Request by number " + arg + " is deleted", Toast.LENGTH_LONG).show();
                finish();
                break;
        }
    }
}