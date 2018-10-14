package asia.airobotics.womensecurity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    SNavigationDrawer sNavigationDrawer;
    Class fragmentClass;
    public static Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        init();
    }

    private void init() {

        sNavigationDrawer = findViewById(R.id.navigation_drawer);
        List<MenuItem> menuItems = new ArrayList<>();

        //Use the MenuItem given by this library and not the default one.
        //First parameter is the title of the menu item and
        // then the second parameter is the image which will be the background of the menu item.

//        menuItems.add(new MenuItem("News",R.drawable.news_bg));
//        menuItems.add(new MenuItem("Feed",R.drawable.feed_bg));
//        menuItems.add(new MenuItem("Messages",R.drawable.message_bg));
//        menuItems.add(new MenuItem("Music",R.drawable.music_bg));
   }
}
