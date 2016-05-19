package sunshine.com.example.dhruv.sunshine;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container,new DetailFragment())
                    .commit();
        }
    }

    public static class DetailFragment extends Fragment
    {
        public DetailFragment(){
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail,container,false);
            Intent intent = getActivity().getIntent();
            if(intent!=null && intent.hasExtra(Intent.EXTRA_TEXT)){
                String forecastStr = intent.getStringExtra(Intent.EXTRA_TEXT);
                ((TextView)rootView.findViewById(R.id.detail_text)).setText(forecastStr);
            }

            return rootView;
        }
    }
}