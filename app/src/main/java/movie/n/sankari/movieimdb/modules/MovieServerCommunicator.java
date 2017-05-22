package movie.n.sankari.movieimdb.modules;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import movie.n.sankari.movieimdb.backend.MovieApiConstants;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DELL on 4/29/2017.
 */

public class MovieServerCommunicator {

    public static final String API_BASE_URL = MovieApiConstants.API_ENDPOINT;

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }

    public static <S> S createService(Class<S> serviceClass, final String authToken) {
        if (authToken != null) {
            httpClient.interceptors().add(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", authToken)
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.interceptors().add(logging);

        httpClient.readTimeout(180, TimeUnit.SECONDS);
        httpClient.writeTimeout(180, TimeUnit.SECONDS);

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = builder.client(client).build();

        return retrofit.create(serviceClass);
    }
}
