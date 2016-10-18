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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;


/**
 * Created by Mohammed Alimoor on 15/08/16.
 *
 * ameral.java@gmail   00970598072007
 *
 */
public class ImageProgressView extends RelativeLayout {


    public VolleyImageView imageView;
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

    void LoadView() {
        imageView  = new VolleyImageView(getContext());
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
            imageView.setImageResource(src);
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

            imageView.setImageUrl(link, VolleyImageView.VolleySingleton.getInstance(getContext()).getImageLoader());
            imageView.setResponseObserver(new VolleyImageView.ResponseObserver() {
                @Override
                public void onError() {
                    Log.d(this.getClass().getName(),"Image Load Error ");
                    progressBar.setVisibility(GONE);
                }

                @Override
                public void onSuccess() {
                    Log.d(this.getClass().getName(),"Image Load Success ");
                    progressBar.setVisibility(GONE);
                }
            });
        }


    }



    //-------------------
    public static class VolleyImageView extends ImageView {

        public static class VolleySingleton {
            public  static VolleyImageView.VolleySingleton mInstance = null;
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

            public static VolleyImageView.VolleySingleton getInstance(Context context) {
                if (mInstance == null) {
                    mInstance = new VolleyImageView.VolleySingleton(context);
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

        public interface ResponseObserver
        {
            public void onError();
            public void onSuccess();
        }

        private VolleyImageView.ResponseObserver mObserver;

        public void setResponseObserver(VolleyImageView.ResponseObserver observer) {
            mObserver = observer;
        }

        /**
         * The URL of the network image to load
         */
        private String mUrl;

        /**
         * Resource ID of the image to be used as a placeholder until the network image is loaded.
         */
        private int mDefaultImageId;

        /**
         * Resource ID of the image to be used if the network response fails.
         */
        private int mErrorImageId;

        /**
         * Local copy of the ImageLoader.
         */
        private ImageLoader mImageLoader;

        /**
         * Current ImageContainer. (either in-flight or finished)
         */
        private ImageLoader.ImageContainer mImageContainer;

        public VolleyImageView(Context context) {
            this(context, null);
        }

        public VolleyImageView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public VolleyImageView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        /**
         * Sets URL of the image that should be loaded into this view. Note that calling this will
         * immediately either set the cached image (if available) or the default image specified by
         * {@link VolleyImageView#setDefaultImageResId(int)} on the view.
         *
         * NOTE: If applicable, {@link VolleyImageView# setDefaultImageResId(int)} and {@link
         * VolleyImageView#setErrorImageResId(int)} should be called prior to calling this function.
         *
         * @param url         The URL that should be loaded into this ImageView.
         * @param imageLoader ImageLoader that will be used to make the request.
         */
        public void setImageUrl(String url, ImageLoader imageLoader) {
            mUrl = url;
            mImageLoader = imageLoader;
            // The URL has potentially changed. See if we need to load it.
            loadImageIfNecessary(false);
        }

        /**
         * Sets the default image resource ID to be used for this view until the attempt to load it
         * completes.
         */
        public void setDefaultImageResId(int defaultImage) {
            mDefaultImageId = defaultImage;
        }

        /**
         * Sets the error image resource ID to be used for this view in the event that the image
         * requested fails to load.
         */
        public void setErrorImageResId(int errorImage) {
            mErrorImageId = errorImage;
        }

        /**
         * Loads the image for the view if it isn't already loaded.
         *
         * @param isInLayoutPass True if this was invoked from a layout pass, false otherwise.
         */
        private void loadImageIfNecessary(final boolean isInLayoutPass) {
            int width = getWidth();
            int height = getHeight();

            boolean isFullyWrapContent = getLayoutParams() != null
                    && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT
                    && getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT;
            // if the view's bounds aren't known yet, and this is not a wrap-content/wrap-content
            // view, hold off on loading the image.
            if (width == 0 && height == 0 && !isFullyWrapContent) {
                return;
            }

            // if the URL to be loaded in this view is empty, cancel any old requests and clear the
            // currently loaded image.
            if (TextUtils.isEmpty(mUrl)) {
                if (mImageContainer != null) {
                    mImageContainer.cancelRequest();
                    mImageContainer = null;
                }
                setDefaultImageOrNull();
                return;
            }

            // if there was an old request in this view, check if it needs to be canceled.
            if (mImageContainer != null && mImageContainer.getRequestUrl() != null) {
                if (mImageContainer.getRequestUrl().equals(mUrl)) {
                    // if the request is from the same URL, return.
                    return;
                } else {
                    // if there is a pre-existing request, cancel it if it's fetching a different URL.
                    mImageContainer.cancelRequest();
                    setDefaultImageOrNull();
                }
            }

            // The pre-existing content of this view didn't match the current URL. Load the new image
            // from the network.
            ImageLoader.ImageContainer newContainer = mImageLoader.get(mUrl,
                    new ImageLoader.ImageListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (mErrorImageId != 0) {
                                setImageResource(mErrorImageId);
                            }

                            if(mObserver!=null)
                            {
                                mObserver.onError();
                            }
                        }

                        @Override
                        public void onResponse(final ImageLoader.ImageContainer response, boolean isImmediate) {
                            // If this was an immediate response that was delivered inside of a layout
                            // pass do not set the image immediately as it will trigger a requestLayout
                            // inside of a layout. Instead, defer setting the image by posting back to
                            // the main thread.
                            if (isImmediate && isInLayoutPass) {
                                post(new Runnable() {
                                    @Override
                                    public void run() {
                                        onResponse(response, false);
                                    }
                                });
                                return;
                            }

                            if (response.getBitmap() != null) {
                                setImageBitmap(response.getBitmap());
                            } else if (mDefaultImageId != 0) {
                                setImageResource(mDefaultImageId);
                            }

                            if(mObserver!=null)
                            {
                                mObserver.onSuccess();
                            }
                        }
                    });

            // update the ImageContainer to be the new bitmap container.
            mImageContainer = newContainer;
        }

        private void setDefaultImageOrNull() {
            if (mDefaultImageId != 0) {
                setImageResource(mDefaultImageId);
            } else {
                setImageBitmap(null);
            }
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            loadImageIfNecessary(true);
        }

        @Override
        protected void onDetachedFromWindow() {
            if (mImageContainer != null) {
                // If the view was bound to an image request, cancel it and clear
                // out the image from the view.
                mImageContainer.cancelRequest();
                setImageBitmap(null);
                // also clear out the container so we can reload the image if necessary.
                mImageContainer = null;
            }
            super.onDetachedFromWindow();
        }

        @Override
        protected void drawableStateChanged() {
            super.drawableStateChanged();
            invalidate();
        }




    }
}

