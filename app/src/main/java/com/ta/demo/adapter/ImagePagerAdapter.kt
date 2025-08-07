package com.ta.demo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ta.demo.fragment.ImageFragment

class ImagePagerAdapter(fragment: FragmentManager) :
    FragmentStatePagerAdapter(fragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = ImageData.IMAGE_DRAWABLES.size

    override fun getItem(position: Int): Fragment {
        return ImageFragment.newInstance(ImageData.IMAGE_DRAWABLES[position])
    }
}
