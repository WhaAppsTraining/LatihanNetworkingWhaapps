package sembarang.latihannetworking.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import sembarang.latihannetworking.model.MovieResponse;
import sembarang.latihannetworking.model.ReviewResponse;
import sembarang.latihannetworking.model.VideoResponse;

/**
 * @author hendrawd on 29/04/18
 */
public interface MovieService {
    @GET("movie/{category}")
    Call<MovieResponse> getMovies(@Path("category") String category);

    @GET("movie/{movie_id}/reviews?language=en-US&page=1")
    Call<ReviewResponse> getReviews(@Path("movie_id") Integer movieId);

    @GET("movie/{movie_id}/videos")
    Call<VideoResponse> getVideos(@Path("movie_id") Integer movieId);
}
