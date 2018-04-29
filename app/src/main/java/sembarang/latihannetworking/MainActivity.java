package sembarang.latihannetworking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sembarang.latihannetworking.model.Movie;
import sembarang.latihannetworking.model.MovieResponse;
import sembarang.latihannetworking.network.MovieService;
import sembarang.latihannetworking.network.ServiceGenerator;

public class MainActivity extends AppCompatActivity implements Callback<MovieResponse> {

    private Call<MovieResponse> mCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // requestMovieList("popular");
        requestMovieList("top_rated");
    }

    /**
     * Mendapatkan movie list dari network menggunakan retrofit
     */
    private void requestMovieList(String category) {
        mCall = ServiceGenerator
                .createService(MovieService.class)
                .getMovies(category);

        // Kenapa implement interface daripada memakai anonymous class?
        // Berasal dari buku Effective Java, Item 5: Avoid creating unnecessary objects
        mCall.enqueue(MainActivity.this);
    }

    @Override
    public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
        MovieResponse movieResponse = response.body();
        if (movieResponse != null) {
            List<Movie> movieList = movieResponse.getResults();
            for (Movie movie : movieList) {
                Log.d("MovieName", movie.getTitle());
            }
        }
    }

    @Override
    public void onFailure(Call<MovieResponse> call, Throwable t) {

    }


}
