package movie.n.sankari.movieimdb.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import movie.n.sankari.movieimdb.R;
import movie.n.sankari.movieimdb.backend.MovieApiConstants;
import movie.n.sankari.movieimdb.models.responseModels.DiscoverResult;

/**
 * Created by DELL on 4/29/2017.
 */

public class MoviesListAddapter extends RecyclerView.Adapter<MoviesListAddapter.ViewHolder> {


    Context context;
    List<DiscoverResult> moviesList;
    IItemClicked iItemClicked;


    public MoviesListAddapter(Context context, List<DiscoverResult> contents, IItemClicked iItemClicked) {
        super();
        this.context = context;
        this.moviesList = contents;
        this.iItemClicked = iItemClicked;
    }

    public interface IItemClicked {
        void onMovieClicked(DiscoverResult discoverResult, int position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_movies_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {


        try {


            viewHolder.movie_name.setText(this.moviesList.get(i).getTitle());

            if (this.moviesList.get(i).getPosterPath() != null) {

                Picasso.
                        with(context).
                        load(MovieApiConstants.IMAGES_DB_PRE_PATH + this.moviesList.get(i).getPosterPath()).
                        placeholder(R.drawable.movie_place_holder_image).
                        into(viewHolder.movie_banner);

            }


            viewHolder.movie_banner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iItemClicked.onMovieClicked(moviesList.get(i), i);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return moviesList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView movie_name;
        public ImageView movie_banner;

        public ViewHolder(View itemView) {
            super(itemView);
            movie_name = (TextView) itemView.findViewById(R.id.movie_name);
            movie_banner = (ImageView) itemView.findViewById(R.id.movie_banner);


        }


    }
}
