package com.engineer.linktextview

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.MotionEvent
import android.widget.TextView
import java.util.regex.Matcher
import java.util.regex.Pattern

object HighLight {

    fun highLight(block: Builder.() -> Unit) = Builder().apply(block).build()

    class Builder {

        var mLinkMovementMethod: LinkMovementMethod? = null
        var mTextView: TextView? = null
        var mContent: String = ""
        var mLinks: List<String> = ArrayList()
        var mColor: Int = Color.RED


        fun build() {
            applyLink(
                mTextView, mContent, mLinks, mColor, mLinkMovementMethod
            )
        }
    }

    private fun applyLink(
        mTextView: TextView?,
        content: String,
        links: List<String>?,
        color: Int,
        mLinkMovementMethod: LinkMovementMethod?
    ) {
        if (mTextView == null) {
            throw IllegalStateException("the TextView must not null")
        }

        if ((links == null || links.isEmpty())) {
            mTextView.text = content
            return
        }


        applyLinkInternal(
            mTextView, content, links, color, mLinkMovementMethod
        )
    }

    private fun applyLinkInternal(
        mTextView: TextView,
        content: String,
        links: List<String>,
        color: Int,
        mLinkMovementMethod: LinkMovementMethod?
    ) {
        val spannableString = SpannableString(content)

        var pattern: Pattern?
        var matcher: Matcher?
        var clickableSpan: ForegroundColorSpan?

        for (value in links) {
            if (TextUtils.isEmpty(value)) {
                continue
            }

            pattern = Pattern.compile(value)
            matcher = pattern.matcher(content)
            while (matcher.find()) {
                clickableSpan = ForegroundColorSpan(color)
                spannableString.setSpan(
                    clickableSpan, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        mTextView.text = spannableString

        if (mLinkMovementMethod != null) {
            mTextView.movementMethod = mLinkMovementMethod
        } else {
            mTextView.movementMethod = TextViewLinkMovementMethod().getInstance()
        }
    }

    class TextViewLinkMovementMethod : LinkMovementMethod() {

        override fun onTouchEvent(
            widget: TextView?, buffer: Spannable?, event: MotionEvent?
        ): Boolean {
            val action = event!!.action
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
                var x = event.x.toInt()
                var y = event.y.toInt()

                x -= widget!!.totalPaddingLeft
                y -= widget.totalPaddingTop

                x += widget.scrollX
                y += widget.scrollY

                val layout = widget.layout
                val line = layout.getLineForVertical(y)
                val off = layout.getOffsetForHorizontal(line, x.toFloat())

                // 命中字符起始X坐标
                val charStartX = layout.getPrimaryHorizontal(off).toInt()

                // 单个字符宽度
                var singleCharWidth = 0
                if (widget.text.isNotEmpty()) {
                    singleCharWidth = widget.paint.measureText(widget.text[0].toString()).toInt()
                }

                if (x <= charStartX + singleCharWidth) {// 命中字符范围内，响应点击
                    val links = buffer!!.getSpans(off, off, ClickableSpan::class.java)

                    if (links.isNotEmpty()) {
                        if (action == MotionEvent.ACTION_UP) {
                            links[0].onClick(widget)
                        }
                        return true
                    }
                } else {// 没有命中，消耗事件不处理
                    return true
                }
            }
            return super.onTouchEvent(widget, buffer, event)
        }

        fun getInstance(): TextViewLinkMovementMethod {
            if (sInstance == null) {
                sInstance = TextViewLinkMovementMethod()
            }
            return sInstance!!
        }

        private var sInstance: TextViewLinkMovementMethod? = null
    }
}