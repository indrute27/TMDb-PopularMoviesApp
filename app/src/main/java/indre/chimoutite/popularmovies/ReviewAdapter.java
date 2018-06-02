package indre.chimoutite.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by indre on 6/2/18. Custom adapter for reviews and recycler view.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private List<Review> review;
    private Context context;
    TextView author;
    TextView content;

    public ReviewAdapter(Context context, List<Review> review) {
        this.review = review;
        this.context = context;
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_layout, viewGroup, false);
        author = (TextView) view.findViewById(R.id.author);
        content = (TextView) view.findViewById(R.id.content);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder viewHolder, int i) {
        author.setText("Review by " + review.get(i).getmAuthor());
        content.setText(review.get(i).getmContent());
    }

    @Override
    public int getItemCount() {
        return review.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View view) {
            super(view);
        }
    }

    // Since recyclerview doesn't have clear or add all functionality make a new method
    public void swap(List<Review> newReviews) {
        if (review != null) {
            review.clear();
            review.addAll(newReviews);
        }
        notifyDataSetChanged();
    }
}
