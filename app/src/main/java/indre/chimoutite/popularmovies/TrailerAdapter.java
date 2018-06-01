package indre.chimoutite.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by indre on 5/31/18. Custom adapter for trailers and recycler view.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    private ArrayList<String> trailer;
    private Context context;
    TextView trailerTitle;
    private ItemClickListener mClickListener;

    public TrailerAdapter(Context context, ArrayList<String> trailer) {
        this.trailer = trailer;
        this.context = context;
    }

    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trailer_layout, viewGroup, false);
        trailerTitle = (TextView) view.findViewById(R.id.trailer_number);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.ViewHolder viewHolder, int i) {
        trailerTitle.setText("Trailer " + (viewHolder.getAdapterPosition() + 1));
    }

    @Override
    public int getItemCount() {
        return trailer.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            watchYoutubeTrailer(context, trailer.get(getAdapterPosition()));
        }
    }

    // Since recyclerview doesn't have clear or add all functionality make a new method
    public void swap(ArrayList<String> newTrailers) {
        if (trailer != null) {
            trailer.clear();
            trailer.addAll(newTrailers);
        }
        notifyDataSetChanged();
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private static void watchYoutubeTrailer(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));

        // If there is a Youtube app on the device then launch it otherwise launch website
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
}
