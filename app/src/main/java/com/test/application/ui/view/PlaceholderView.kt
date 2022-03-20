package com.test.application.ui.view

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RawRes
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.test.application.utils.toPx


class PlaceholderView : LinearLayout {




    val txtDescr : TextView
    val lottie : LottieAnimationView

    @RawRes
    var lottieRes: Int = 0
        set(value) {
            lottie.setAnimation(value)
            lottie.loop(true)
            lottie.repeatMode = LottieDrawable.RESTART
            lottie.playAnimation()
        }

    var text: String = ""
        set(value) {
            txtDescr.text = value
        }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    init {
        this.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        this.setBackgroundColor(Color.TRANSPARENT)
        this.gravity = Gravity.CENTER
        this.orientation = LinearLayout.VERTICAL

        lottie = LottieAnimationView(context).apply {
            this.layoutParams = LayoutParams(
                100.toPx(),
                100.toPx()
            )
        }

        addView(lottie)
        txtDescr = TextView(context).apply {
            this.layoutParams = LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                this.topMargin = 16.toPx()
                this.marginStart = 50.toPx()
                this.marginEnd = 50.toPx()
            }

            this.gravity = Gravity.CENTER
            this.setTextColor(Color.BLACK)
            this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            this.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        }

        addView(txtDescr)

    }

}