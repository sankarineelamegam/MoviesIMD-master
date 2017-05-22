package movie.n.sankari.movieimdb.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import movie.n.sankari.movieimdb.R;
import movie.n.sankari.movieimdb.activities.MainActivity;
import movie.n.sankari.movieimdb.interfaces.ICommonInterface;

public class MovieDetailsFragment extends Fragment implements ICommonInterface {

    private TextView title;
    private TextView details;

    private String titleText = "";
    private String detailsText = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {

        View view = null;
            try{
                // Inflate the layout for this fragment
                 view = inflater.inflate(R.layout.fragment_movie_details2, container, false);
                initViews(view);
                setArguments();
                populateViews();
                MainActivity.btnFilter.setVisibility(View.GONE);
            }catch (Exception e){

            }

        return view;
    }


    private void populateViews() {
        title.setText(titleText);
        details.setText(detailsText);
    }


    private void setArguments() {

        if (getArguments() != null) {

            if (getArguments().containsKey("titleText")) {

                titleText = getArguments().getString("titleText");

            }

            if (getArguments().containsKey("detailsText")) {

                detailsText = getArguments().getString("detailsText");

            }


        }

    }


    @Override
    public void initServices() {

    }

    @Override
    public void initListeners() {

    }

    @Override
    public void initViews(View view) {

        title = (TextView) view.findViewById(R.id.title);
        details = (TextView) view.findViewById(R.id.details);


    }
}
