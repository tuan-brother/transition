package com.ta.demo.other

import android.os.Bundle
import android.transition.Transition
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.ta.demo.R

class DetailHomeActivity : AppCompatActivity() {
    companion object {
        // Extra name for the ID parameter
        const val EXTRA_PARAM_ID = "detail:_id"

        // View name of the header image. Used for activity scene transitions
        const val VIEW_NAME_HEADER_IMAGE = "detail:header:image"

        // View name of the header title. Used for activity scene transitions
        const val VIEW_NAME_HEADER_TITLE = "detail:header:title"
    }

    private lateinit var headerImageView: ImageView
    private lateinit var headerTitle: TextView
    private lateinit var item: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_home)

        // Retrieve the correct Item instance using the ID provided in the Intent
        val itemId = intent.getIntExtra(EXTRA_PARAM_ID, 0)
        item = Item.getItem(itemId)

        headerImageView = findViewById(R.id.imageview_header)
        headerTitle = findViewById(R.id.textview_title)

        // Set transition names for shared element transition
        ViewCompat.setTransitionName(headerImageView, VIEW_NAME_HEADER_IMAGE)
        ViewCompat.setTransitionName(headerTitle, VIEW_NAME_HEADER_TITLE)

        loadItem()
    }

    private fun loadItem() {
        // Set the title TextView to the item's name and author
        headerTitle.text = getString(R.string.image_header, item.name, item.author)

        if (addTransitionListener()) {
            // Load the thumbnail first, then full image after transition ends
            loadThumbnail()
        } else {
            // Immediately load full-size image
            loadFullSizeImage()
        }
    }

    /**
     * Load the item's thumbnail image into the header image view.
     */
    private fun loadThumbnail() {
        Glide.with(headerImageView.context)
            .load(item.thumbnailUrl)
            .into(headerImageView)
    }

    /**
     * Load the item's full-size image into the header image view.
     */
    private fun loadFullSizeImage() {
        Glide.with(headerImageView.context)
            .load(item.photoUrl)
            .into(headerImageView)
    }

    /**
     * Add a transition listener to load full-size image after shared element transition ends.
     */
    private fun addTransitionListener(): Boolean {
        val transition = window.sharedElementEnterTransition ?: return false

        transition.addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(transition: Transition) {
                loadFullSizeImage()
                transition.removeListener(this)
            }

            override fun onTransitionStart(transition: Transition) {}

            override fun onTransitionCancel(transition: Transition) {
                transition.removeListener(this)
            }

            override fun onTransitionPause(transition: Transition) {}

            override fun onTransitionResume(transition: Transition) {}
        })

        return true
    }
}