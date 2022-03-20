package com.test.application.ui.fragments.currency

import android.graphics.Color
import android.graphics.Typeface
import android.text.*
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.*
import com.test.application.R
import com.test.application.data.ui.models.CurrencyUI
import com.test.application.utils.toPx


import java.util.*


class CurrencyAdapter()  : RecyclerView.Adapter<CurrencyAdapter.CurrencyHolder>() {

    private val itemList: MutableList<CurrencyUI>
    var onCurrencyListener: OnCurrencyListener? = null

    init {
        this.itemList = ArrayList()
    }

    fun update(newList: List<CurrencyUI>) {
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyHolder {
        val view =  FrameLayout(parent.context).apply {
            this.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 56.toPx())
        }

        return CurrencyHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {
        val item = itemList[position]
        holder.fillItem(item,  onCurrencyListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    interface OnCurrencyListener {
        fun onHandleFavorite(favorite : Boolean,  currencyUI: CurrencyUI)
    }

    class CurrencyHolder(val frame: FrameLayout) : RecyclerView.ViewHolder(frame) {

        val imgFavorite: ImageView
        val txtCurrencyName: TextView
        val txtRate: TextView




        init {
            val textContext = LinearLayout(frame.context).apply {
                this.layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    this.marginStart = 16.toPx()
                    this.marginEnd = 56.toPx()
                    this.gravity = Gravity.CENTER_VERTICAL
                }

                this.orientation = LinearLayout.VERTICAL

                frame.addView(this)
            }

            txtCurrencyName = TextView(frame.context).apply {
                this.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                this.maxLines = 1
                this.ellipsize = TextUtils.TruncateAt.END
                this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                this.setTextColor(Color.BLACK)
                this.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            }

            textContext.addView(txtCurrencyName)
            txtRate = TextView(frame.context).apply {
                this.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    this.topMargin = 4.toPx()
                }

                this.maxLines = 1
                this.ellipsize = TextUtils.TruncateAt.END

                this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                this.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            }

            textContext.addView(txtRate)

            imgFavorite = ImageView(frame.context).apply {
                this.layoutParams = FrameLayout.LayoutParams(40.toPx(), 40.toPx()).apply {
                    this.gravity = Gravity.END or Gravity.CENTER_VERTICAL
                    this.marginEnd = 8.toPx()
                }

                this.setPadding(8.toPx(), 8.toPx(), 8.toPx(), 8.toPx())
                val outValue = TypedValue()
                context.theme.resolveAttribute(
                    android.R.attr.selectableItemBackgroundBorderless,
                    outValue,
                    true
                )

                this.setBackgroundResource(outValue.resourceId)
                frame.addView(this)
            }
        }

        fun fillItem(item: CurrencyUI, onCurrencyListener: OnCurrencyListener?) {
            txtCurrencyName.text = item.secondCurrency
            txtRate.text = item.rate.toString()
            val favoriteRes = if (item.isFavorite) {
                R.drawable.ic_star
            } else {
                R.drawable.ic_star_outline
            }

            imgFavorite.setImageResource(favoriteRes)
            imgFavorite.setOnClickListener {
                onCurrencyListener?.onHandleFavorite(item.isFavorite.not(), item)
            }
        }
    }

}
