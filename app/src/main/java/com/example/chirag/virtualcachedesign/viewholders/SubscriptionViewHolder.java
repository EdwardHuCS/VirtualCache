package com.example.chirag.virtualcachedesign.viewholders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.chirag.virtualcachedesign.BuildConfig;
import com.example.chirag.virtualcachedesign.R;
import com.example.chirag.virtualcachedesign.service.SubListItem;
import com.example.chirag.virtualcachedesign.activities.MainActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by chirag on 4/1/17.
 */

public class SubscriptionViewHolder extends RecyclerView.ViewHolder{

    private Context context;

    public SubscriptionViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
    }

    public void setSubscriptionImage(final SubListItem.Item item) {
        ImageView imageView1 = (ImageView) itemView.findViewById(R.id.photo);
            Picasso.with(context).load(item.snippet.thumbnails.medium.url)
                    .error(R.drawable.us)
                    .placeholder(R.drawable.us)
                    .transform(new RoundedTransformation()).into(imageView1);

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).fetchChannels(item);
            }
        });
    }

    public class RoundedTransformation implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            try {

                int size = Math.min(source.getWidth(), source.getHeight());

                int x = (source.getWidth() - size) / 2;
                int y = (source.getHeight() - size) / 2;

                Bitmap squaredBitmap = Bitmap
                        .createBitmap(source, x, y, size, size);
                if (squaredBitmap != source) {
                    source.recycle();
                }

                Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

                Canvas canvas = new Canvas(bitmap);
                Paint paint = new Paint();
                BitmapShader shader = new BitmapShader(squaredBitmap,
                        BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
                paint.setShader(shader);
                paint.setAntiAlias(true);

                float r = size / 2f;
                canvas.drawCircle(r, r, r, paint);

                squaredBitmap.recycle();
                return bitmap;
            } catch (Exception e) {
                // TODO: handle exception
                if (BuildConfig.DEBUG)
                    e.printStackTrace();
                return null;
            }

        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
