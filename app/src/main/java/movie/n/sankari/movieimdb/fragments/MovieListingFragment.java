package movie.n.sankari.movieimdb.fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import movie.n.sankari.movieimdb.R;
import movie.n.sankari.movieimdb.adapters.MoviesListAddapter;
import movie.n.sankari.movieimdb.dialogs.DatePickerFragment;
import movie.n.sankari.movieimdb.interfaces.ICommonInterface;
import movie.n.sankari.movieimdb.models.responseModels.Discover;
import movie.n.sankari.movieimdb.models.responseModels.DiscoverResult;
import movie.n.sankari.movieimdb.services.DiscoverMoviesService;
import movie.n.sankari.movieimdb.utils.EndlessRecyclerOnScrollListener;
import movie.n.sankari.movieimdb.utils.FieldShakerAnimation;
import movie.n.sankari.movieimdb.utils.Progress;


public class MovieListingFragment
        extends Fragment
        implements ICommonInterface,
        DiscoverMoviesService.DiscoverMoviesCallback,
        MoviesListAddapter.IItemClicked,
        View.OnClickListener,
        DatePickerFragment.DatePickerInterface

{


    private DiscoverMoviesService discoverMoviesService;

    private RecyclerView movies_list_recyclerView;
    private TextView from_date;
    private TextView to_date;
    private Button search;


    private LinearLayoutManager layoutManager;
    private MoviesListAddapter moviesListAddapter;

    private boolean isBusy = false;
    private boolean hasMoreData = false;
    private int totalPages = 0;
    private int currentPageNumber = 1;

    private List<DiscoverResult> discoverResults = new ArrayList<DiscoverResult>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initServices();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=null;
        try{
                // Inflate the layout for this fragment

                 view = inflater.inflate(R.layout.fragment_movie_details, container, false);
                initViews(view);
                initListeners();
                initAdapter();

                getMovies(currentPageNumber, "", "");

        }catch (Exception e){

        }
        return view;
    }

    private void initAdapter() {
        int numberOfColumns = 2;
        movies_list_recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        moviesListAddapter = new MoviesListAddapter(getContext(), discoverResults, MovieListingFragment.this);
        movies_list_recyclerView.setAdapter(moviesListAddapter);
    }

    @Override
    public void initServices() {
        discoverMoviesService = new DiscoverMoviesService();

    }

    @Override
    public void initListeners() {
        initRecyclerViewListener();
        from_date.setOnClickListener(this);
        to_date.setOnClickListener(this);
        search.setOnClickListener(this);

    }


    private void initRecyclerViewListener() {

        movies_list_recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(
                layoutManager) {


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                // TODO Auto-generated method stub
                super.onScrollStateChanged(recyclerView, newState);

            }


            @Override
            public void onLoadMore(int current_page) {
                // TODO Auto-generated method stub
                if (!isBusy) {
                    if (hasMoreData) {

                        if (validateInputs(false)) {
                            getMovies(currentPageNumber, from_date.getText().toString(), to_date.getText().toString());
                        } else {
                            getMovies(currentPageNumber, "", "");
                        }
                    }

                }
            }
        });

    }

    @Override
    public void initViews(View view) {
        movies_list_recyclerView = (RecyclerView) view.findViewById(R.id.movies_list_recyclerView);

        from_date = (TextView) view.findViewById(R.id.from_date);
        to_date = (TextView) view.findViewById(R.id.to_date);
        search = (Button) view.findViewById(R.id.search);

        initMoviesRecyclerView();


    }

    private void initMoviesRecyclerView() {
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        movies_list_recyclerView.setLayoutManager(layoutManager);
        movies_list_recyclerView.setHasFixedSize(true);


    }


    private void getMovies(int pageNumber, String fromDate, String toDate) {

        Progress.show(getActivity());
        isBusy = true;
        discoverMoviesService.getDiscoverMovies(
                getContext(),
                pageNumber,
                fromDate,
                toDate,
                this
        );

    }

    @Override
    public void onDiscoverMoviesSuccess(Discover discoverMovies) {
        Progress.hide();
        isBusy = false;
        totalPages = discoverMovies.getTotalPages();
        if (currentPageNumber < totalPages) {
            hasMoreData = true;
            currentPageNumber++;
        } else {
            hasMoreData = false;
        }

        discoverResults.addAll(discoverMovies.getResults());
        moviesListAddapter.notifyDataSetChanged();


    }

    @Override
    public void onDiscoverMoviesFailure(String errorMessage) {
        Progress.hide();
        isBusy = false;

    }


    @Override
    public void onMovieClicked(DiscoverResult discoverResult, int position) {

        Bundle movieDetails = new Bundle();
        movieDetails.putString("titleText", discoverResult.getTitle());
        movieDetails.putString("detailsText", discoverResult.getOverview());

        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
        movieDetailsFragment.setArguments(movieDetails);
        moveToDetails(movieDetailsFragment);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.from_date:
                DialogFragment fromDate = new DatePickerFragment(this, true);
                fromDate.show(getFragmentManager(), "datePicker");

                break;

            case R.id.to_date:
                DialogFragment toDate = new DatePickerFragment(this, false);
                toDate.show(getFragmentManager(), "datePicker");

                break;

            case R.id.search:

                if (validateInputs(true)) {
                    resetPagination();
                    getMovies(currentPageNumber, from_date.getText().toString(), to_date.getText().toString());
                }

                break;


        }

    }


    private void resetPagination() {
        isBusy = false;
        hasMoreData = false;
        totalPages = 0;
        currentPageNumber = 1;
        discoverResults.clear();
    }

    @Override
    public void onDateSet(String date, boolean isFromDate) {

        if (isFromDate) {
            from_date.setText(date);
            to_date.setText("");

        } else {
            to_date.setText(date);

        }

    }

    private boolean validateInputs(boolean isShake) {

        if (from_date.getText().toString().length() == 0) {
            if (isShake) {
                FieldShakerAnimation.startShaking(getActivity(), from_date);
            }

            return false;
        }

        if (to_date.getText().toString().length() == 0) {
            if (isShake) {
                FieldShakerAnimation.startShaking(getActivity(), to_date);
            }

            return false;
        }

        return true;
    }

    private void moveToDetails(MovieDetailsFragment movieDetailsFragment) {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.add(R.id.main_fragment, movieDetailsFragment, "MovieDetailsFragment");
        ft.addToBackStack("MovieDetailsFragment");
        ft.commit();

    }

}

