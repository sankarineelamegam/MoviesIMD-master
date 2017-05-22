package movie.n.sankari.movieimdb.services;

import android.content.Context;

import org.json.JSONObject;

import movie.n.sankari.movieimdb.R;
import movie.n.sankari.movieimdb.backend.IRequestInterface;
import movie.n.sankari.movieimdb.backend.MovieApiConstants;
import movie.n.sankari.movieimdb.models.responseModels.Discover;
import movie.n.sankari.movieimdb.modules.MovieServerCommunicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 4/29/2017.
 */

public class DiscoverMoviesService {

    public interface DiscoverMoviesCallback {
        void onDiscoverMoviesSuccess(Discover discoverMovies);

        void onDiscoverMoviesFailure(String errorMessage);
    }

    public void getDiscoverMovies(
            final Context context,
            int pageNumber,
            String fromDate,
            String toDate,
            final DiscoverMoviesCallback discoverMoviesCallback

    ) {
        Call<Discover> availableMovies = MovieServerCommunicator.createService(IRequestInterface.class).getMoviesList(
                MovieApiConstants.API_KEY,
                pageNumber,
                fromDate,
                toDate
        );
        availableMovies.enqueue(new Callback<Discover>() {
            @Override
            public void onResponse(Call<Discover> call, Response<Discover> response) {

                if (response != null) {
                    try {

                        if (response.isSuccessful()) {
                            if (response.body().getResults() != null && response.body().getResults().size() > 0) {
                                discoverMoviesCallback.onDiscoverMoviesSuccess(response.body());
                            } else {
                                discoverMoviesCallback.onDiscoverMoviesFailure(context.getString(R.string.no_movies_available));
                            }

                            return;
                        } else {
                            if (response.errorBody() != null) {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                discoverMoviesCallback.onDiscoverMoviesFailure(jObjError.getString(MovieApiConstants.ERROR_RESPONSE_ATTRIBUTE));
                                return;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
                discoverMoviesCallback.onDiscoverMoviesFailure(context.getString(R.string.general_error));
            }

            @Override
            public void onFailure(Call<Discover> call, Throwable t) {
                discoverMoviesCallback.onDiscoverMoviesFailure(context.getString(R.string.server_not_responding));
            }
        });


    }
}
