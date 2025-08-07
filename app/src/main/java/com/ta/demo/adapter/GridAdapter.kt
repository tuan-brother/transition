package com.ta.demo.adapter

import android.graphics.drawable.Drawable
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.ta.demo.R
import com.ta.demo.adapter.ImageData.IMAGE_DRAWABLES
import com.ta.demo.fragment.ImagePagerFragment
import java.util.concurrent.atomic.AtomicBoolean
import com.bumptech.glide.request.target.Target
import com.ta.demo.MainActivity

class GridAdapter(fragment: Fragment) : RecyclerView.Adapter<GridAdapter.ImageViewHolder>() {

    private val requestManager: RequestManager = Glide.with(fragment)
    private val viewHolderListener: ViewHolderListener = ViewHolderListenerImpl(fragment)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_card, parent, false)
        return ImageViewHolder(view, requestManager, viewHolderListener)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount(): Int = IMAGE_DRAWABLES.size

    /**
     * ViewHolder for the grid's images.
     */
    class ImageViewHolder(
        itemView: View,
        private val requestManager: RequestManager,
        private val viewHolderListener: ViewHolderListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val image: ImageView = itemView.findViewById(R.id.card_image)

        init {
            itemView.findViewById<View>(R.id.card_view).setOnClickListener(this)
        }

        fun onBind() {
            val adapterPosition = adapterPosition
            setImage(adapterPosition)
            image.transitionName = IMAGE_DRAWABLES[adapterPosition].toString()
        }

        private fun setImage(adapterPosition: Int) {
            requestManager
                .load(IMAGE_DRAWABLES[adapterPosition])
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        viewHolderListener.onLoadCompleted(image, adapterPosition)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        viewHolderListener.onLoadCompleted(image, adapterPosition)
                        return false
                    }
                })
                .into(image)
        }

        override fun onClick(view: View) {
            viewHolderListener.onItemClicked(view, adapterPosition)
        }
    }

    /**
     * Listener interface for ViewHolder events.
     */
    interface ViewHolderListener {
        fun onLoadCompleted(view: ImageView, adapterPosition: Int)
        fun onItemClicked(view: View, adapterPosition: Int)
    }

    /**
     * Default ViewHolderListener implementation.
     */
    private class ViewHolderListenerImpl(
        private val fragment: Fragment
    ) : ViewHolderListener {

        private val enterTransitionStarted = AtomicBoolean()

        override fun onLoadCompleted(view: ImageView, position: Int) {
            if (MainActivity.currentPosition != position) return
            if (enterTransitionStarted.getAndSet(true)) return
            fragment.startPostponedEnterTransition()
        }

        override fun onItemClicked(view: View, position: Int) {
            MainActivity.currentPosition = position

            (fragment.exitTransition as? TransitionSet)?.excludeTarget(view, true)

            val transitioningView = view.findViewById<ImageView>(R.id.card_image)

            fragment.parentFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .addSharedElement(transitioningView, transitioningView.transitionName)
                .replace(
                    R.id.fragment_container,
                    ImagePagerFragment(),
                    ImagePagerFragment::class.java.simpleName
                )
                .addToBackStack(null)
                .commit()
        }
    }
}
