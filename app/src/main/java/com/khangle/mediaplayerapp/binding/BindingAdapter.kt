package com.khangle.mediaplayerapp.binding

import android.text.Html
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.BindingAdapter
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.data.model.Track
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import jp.wasabeef.picasso.transformations.BlurTransformation

private const val TAG = "BindingAdapter"
@BindingAdapter("image_url")
fun setImageUrl(view: ImageView, path: String?) {
   if (path!= null && !path.equals("")) {
        Picasso.get().load(path).error(R.drawable.ic_launcher_foreground).into(view) // khi error thi hien con android
    }

}

@BindingAdapter("bkg_blur_url", "target")
fun setImageUrlBlur(view: MotionLayout, path: String?, target: Target?) {
    Log.i(TAG, "setImageUrlBlur: ")
    if (path!= null && target != null) {
        Picasso.get().load(path).error(R.drawable.ic_launcher_foreground) .transform(BlurTransformation(view.context, 100, 1)).into(target) // khi error thi hien con android
    }

}

@BindingAdapter("subtitle", "title")
fun setTrackItemArtist(view: TextView, subtitle: String?, title: String?) {

   view.text =  Html.fromHtml("<b>" + title + "</b>" +  "<br />" +
           "<small>  <font color='grey'>" + subtitle + "</font></small>")
}

@BindingAdapter("track")
fun setTrack(view: TextView, track: Track?) {

    view.text = Html.fromHtml("<b>" + track?.title + "</b>" +  "<br />" +
            "<small> <font color='grey'>" + track?.artist?.name + "</font></small>")

}

