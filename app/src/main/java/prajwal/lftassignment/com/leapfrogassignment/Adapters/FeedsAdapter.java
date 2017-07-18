package prajwal.lftassignment.com.leapfrogassignment.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import prajwal.lftassignment.com.leapfrogassignment.CustomViews.StarImageView;
import prajwal.lftassignment.com.leapfrogassignment.Model.Feeds;
import prajwal.lftassignment.com.leapfrogassignment.R;

/**
 * Created by prajwal on 7/17/2017.
 */

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.FeedsViewHolder> {
    private List<Feeds> mFeedsList;
    private Context mContext;
    private Feeds feedsItem;

    public FeedsAdapter(Context context, List<Feeds> feedsList) {
        this.mContext = context;
        this.mFeedsList = feedsList;
    }


    @Override
    public FeedsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feed, parent, false);
        FeedsViewHolder feedsViewHolder = new FeedsViewHolder(v);
        return feedsViewHolder;
    }

    @Override
    public void onBindViewHolder(FeedsViewHolder holder, int position) {
        feedsItem = mFeedsList.get(position);
        holder.tvUserName.setText(feedsItem.getUserName().toString());
        holder.tvDescription.setText(feedsItem.getStatus().toString());
        Glide.with(mContext).load(feedsItem.getFeedImage()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.ivFeedImage);
        Glide.with(mContext).load(feedsItem.getUserImage()).fitCenter().override(80,80).diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.ivUserImage);
    }

    @Override
    public int getItemCount() {
        return mFeedsList.size();
    }

    public class FeedsViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUserName, tvDescription;
        private StarImageView ivUserImage;
        private ImageView ivFeedImage;

        public FeedsViewHolder(View itemView) {
            super(itemView);
            tvUserName = (TextView) itemView.findViewById(R.id.row_tv_username);
            tvDescription = (TextView) itemView.findViewById(R.id.row_tv_feed_description);
            ivUserImage = (StarImageView) itemView.findViewById(R.id.row_iv_profile);
            ivFeedImage = (ImageView) itemView.findViewById(R.id.row_iv_feed_image);
        }
    }
}
