package ru.startandroid1.taximanager.fragment.customer;

import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import ru.startandroid1.taximanager.database.DB;
import ru.startandroid1.taximanager.R;

public class CustomerFragmentFeedbacks extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LAYOUT = R.layout.fragment_user_feedback;
    private View view;
    private NavigationView navigationView;
    private TextView textView;
    private EditText editText;
    private ListView listView2;
    static String arg = "";
    private static final int CM_DELETE_ID = 2;
    DB db;
    SimpleCursorAdapter scAdapter2;

    public static CustomerFragmentFeedbacks getInstance(){
        Bundle args = new Bundle();
        CustomerFragmentFeedbacks fragment = new CustomerFragmentFeedbacks();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        listView2 = (ListView)view.findViewById(R.id.mylist2);
        navigationView = (NavigationView)getActivity().findViewById(R.id.nav_view_user);
        textView = (TextView)navigationView.getHeaderView(0).findViewById(R.id.tVMainActivityLogin);
        editText = (EditText)view.findViewById(R.id.eTMessageFeedback);
        Button button = (Button) view.findViewById(R.id.btnGiveFeedback);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(editText.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "You did not write any feedback", Toast.LENGTH_SHORT).show();
                }
                else {
                    db.addRecToFeedbacks(R.mipmap.ic_launcher, textView.getText().toString(), editText.getText().toString());
                    editText.setText("");
                    // получаем новый курсор с данными
                    getLoaderManager().getLoader(0).forceLoad();
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // открываем подключение к БД
        db = new DB(getContext());
        db.open();

        // формируем столбцы сопоставления
        String[] from = new String[] { DB.COLUMN_PIC, DB.COLUMN_USER, DB.COLUMN_FEEDBACK };
        int[] to = new int[] { R.id.ivIconFeedbackFU, R.id.tvUserFeedbackFU, R.id.tvFeedbackFU};

        // создаем адаптер и настраиваем список
        scAdapter2 = new SimpleCursorAdapter(getContext(), R.layout.item_user_feedback, null, from, to, 0);
        listView2.setAdapter(scAdapter2);

        registerForContextMenu(listView2);

        // создаем лоадер для чтения данных
        getLoaderManager().initLoader(0, null, this);

        arg = textView.getText().toString();
    }

    public void onPause() {
        super.onPause();
        Toast.makeText(getContext(), "onPauseFeedbacks", Toast.LENGTH_SHORT).show();
    }

    public void onResume() {
        super.onResume();
        Toast.makeText(getContext(), "onResumeFeedbacks", Toast.LENGTH_SHORT).show();
        // получаем новый курсор с данными
        getLoaderManager().getLoader(0).forceLoad();
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, "delete_record");
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            // получаем из пункта контекстного меню данные по пункту списка
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                    .getMenuInfo();
            Toast.makeText(getContext(), "ContextFeedbacks", Toast.LENGTH_SHORT).show();
            // извлекаем id записи и удаляем соответствующую запись в БД
            db.delRecFromFeedbacks(acmi.id);
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
        scAdapter2.swapCursor(data);
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
            Cursor cursor = db.getAllDataFeedbacks();
            return cursor;
        }
    }
}
