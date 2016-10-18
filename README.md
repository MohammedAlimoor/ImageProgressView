# ImageProgressView
its library  for android  
ImageView With progress waiting   when  image  load from internet 
its easy and simple
# setup
in general project build.gradle   add 
 ````java
 maven {
            url  "http://dl.bintray.com/mohammedalimoor/maven"
        }
 ````       
    
   in app build.gradle   add 
 ````java
    compile 'com.github.MohammedAlimoor:imageprogressview:0.1.3'`
 ```` 
# how to use
 ````xml
 <alimoor.imageprogressview.ImageProgressView
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       app:srcImage="@mipmap/ic_launcher"
       app:hideProgressBar="false"
       app:scaleType="fitCenter"
       app:progressType="Large"
       app:plaseholderImage="@mipmap/ic_launcher"
       app:plaseholderImageError="@mipmap/ic_launcher"
       app:progressBarColor="#FF00"
       app:link="https://cdn3.pcadvisor.co.uk/cmsdata/features/3420161/Android_800_thumb800.jpg"

       ></alimoor.imageprogressview.ImageProgressView>
 ````
