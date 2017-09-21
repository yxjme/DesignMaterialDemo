package com.deringmobile.jbh.designmaterialdemo.net;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.deringmobile.jbh.designmaterialdemo.R;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * Created by zbsdata on 2017/8/29.
 */

public class LoadImageManager {

    private static LoadImageManager manager;

    public static LoadImageManager newStanence(){
        if(manager==null){
            manager=new LoadImageManager();
        }
        return manager;
    }

    public void loadImage(String url, ImageView imageView){
        ImageLoader.getInstance().displayImage(url,imageView);
    }


    public void loadImage(final Context context, String url, int w, int h, final ImageView imge){
        RequestOptions opts=new RequestOptions();
        opts.diskCacheStrategy(DiskCacheStrategy.ALL);
        opts.error(R.mipmap.ic_launcher);
        opts.placeholder(R.mipmap.ic_launcher);
//                opts.transform(new CircleCrop(MainActivity.this));//
        Glide.with(context)
                .load(url)
                .apply(opts)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (imge == null) {
                            return false;
                        }
                        if (imge.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            imge.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        ViewGroup.LayoutParams params = imge.getLayoutParams();
                        int vw = imge.getWidth() - imge.getPaddingLeft() - imge.getPaddingRight();
                        float scale = (float) vw / (float) resource.getIntrinsicWidth();
                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
                        params.height = vh + imge.getPaddingTop() + imge.getPaddingBottom();

                        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                        int width=displayMetrics.widthPixels;
                        int height=displayMetrics.heightPixels;
                        params.height=width/2-imge.getPaddingLeft()-imge.getPaddingRight();
                        params.width=width/2-imge.getPaddingTop()-imge.getPaddingBottom();
                        imge.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imge.setLayoutParams(params);
                        return false;
                    }
                })
                .into(imge);
    }
}
