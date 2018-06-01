package indre.chimoutite.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by indre on 4/28/18. Custom adapter for film object and recycler view.
 */

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.ViewHolder> {
    private ArrayList<Film> film;
    private Context context;
    private ItemClickListener mClickListener;

    public FilmAdapter(Context context, ArrayList<Film> film) {
        this.film = film;
        this.context = context;
    }

    @Override
    public FilmAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilmAdapter.ViewHolder viewHolder, int i) {

        Picasso.with(context)
                .load(film.get(i).getmPosterUrl()).resize(185, 277)
                .into(viewHolder.filmPosterMain);
    }

    @Override
    public int getItemCount() {
        return film.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView filmPosterMain;
        public ViewHolder(View view) {
            super(view);
            filmPosterMain = view.findViewById(R.id.filmPosterMain);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("PosterURL", film.get(getAdapterPosition()).getmPosterUrl());
            intent.putExtra("ReleaseDate", film.get(getAdapterPosition()).getmReleaseDate());
            intent.putExtra("VoterAvg", film.get(getAdapterPosition()).getmVoteAverage());
            intent.putExtra("Overview", film.get(getAdapterPosition()).getmOverview());
            intent.putExtra("Title", film.get(getAdapterPosition()).getmTitle());
            intent.putExtra("id", film.get(getAdapterPosition()).getmId());
            context.startActivity(intent);
        }
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // Since recyclerview doesn't have clear or add all functionality make a new method
    public void swap(List<Film> newFilms) {
        if (film != null) {
            film.clear();
            film.addAll(newFilms);
        }
        notifyDataSetChanged();
    }
}
