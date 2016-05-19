package com.kunlun.breadtrip.recommend;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kunlun.breadtrip.R;
import com.kunlun.breadtrip.bean.RecommendBean;

import java.util.List;

/**
 * Created by dllo on 16/5/13.
 * ListView的适配器
 */
public class ListAdapter extends BaseAdapter {
    private Context context;
    List<RecommendBean> recommendBeen;

    public ListAdapter(Context context) {
        this.context = context;
    }


    public void setRecommendBeen(List<RecommendBean> recommendBeen) {
        this.recommendBeen = recommendBeen;
        Log.d("数量","++++++"+recommendBeen.size());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return recommendBeen == null ? 0 : recommendBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_recommend, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.dateTv.setText(recommendBeen.get(position).getStart_date() + " / "
                + recommendBeen.get(position).getDays() + "天 / " +
                recommendBeen.get(position).getPhotos_count() + "图");
        holder.contentTv.setText(recommendBeen.get(position).getName());
        holder.authorTv.setText("by "+recommendBeen.get(position).getUser().getName());


        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(recommendBeen.get(position).getFront_cover_photo_url())
                .build();
        DraweeController controllerSecond = Fresco.newDraweeControllerBuilder()
                .setUri(recommendBeen.get(position).getUser().getImage())
                .build();

        holder.coverSdv.setController(controller);
        holder.headSdv.setController(controllerSecond);

        return convertView;
    }

    class ViewHolder {
        SimpleDraweeView headSdv, coverSdv;
        TextView dateTv, contentTv, authorTv;

        public ViewHolder(View itemView) {
            headSdv = (SimpleDraweeView) itemView.findViewById(R.id.recommend_item_head_sdv);
            coverSdv = (SimpleDraweeView) itemView.findViewById(R.id.recommend_item_cover_sdv);
            dateTv = (TextView) itemView.findViewById(R.id.recommend_item_date_tv);
            contentTv = (TextView) itemView.findViewById(R.id.recommend_item_content_tv);
            authorTv = (TextView) itemView.findViewById(R.id.recommend_item_author_tv);
        }
    }
}
