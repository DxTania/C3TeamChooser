package org.ieee.c3.teamchooser;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    public List<String> todaysIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todaysIDs = new ArrayList<String>();

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            Fragment fragment;
            switch(position) {
                case 0:
                    fragment = new RegisterFragment();
                    break;
                case 1:
                    fragment = new MakeTeamsFragment();
                    break;
                default:
                    fragment = new SettingsFragment();
            }
            //Bundle args = new Bundle();
            //args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
            //fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    public static void toast(String text, Context context) {
        if (text == null || text.equals("")) {
            text = "Something went wrong! :(";
        }
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public List<String> getTodaysIDs() {
        return todaysIDs;
    }

    /**
     * This function searches people.csv for a specific id
     * @param id The id to search for
     * @param context The context to use for file opening
     * @return String name The name if found, empty string otherwise
     */
    public static String findEntry(String id, Context context) {
        FileInputStream fos;
        try {
            fos = context.openFileInput("people.csv");
        } catch (FileNotFoundException e) {
            Log.d("DBG Main Activity", "File not found!");
            MainActivity.toast(null, context);
            return null;
        }
        // Look for an entry with this id
        int content;
        String curId = "";
        String name = "";
        try {
            while((content = fos.read()) != -1) {
                char c = (char) content;
                if (c == ',' || c == '\n') {
                    if (curId.equals(id)) {
                        break;
                    } else {
                        curId = "";
                    }
                } else {
                    curId += c;
                }
            }
            // If we found the id, find the name of the person
            if (!curId.equals("")) {
                while((content = fos.read()) != -1) {
                    char c = (char) content;
                    if (c == ',') {
                        break;
                    } else {
                        name += c;
                    }
                }
            }
            fos.close();
        } catch(IOException e) {
            Log.d("DBG Main Activity", "IO Exception");
            MainActivity.toast("Something went wrong! :(", context);
        }
        return name;
    }

    public static String getContents(Context context) {
        String file = "";
        int content;
        try {
            FileInputStream fos = context.openFileInput("people.csv");
            while((content = fos.read()) != -1) {
                file += (char) content;
            }
            fos.close();
            return file;
        } catch (FileNotFoundException e) {
            return "";
        } catch (IOException e) {
            Log.d("DBG Make Teams Fragment:", "IO Exception");
            MainActivity.toast(null, context);
            return "";
        }
    }

    public static void resultOK(String id, Activity activity) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("ID", id);
        activity.setResult(RESULT_OK, returnIntent);
    }

}
