package ru.startandroid1.taximanager.fragment.driver;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import ru.startandroid1.taximanager.database.DB;
import ru.startandroid1.taximanager.activities.InfoActivity;
import ru.startandroid1.taximanager.R;

public class DriverFragmentAllTransportations extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LAYOUT = R.layout.fragment_driver_all;
    private View view;
    private NavigationView navigationView;
    private TextView textView;
    private ListView listView;
    String [] mass_data = new String[8];
    static String arg = "";
    private static final int CM_DELETE_ID = 16;
    private static final int CM_UPDATE_ID = 17;
    DB db;
    SimpleCursorAdapter scAdapter;

    public static DriverFragmentAllTransportations getInstance(){
        Bundle args = new Bundle();
        DriverFragmentAllTransportations fragment = new DriverFragmentAllTransportations();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        listView = (ListView)view.findViewById(R.id.mylistfda);
        navigationView = (NavigationView)getActivity().findViewById(R.id.nav_view);
        textView = (TextView)navigationView.getHeaderView(0).findViewById(R.id.tvDriverActivityLogin);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // открываем подключение к БД
        db = new DB(getContext());
        db.open();

        // формируем столбцы сопоставления
        String[] from = new String[] { DB.COLUMN_ICON, DB.COLUMN_ID, DB.COLUMN_STATE };
        int[] to = new int[] { R.id.ivIconFDA, R.id.tvNumberFDA, R.id.tvStateFDA };
        // создаем адаптер и настраиваем список
        scAdapter = new SimpleCursorAdapter(getContext(), R.layout.item_driver_all, null, from, to, 0);
        listView.setAdapter(scAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), InfoActivity.class);
                mass_data = db.getInfoRequest(l);
                intent.putExtra("state", mass_data[0]);
                intent.putExtra("routefrom", mass_data[1]);
                intent.putExtra("routeto", mass_data[2]);
                intent.putExtra("distance", mass_data[3]);
                intent.putExtra("customer", mass_data[4]);
                intent.putExtra("driver", mass_data[5]);
                intent.putExtra("cost", mass_data[6]);
                intent.putExtra("time", mass_data[7]);
                intent.putExtra("id", l);
                intent.putExtra("fragment", "da");
                startActivity(intent);
            }
        });
        registerForContextMenu(listView);

        // создаем лоадер для чтения данных
        getLoaderManager().initLoader(0, null, this);

        arg = textView.getText().toString();
    }

    public void onPause() {
        super.onPause();
        Toast.makeText(getContext(), "onPauseAll", Toast.LENGTH_SHORT).show();
    }

    public void onResume() {
        super.onResume();
        // получаем новый курсор с данными
        getLoaderManager().getLoader(0).forceLoad();
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, "delete_transportation");
        menu.add(0, CM_UPDATE_ID, 0, "update_data");
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            // получаем из пункта контекстного меню данные по пункту списка
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                    .getMenuInfo();
            // извлекаем id записи и удаляем соответствующую запись в БД
            db.delRecFromRequests(acmi.id);
            // получаем новый курсор с данными
            getLoaderManager().getLoader(0).forceLoad();
            return true;
        }
        if (item.getItemId() == CM_UPDATE_ID) {
            // получаем новый курсор с данными
            getLoaderManager().getLoader(0).forceLoad();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    public void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        db.close();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(getContext(), db);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        scAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    static class MyCursorLoader extends CursorLoader {

        DB db;

        public MyCursorLoader(Context context, DB db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getAllDriverRequest(arg);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cursor;
        }
    }
}
