package layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hemantithide.borisendesjaak.MainActivity;
import com.hemantithide.borisendesjaak.OnFragmentChangeListener;
import com.hemantithide.borisendesjaak.R;

public class MainFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    public void init(OnFragmentChangeListener listener) {
        Button playBtn = (Button) getView().findViewById(R.id.menu_button_play);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.changeFragment(MainActivity.PLAY_FRAGMENT);
            }
        });
    }

}