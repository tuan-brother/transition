package com.ta.demo.fragment

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ta.demo.MainActivity
import com.ta.demo.R
import com.ta.demo.adapter.GridAdapter

class GridFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recyclerView = inflater.inflate(R.layout.fragment_grid, container, false) as RecyclerView
        recyclerView.adapter = GridAdapter(this)

        prepareTransitions()
        postponeEnterTransition()

        return recyclerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scrollToPosition()
    }

    /**
     * Scrolls the recycler view to show the last viewed item in the grid.
     */
    private fun scrollToPosition() {
        recyclerView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                recyclerView.removeOnLayoutChangeListener(this)
                val layoutManager = recyclerView.layoutManager ?: return
                val viewAtPosition = layoutManager.findViewByPosition(MainActivity.currentPosition)
                if (viewAtPosition == null || layoutManager.isViewPartiallyVisible(viewAtPosition, false, true)) {
                    recyclerView.post {
                        layoutManager.scrollToPosition(MainActivity.currentPosition)
                    }
                }
            }
        })
    }

    /**
     * Prepares the shared element transition to the pager fragment.
     */
    private fun prepareTransitions() {
        exitTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.grid_exit_transition)

        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: List<String>,
                sharedElements: MutableMap<String, View>
            ) {
                val selectedViewHolder = recyclerView
                    .findViewHolderForAdapterPosition(MainActivity.currentPosition) ?: return
                sharedElements[names[0]] =
                    selectedViewHolder.itemView.findViewById(R.id.card_image)
            }
        })
    }
}
