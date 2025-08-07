package com.ta.demo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ta.demo.fragment.GridFragment

class MainActivity : AppCompatActivity() {

    companion object {
        /**
         * Holds the current image position to be shared between the grid and the pager fragments.
         * This position is updated when a grid item is clicked or when paging the pager.
         *
         * In this demo app, the position always points to an image index from the [ImageData] class.
         */
        var currentPosition: Int = 0
        private const val KEY_CURRENT_POSITION = "com.google.samples.gridtopager.key.currentPosition"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(KEY_CURRENT_POSITION, 0)
            // Return here to prevent adding additional GridFragments when changing orientation.
            return
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, GridFragment(), GridFragment::class.java.simpleName)
            .commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_POSITION, currentPosition)
    }
}
