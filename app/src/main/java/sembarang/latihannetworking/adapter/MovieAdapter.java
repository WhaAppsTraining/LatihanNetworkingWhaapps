package sembarang.latihannetworking.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;
import sembarang.latihannetworking.R;
import sembarang.latihannetworking.model.Movie;
import sembarang.latihannetworking.network.UrlComposer;

/**
 * @author hendrawd on 06/05/18
 */
public class MovieAdapter extends
        RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;

    public MovieAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    // method yang dipanggil secara otomatis oleh RecyclerView
    // untuk membuat layout tiap view item
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                              int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(
                R.layout.item_movie,
                parent,
                false
        );
        return new MovieViewHolder(itemView);
    }

    // method yang dipanggil secara otomatis oleh RecyclerView
    // untuk reuse ViewHolder yang sudah dibuat di onCreateViewHolder
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder,
                                 int position) {
        // mendapatkan object movie pada posisi yang direquest
        // oleh RecyclerView
        Movie movie = movieList.get(position);

        // bind data ke view
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPoster;
        private TextView tvTitle;
        private TextView tvDescription;

        MovieViewHolder(View itemView) {
            super(itemView);
            // mendapatkan reference dari views
            ivPoster = itemView.findViewById(R.id.imageView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvOverview);
        }

        private void bind(Movie movie) {
            String posterUrl = UrlComposer.getPosterUrl(
                    movie.getPosterPath()
            );

            // load image dari url dengan menggunakan glide
            Glide.with(itemView.getContext())
                    .load(posterUrl)
                    .apply(RequestOptions.bitmapTransform(
                            new SketchFilterTransformation()
                    ))
                    .into(ivPoster);

            // set texts
            tvTitle.setText(movie.getTitle());
            tvDescription.setText(movie.getOverview());
        }
    }
}
