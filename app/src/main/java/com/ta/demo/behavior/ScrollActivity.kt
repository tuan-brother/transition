package com.ta.demo.behavior

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.SimpleAdapter
import android.widget.Space
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ta.demo.R
import com.ta.demo.databinding.ActivityScrollBinding

class ScrollActivity : AppCompatActivity() {
    private lateinit var customNav: LinearLayout
    private var isNavVisible = true
    private var isExpanded = true
    private var currentTab = 0 // 0 = Home, 1 = Profile
    lateinit var binding: ActivityScrollBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScrollBinding.inflate(layoutInflater)
        setContentView(binding.root)
        customNav = findViewById(R.id.customNav)

        binding.btnHome.setOnClickListener {
            currentTab = 0
            openFragment(HomeFragment())
        }
        binding.btnProfile.setOnClickListener {
            currentTab = 1
            openFragment(ProfileFragment())
        }

        // Mặc định mở Home
        openFragment(HomeFragment())
    }

    fun showNav() {
        if (!isNavVisible) {
            customNav.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(250)
                .setInterpolator(DecelerateInterpolator())
                .start()
            isNavVisible = true
        }
    }

    fun hideNav() {
        if (isNavVisible) {
            customNav.animate()
                .translationY(customNav.height.toFloat() + 30)
                .alpha(0f)
                .setDuration(250)
                .setInterpolator(AccelerateInterpolator())
                .start()
            isNavVisible = false
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    fun expandNav() {
        if (!isExpanded) {
            animateNavWidth(customNav.width, dpToPx(200))

            val showBtn = if (currentTab == 0) binding.btnProfile else binding.btnHome
            showBtn.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(250)
                .start()

            isExpanded = true
        }
    }

    fun shrinkNav() {
        if (isExpanded) {
            animateNavWidth(customNav.width, dpToPx(100))

            val hideBtn = if (currentTab == 0) binding.btnProfile else binding.btnHome
            hideBtn.animate()
                .alpha(0f)
                .scaleX(0.5f) // Thu nhỏ icon còn 50%
                .scaleY(0.5f)
                .setDuration(250)
                .start()

            isExpanded = false
        }
    }


    private fun animateNavWidth(from: Int, to: Int) {
        val animator = ValueAnimator.ofInt(from, to)
        animator.duration = 250
        animator.addUpdateListener {
            val params = customNav.layoutParams
            params.width = it.animatedValue as Int
            customNav.layoutParams = params
        }
        animator.start()
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}
