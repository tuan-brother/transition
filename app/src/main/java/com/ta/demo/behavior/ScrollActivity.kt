package com.ta.demo.behavior

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
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
    private var fullWidth = 0
    private var smallWidth = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScrollBinding.inflate(layoutInflater)
        setContentView(binding.root)
        customNav = findViewById(R.id.customNav)

        // Đo width khi view đã render
        customNav.post {
            fullWidth = customNav.width // width 2 nút
            binding.btnProfile.visibility = View.GONE
            customNav.post {
                smallWidth = customNav.width // width 1 nút
                binding.btnProfile.visibility = View.VISIBLE // reset về trạng thái ban đầu
            }
        }


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
            val showBtn = if (currentTab == 0) binding.btnProfile else binding.btnHome
            showBtn.visibility = View.VISIBLE
            showBtn.alpha = 0f
            showBtn.scaleX = 0.5f
            showBtn.scaleY = 0.5f

            animateNavWidth(smallWidth, fullWidth)

            showBtn.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setInterpolator(OvershootInterpolator())
                .setDuration(300)
                .start()

            isExpanded = true
        }
    }

    fun shrinkNav() {
        if (isExpanded) {
            val hideBtn = if (currentTab == 0) binding.btnProfile else binding.btnHome

            animateNavWidth(fullWidth, smallWidth)

            hideBtn.animate()
                .alpha(0f)
                .scaleX(0.5f)
                .scaleY(0.5f)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setDuration(300)
                .withEndAction { hideBtn.visibility = View.GONE }
                .start()

            isExpanded = false
        }
    }

    private fun animateNavWidth(from: Int, to: Int) {
        val anim = ValueAnimator.ofInt(from, to)
        anim.addUpdateListener {
            val params = customNav.layoutParams
            params.width = it.animatedValue as Int
            customNav.layoutParams = params
        }
        anim.duration = 300
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.start()
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}
