package com.ta.demo.other

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.ta.demo.R

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val grid: GridView = findViewById(R.id.grid)
        val adapter = GridAdapter()
        grid.adapter = adapter
        grid.onItemClickListener = onItemClickListener
    }

    /**
     * Called when an item in the GridView is clicked. Launches the DetailActivity with
     * a shared element scene transition.
     */
    private val onItemClickListener =
        AdapterView.OnItemClickListener { parent, view, position, id ->
            val item = parent.getItemAtPosition(position) as Item

            // Create the intent for DetailActivity
            val intent = Intent(this, DetailHomeActivity::class.java)
            intent.putExtra(DetailHomeActivity.EXTRA_PARAM_ID, item.id)

            // Create transition animation
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                androidx.core.util.Pair(
                    view.findViewById(R.id.imageview_item),
                    DetailHomeActivity.VIEW_NAME_HEADER_IMAGE
                ),
                androidx.core.util.Pair(
                    view.findViewById(R.id.textview_name),
                    DetailHomeActivity.VIEW_NAME_HEADER_TITLE
                )
            )

            // Start the activity with the transition
            ActivityCompat.startActivity(this, intent, options.toBundle())
        }

    /**
     * Adapter that provides the data and views for the GridView.
     */
    private inner class GridAdapter : BaseAdapter() {

        override fun getCount(): Int = Item.ITEMS.size

        override fun getItem(position: Int): Item = Item.ITEMS[position]

        override fun getItemId(position: Int): Long = getItem(position).id.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: layoutInflater.inflate(R.layout.grid_item, parent, false)

            val item = getItem(position)

            // Load the thumbnail image
            val image: ImageView = view.findViewById(R.id.imageview_item)
            Glide.with(image.context).load(item.thumbnailUrl).into(image)

            // Set the name
            val name: TextView = view.findViewById(R.id.textview_name)
            name.text = item.name

            return view
        }
    }
}