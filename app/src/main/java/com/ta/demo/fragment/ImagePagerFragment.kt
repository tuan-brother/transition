package com.ta.demo.fragment

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.ta.demo.MainActivity
import com.ta.demo.R
import com.ta.demo.adapter.ImagePagerAdapter

class ImagePagerFragment : Fragment() {

    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewPager = inflater.inflate(R.layout.fragment_pager, container, false) as ViewPager
        viewPager.adapter = ImagePagerAdapter(childFragmentManager)

        // Set the current position and listener to update MainActivity.currentPosition
        viewPager.currentItem = MainActivity.currentPosition
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                MainActivity.currentPosition = position
            }
        })

        prepareSharedElementTransition()

        // Only postpone enter transition on first creation
        if (savedInstanceState == null) {
            postponeEnterTransition()
        }

        return viewPager
    }

    /**
     * Prepares the shared element transition from and back to the grid fragment.
     */
    private fun prepareSharedElementTransition() {
        val transition = TransitionInflater.from(context)
            .inflateTransition(R.transition.image_shared_element_transition)
        sharedElementEnterTransition = transition

        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>?,
                sharedElements: MutableMap<String, View>?
            ) {
                val adapter = viewPager.adapter ?: return
                val currentFragment = adapter.instantiateItem(
                    viewPager,
                    MainActivity.currentPosition
                ) as? Fragment ?: return

                val view = currentFragment.view ?: return

                sharedElements?.set(names?.get(0) ?: "", view.findViewById(R.id.image))
            }
        })
    }
}
