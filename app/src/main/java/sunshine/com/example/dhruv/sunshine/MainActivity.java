package sunshine.com.example.dhruv.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ForecastFragment.Callback{

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String DETAILFRAGMENT_TAG = "DFTAG";

    private boolean mTwoPane;
    private String mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocation = Utility.getPreferredLocation(this);
        if(findViewById(R.id.weather_detail_container)!=null){
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if(savedInstanceState==null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container,new DetailFragment(),DETAILFRAGMENT_TAG)
                        .commit();
            }
        }else{
            mTwoPane = false;
        }

        ForecastFragment forecastFragment = (ForecastFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_forecast);
        forecastFragment.setmUseTodayLayout(!mTwoPane);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }
        if(id == R.id.action_map){
            openPreferredLocationInMap();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openPreferredLocationInMap(){
        String location = Utility.getPreferredLocation(this);

        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q",location)
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }else{
            Log.d(LOG_TAG, "Couldn't call "+location+", no recieving apps installed");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String location = Utility.getPreferredLocation(this);
        if(location!=null && !location.equals(mLocation)){
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
            if(ff!=null){
                ff.onLocationChanged();
            }
            DetailFragment df = (DetailFragment) getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
            if(df!=null){
                df.onLocationChanged(location);
            }
            mLocation = location;
        }
    }

    @Override
    public void onItemSelected(Uri contentUri) {
        if(mTwoPane){
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI,contentUri);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.weather_detail_container,fragment,DETAILFRAGMENT_TAG)
                    .commit();
        }else{
            Intent intent = new Intent(this,DetailActivity.class)
                    .setData(contentUri);
            startActivity(intent);
        }
    }
}
