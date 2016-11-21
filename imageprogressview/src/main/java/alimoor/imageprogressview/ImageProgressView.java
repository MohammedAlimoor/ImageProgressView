package alimoor.imageprogressview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;


/**
 * Created by Mohammed Alimoor on 15/08/16.
 *
 * ameral.java@gmail   00970598072007
 *
 */
public class ImageProgressView extends RelativeLayout {


    public ManagedNetworkImageView imageView;
    public ProgressBar progressBar;

    ImageView.ScaleType image_scaleType;
    int PlaseHolderImage = 0;
    int PlaseHolderImage_error = 0;
    int progressViewStyle = 0;
    int src = 0;
    int progressBarColor = 0;
    boolean hideProgresspar = false;
    String link = "";

    public ImageProgressView(Context context) {
        super(context);
        initControl(context);
    }

    public ImageProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAttrs(context,attrs);
        initControl(context);

    }

    public ImageProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttrs(context,attrs);
        initControl(context);
    }


    public void loadAttrs(Context context, AttributeSet attrs){
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ImageProgressView);
        int scaleType = array.getInt(R.styleable.ImageProgressView_scaleType, 0);
        hideProgresspar = array.getBoolean(R.styleable.ImageProgressView_hideProgressBar, false);
        link = array.getString(R.styleable.ImageProgressView_link);
        PlaseHolderImage = array.getResourceId(R.styleable.ImageProgressView_plaseholderImage, 0);
        PlaseHolderImage_error = array.getResourceId(R.styleable.ImageProgressView_plaseholderImageError, 0);
        progressBarColor = array.getColor(R.styleable.ImageProgressView_progressBarColor, 0);
        src = array.getResourceId(R.styleable.ImageProgressView_srcImage, 0);
        int progressType = array.getInt(R.styleable.ImageProgressView_progressType, 0);
        switch (progressType) {
            case 1:
                progressViewStyle = android.R.attr.progressBarStyleHorizontal;
                break;
            case 2:
                progressViewStyle = android.R.attr.progressBarStyleInverse;

                break;
            case 3:
                progressViewStyle = android.R.attr.progressBarStyleLarge;

                break;
            case 4:
                progressViewStyle = android.R.attr.progressBarStyleLargeInverse;
                break;
            case 5:
                progressViewStyle = android.R.attr.progressBarStyleSmall;
                break;
            case 6:
                progressViewStyle = android.R.attr.progressBarStyleSmallInverse;
                break;
            case 7:
                progressViewStyle = android.R.attr.progressBarStyleSmallTitle;
                break;

        }
        switch (scaleType) {
            case 1:
                image_scaleType = ImageView.ScaleType.CENTER;
                break;
            case 2:
                image_scaleType = ImageView.ScaleType.CENTER_CROP;

                break;
            case 3:
                image_scaleType = ImageView.ScaleType.CENTER_INSIDE;

                break;
            case 4:
                image_scaleType = ImageView.ScaleType.FIT_CENTER;
                break;
            case 5:
                image_scaleType = ImageView.ScaleType.FIT_XY;
                break;

        }
    }
    private void initControl(Context context) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

      //  inflater.inflate(R.layout.view_imageview_progressbar, this);
        LoadView();

    }

    public void setLink(String link) {
        this.link = link;
        LoadView();
    }

    void LoadView() {
        imageView  = new ManagedNetworkImageView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
       // params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        this.addView(imageView, params);
        LayoutParams paramsProgressBar = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsProgressBar.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        //   params.addRule(RelativeLayout.ALIGN_
        //  PARENT_LEFT, RelativeLayout.TRUE);
       //  params.leftMargin = 107


        if (progressViewStyle != 0) {
            progressBar = new ProgressBar(
                    getContext(),
                    null,
                    progressViewStyle);
        }else{
            progressBar = new ProgressBar(
                    getContext(),
                    null,
                    android.R.attr.progressBarStyleInverse);
        }


        this.addView(progressBar,paramsProgressBar);

        if (image_scaleType != null) {
            imageView.setScaleType(image_scaleType);
        }



        if (progressBarColor != 0) {
            progressBar.getIndeterminateDrawable().setColorFilter(progressBarColor, PorterDuff.Mode.MULTIPLY);
        }
        if (PlaseHolderImage_error!= 0) {
            imageView.setErrorImageResId(PlaseHolderImage_error);
        }
        if (src != 0) {
            //imageView.setImageResource(src);
            imageView.setDefaultImageResId(src);

        }
        if(hideProgresspar == true){
            progressBar.setVisibility(INVISIBLE);
        }
        if (link!=null && !link.toString().isEmpty()){
            progressBar.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(VISIBLE);

                }
            });

            if (!isInEditMode()){

                imageView.setImageUrl(link, VolleySingleton.getInstance(getContext()).getImageLoader());
            }


            imageView.setResponseListener(new ManagedNetworkImageView.ResponseListener() {
                @Override
                public void onError() {
                    Log.w(this.getClass().getName(),"Image Load Error ");
                    progressBar.setVisibility(GONE);
                }

                @Override
                public void onSuccess() {
                    Log.w(this.getClass().getName(),"Image Load Success ");
                    progressBar.setVisibility(GONE);
                }
            });

//            imageView.setResponseObserver(new VolleyImageView.ResponseObserver() {
//                @Override
//                public void onError() {
//                    Log.w(this.getClass().getName(),"Image Load Error ");
//                    progressBar.setVisibility(GONE);
//                }
//
//                @Override
//                public void onSuccess() {
//                    Log.w(this.getClass().getName(),"Image Load Success ");
//                    progressBar.setVisibility(GONE);
//                }
//            });
        }


    }



    protected static class ManagedNetworkImageView extends NetworkImageView {
        private int mErrorResId;

        protected interface ResponseListener
        {
            public void onError();
            public void onSuccess();
        }

        ResponseListener responseListener;

        public void setResponseListener(ResponseListener responseListener) {
            this.responseListener = responseListener;
        }

        public ManagedNetworkImageView(Context context) {
            super(context);
        }

        public ManagedNetworkImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public ManagedNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        public void setErrorImageResId(int errorImage) {
            mErrorResId = errorImage;
            super.setErrorImageResId(errorImage);
        }

        @Override
        public void setImageResource(int resId) {
            if (resId == mErrorResId) {
                // TODO Handle the error here
                responseListener.onError();
            }
            super.setImageResource(resId);
        }

        @Override
        public void setImageBitmap(Bitmap bm) {
            // TODO Handle the success here
            responseListener.onSuccess();
            super.setImageBitmap(bm);
        }
    }

    private static class VolleySingleton {
        public  static VolleySingleton mInstance = null;
        private RequestQueue mRequestQueue;
        private ImageLoader mImageLoader;

        private VolleySingleton(Context context) {
            // HurlStack stack = new HurlStack(null, TrustAllSSLSocket.createSslSocketFactory());

            //  mRequestQueue = Volley.newRequestQueue(Application.getAppContext(),stack);
            mRequestQueue = Volley.newRequestQueue(context);
            mImageLoader = new ImageLoader(this.mRequestQueue, new ImageLoader.ImageCache() {
                private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);

                public void putBitmap(String url, Bitmap bitmap) {
                    mCache.put(url, bitmap);
                }

                public Bitmap getBitmap(String url) {
                    return mCache.get(url);
                }
            });
        }

        public static VolleySingleton getInstance(Context context) {
            if (mInstance == null) {
                mInstance = new VolleySingleton(context);
            }
            return mInstance;
        }

        public RequestQueue getRequestQueue() {
            return this.mRequestQueue;
        }

        public ImageLoader getImageLoader() {
            return this.mImageLoader;
        }



        public class ImageLoaderCach {
            private RequestQueue mRequestQueue;
            private ImageLoader mImageLoader;

            public ImageLoader getmImageLoader(Context context) {
                mRequestQueue = Volley.newRequestQueue(context);
                mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {

                    private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);

                    public void putBitmap(String url, Bitmap bitmap) {
                        mCache.put(url, bitmap);
                    }

                    public Bitmap getBitmap(String url) {
                        return mCache.get(url);
                    }
                });


                return mImageLoader;
            }
        }

    }


}

