package com.sudo.st1androidapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import android.provider.BaseColumns;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import static com.sudo.st1androidapp.R.id.checkBoxContainer;
import static com.sudo.st1androidapp.R.id.container;

public class MainActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    public ViewPager mViewPager;


    public int numberOfTabs = 50;
    public ArrayList<CheckBox> tasks = new ArrayList<>();

    private TaskDbHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(1000);

        final PlaceholderFragment currentFragment = (PlaceholderFragment) mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                askName(currentFragment);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if(id == R.id.action_addDays) {

        }

        return super.onOptionsItemSelected(item);
    }

    public String askName(PlaceholderFragment currentFragment) {

        final EditText taskEditText = new EditText(MainActivity.this);
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Add a new task")
                .setMessage("What do you want to do next?")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());

                        addCheck(task);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

        return taskEditText.toString();
    }

    public void addCheck(String name) {
        /* SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, name);
        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        db.close(); */


        PlaceholderFragment currentFragment = (PlaceholderFragment) mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem());
        final LinearLayout ll = (LinearLayout) currentFragment.rootView.findViewById(R.id.ll);

        final CheckBox cb = new CheckBox(getApplicationContext());
        cb.setText(name);

        tasks.add(cb);
        cb.setId(tasks.size() + 1);

        ll.addView(cb);

        CheckBox repeatChkBx = ( CheckBox ) currentFragment.rootView.findViewById( tasks.size() + 1 );
        repeatChkBx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    ll.removeView(cb);
                }

            }
        });
    }

    /* -------------- */

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        public View rootView;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public View getView() {
            return rootView;
        }

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public ArrayList<Fragment> frags = new ArrayList<>(numberOfTabs);

        private SectionsPagerAdapter(FragmentManager fm) {

            super(fm);
            for(int i = 0; i <= numberOfTabs - 1; i++) {
                frags.add(PlaceholderFragment.newInstance(i));
            }
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            // return PlaceholderFragment.newInstance(position + 1);

            return frags.get(position);
        }

        @Override
        public int getCount() {
            return frags.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Date currentDate = new Date();

            int daysAdded = position * (1000 * 60 * 60 * 24);

            if(position == 0) {
                return "Today";
            } else if (position == 1) {
                return "Tomorrow";
            } else {
                return new SimpleDateFormat("dd MMMM yy").format(new Date(currentDate.getTime() + daysAdded));
            }
        }
    }

    public class TaskContract {
        public static final String DB_NAME = "com.sudo.st1androidapp";
        public static final int DB_VERSION = 1;

        public class TaskEntry implements BaseColumns {
            public static final String TABLE = "tasks";

            public static final String COL_TASK_TITLE = "title";
            public static final String COL_TASK_DATE = "date";
        }
    }

    public class TaskDbHelper extends SQLiteOpenHelper {

        public TaskDbHelper(Context context) {
            super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
                    TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL);" +
                    TaskContract.TaskEntry.COL_TASK_DATE + " DATE NOT NULL);";

            db.execSQL(createTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
            onCreate(db);
        }
    }
}
