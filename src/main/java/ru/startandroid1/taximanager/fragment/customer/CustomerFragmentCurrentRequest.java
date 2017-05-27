package ru.startandroid1.taximanager.fragment.customer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
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

import ru.startandroid1.taximanager.database.DB;
import ru.startandroid1.taximanager.activities.InfoActivity;
import ru.startandroid1.taximanager.R;

public class CustomerFragmentCurrentRequest extends Fragment implements LoaderCallbacks<Cursor> {

    private static final int LAYOUT = R.layout.fragment_user_current;
    private View view;
    private NavigationView navigationView;
    private TextView textView;
    private ListView lvData;
    private String [] mass_data = new String[8];
    static String arg = "";
    private static final int CM_CANCEL_ID = 3;
    private static final int CM_UPDATE_ID = 5;
    DB db;
    SimpleCursorAdapter scAdapter3;

    public static CustomerFragmentCurrentRequest getInstance(){
        Bundle args = new Bundle();
        CustomerFragmentCurrentRequest fragment = new CustomerFragmentCurrentRequest();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        lvData = (ListView)view.findViewById(R.id.mylist1);
        navigationView = (NavigationView)getActivity().findViewById(R.id.nav_view_user);
        textView = (TextView)navigationView.getHeaderView(0).findViewById(R.id.tVMainActivityLogin);
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
        int[] to = new int[] { R.id.ivIconFUC, R.id.tvNumberFUC, R.id.tvStateFUC };

        // создаем адаптер и настраиваем список
        scAdapter3 = new SimpleCursorAdapter(getContext(), R.layout.item_user_current, null, from, to, 0);
        lvData.setAdapter(scAdapter3);
        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                intent.putExtra("fragment", "cc");
                startActivity(intent);
            }
        });
        registerForContextMenu(lvData);

        // создаем лоадер для чтения данных
        getLoaderManager().initLoader(0, null, this);

        arg = textView.getText().toString();
    }

    public void onResume() {
        super.onResume();
        // получаем новый курсор с данными
        getLoaderManager().getLoader(0).forceLoad();
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_CANCEL_ID, 0, "cancel_request");
        menu.add(0, CM_UPDATE_ID, 0, "update_data");
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_CANCEL_ID) {
            // получаем из пункта контекстного меню данные по пункту списка
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                    .getMenuInfo();
            Toast.makeText(getContext(), "ContextCurrent", Toast.LENGTH_SHORT).show();
            // извлекаем id записи и удаляем соответствующую запись в БД
            db.updRequestFromUsers(String.valueOf(acmi.id));
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
        scAdapter3.swapCursor(data);
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
            Cursor cursor = db.getCurrentUserRequest(arg, "processed", "perfomed");
            return cursor;
        }
    }
}
