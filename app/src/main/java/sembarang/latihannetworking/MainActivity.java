package sembarang.latihannetworking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.IOException;
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
        // request secara asynchronous
        // mCall.enqueue(MainActivity.this);

        // request secara synchronous
        // harus menggunakan thread lain
        // tidak bisa jalan di main thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<MovieResponse> response = mCall.execute();
                    final MovieResponse movieResponse = response.body();
                    MainActivity.this.runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    showMovies(movieResponse);
                                }
                            }
                    );
                    // atau
                    // new Handler(getMainLooper()).post(new Runnable() {
                    //     @Override
                    //     public void run() {
                    //         showMovies(movieResponse);
                    //     }
                    // });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
        MovieResponse movieResponse = response.body();
        showMovies(movieResponse);
    }

    private void showMovies(MovieResponse movieResponse) {
        TextView textView = findViewById(R.id.tv);
        if (movieResponse != null) {
            List<Movie> movieList = movieResponse.getResults();
            // jangan memakan string concatenation di dalam loop
            // karena penggunaan memory lebih besar, dan performa lebih jelek
            StringBuilder stringBuilder = new StringBuilder();
            for (Movie movie : movieList) {
                // Log.d("MovieName", movie.getTitle());
                // method chaining
                stringBuilder
                        .append(movie.getTitle())
                        .append("\n");
            }
            textView.setText(stringBuilder.toString());
        }
    }

    @Override
    public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {

    }


}
