package movie.n.sankari.movieimdb.backend;

import movie.n.sankari.movieimdb.models.responseModels.Discover;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by DELL on 4/29/2017.
 */

public interface IRequestInterface {

    @GET(MovieApiConstants.DISCOVER_MOVIES)
    Call<Discover> getMoviesList(
            @Query("api_key") String apiKey,
            @Query("page") int page,
            @Query("release_date.gte") String fromDate,
            @Query("release_date.lte") String toDate


    );
}
