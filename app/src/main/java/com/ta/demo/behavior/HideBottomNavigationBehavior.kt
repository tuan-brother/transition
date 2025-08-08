package com.ta.demo.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class HideBottomNavigationBehavior(
    context: Context,
    attrs: AttributeSet
) : CoordinatorLayout.Behavior<BottomNavigationView>(context, attrs) {

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: BottomNavigationView,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: BottomNavigationView,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        if (dy > 0) { // Scroll xuống → ẩn
            child.animate()
                .translationY(child.height.toFloat())
                .setDuration(200)
                .start()
        } else if (dy < 0) { // Scroll lên → hiện
            child.animate()
                .translationY(0f)
                .setDuration(200)
                .start()
        }
    }
}
