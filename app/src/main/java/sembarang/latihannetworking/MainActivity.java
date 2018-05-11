package sembarang.latihannetworking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sembarang.latihannetworking.adapter.MovieAdapter;
import sembarang.latihannetworking.model.Movie;
import sembarang.latihannetworking.model.MovieResponse;
import sembarang.latihannetworking.network.MovieService;
import sembarang.latihannetworking.network.ServiceGenerator;

public class MainActivity extends AppCompatActivity implements Callback<MovieResponse> {

    private static final String CATEGORY_POPULAR = "popular";
    private static final String CATEGORY_TOP_RATED = "top_rated";
    private Call<MovieResponse> mCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestMovieList(CATEGORY_POPULAR);
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
        mCall.enqueue(MainActivity.this);

        // request secara synchronous
        // harus menggunakan thread lain
        // tidak bisa jalan di main thread
        // new Thread(new Runnable() {
        //     @Override
        //     public void run() {
        //         try {
        //             Response<MovieResponse> response = mCall.execute();
        //             final MovieResponse movieResponse = response.body();
        //             MainActivity.this.runOnUiThread(
        //                     new Runnable() {
        //                         @Override
        //                         public void run() {
        //                             showMovies(movieResponse);
        //                         }
        //                     }
        //             );
        //             // atau
        //             // new Handler(getMainLooper()).post(new Runnable() {
        //             //     @Override
        //             //     public void run() {
        //             //         showMovies(movieResponse);
        //             //     }
        //             // });
        //         } catch (IOException e) {
        //             e.printStackTrace();
        //         }
        //     }
        // }).start();
    }

    @Override
    public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
        MovieResponse movieResponse = response.body();
        showMovies(movieResponse);
    }

    private void showMovies(MovieResponse movieResponse) {
        if (movieResponse != null) {
            List<Movie> movieList = movieResponse.getResults();
            // jangan memakan string concatenation di dalam loop
            // karena penggunaan memory lebih besar, dan performa lebih jelek
            // mendapatkan reference dari recyclerview di layout xml
            RecyclerView recyclerView = findViewById(R.id.recyclerView);

            // membuat layout manager yang akan digunakan untuk
            // RecyclerView
            LinearLayoutManager linearLayoutManager =
                    new LinearLayoutManager(this);

            // set layout manager yang sudah dibikin di atas
            // ke RecyclerView
            recyclerView.setLayoutManager(linearLayoutManager);

            // membuat item decoration untuk memisahkan tiap item
            // membuat DividerItemDecoration
            DividerItemDecoration dividerItemDecoration =
                    new DividerItemDecoration(
                            this,
                            DividerItemDecoration.VERTICAL
                    );
            // set item decoration ke RecyclerView
            recyclerView.addItemDecoration(dividerItemDecoration);

            // membuat adapter
            MovieAdapter movieAdapter = new MovieAdapter(movieList);

            // set adapter ke RecyclerView
            recyclerView.setAdapter(movieAdapter);
        }
    }

    @Override
    public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {

    }


}
