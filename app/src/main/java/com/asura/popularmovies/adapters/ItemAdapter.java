package com.asura.popularmovies.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asura.popularmovies.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemCardHolder>{

    public static final int TRAILER = 500;
    public static final int REVIEW = 600;

    private Context mCtx;
    private int mType;

    private List<String> mList;

    public ItemAdapter(Context ctx, int type, List<String> typeList){
        mCtx = ctx;
        mType = type;
        mList = typeList;
    }

    @Override
    public ItemCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        return new ItemCardHolder(itemView);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(ItemCardHolder holder, int position) {
        holder.link = mList.get(position);

        int realPos = position + 1;
        switch (mType){
            case TRAILER:
                holder.cardImage.setImageDrawable(mCtx.getDrawable(R.drawable.ic_play_arrow_black_24px));
                holder.cardText.setText(mCtx.getText(R.string.trailer) + " " + realPos);
                break;
            case REVIEW:
                holder.cardImage.setImageDrawable(mCtx.getDrawable(R.drawable.ic_rate_review_24));
                holder.cardText.setText(mCtx.getText(R.string.review) + " " + realPos );
                break;
            default:
                throw new UnsupportedOperationException("Unknown Adapter type");
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ItemCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.card_image)
        public ImageView cardImage;

        @BindView(R.id.card_text)
        public TextView cardText;

        public String link;

        public ItemCardHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            mCtx.startActivity(intent);
        }

    }
}
