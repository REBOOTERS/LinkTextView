package com.engineer.linktextview

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast

/**
 * @author: zhuyongging
 * @since: 2019-03-02
 */
object Linker {


    class Builder {


        private lateinit var mTextView: TextView
        private lateinit var Content: String
        private lateinit var links: List<String>
        private var color: Int = Color.BLACK
        private var shouldShowUnderLine: Boolean = false
        private lateinit var mLinkClickListener: OnLinkClickListener

        fun textView(textView: TextView): Builder {
            this.mTextView = textView
            return this
        }

        fun content(content: String): Builder {
            this.Content = content
            return this
        }

        fun links(link: String): Builder {
            val links = arrayOf(link)
            return links(links)
        }

        fun links(links: Array<String>): Builder {
            return links(links.asList())
        }

        fun links(links: List<String>): Builder {
            this.links = links
            return this
        }


        fun linkColor(color: Int): Builder {
            this.color = color
            return this
        }

        fun shouldShowUnderLine(shouldShowUnderLine: Boolean): Builder {
            this.shouldShowUnderLine = shouldShowUnderLine
            return this
        }

        fun addOnLinkClickListener(listener: OnLinkClickListener): Builder {
            this.mLinkClickListener = listener
            return this
        }

        fun apply() {
            applylink(mTextView, Content, links, color,
                    shouldShowUnderLine, mLinkClickListener)
        }

    }

    fun applylink(mTextView: TextView?, content: String, links: List<String>?, color: Int,
                  shouldShowUnderLine: Boolean, linkClickListener: OnLinkClickListener) {

        if (mTextView == null) {
            throw IllegalStateException("the TextView must not null")
        }

        if (TextUtils.isEmpty(content)) {
            throw IllegalStateException("the content must not null")
        }

        if (links == null || links.isEmpty()) {
            mTextView.text = content
            return
        }

        applyLinkInternal(mTextView, content, links, color,
                shouldShowUnderLine, linkClickListener)

    }

    private fun applyLinkInternal(mTextView: TextView, content: String, links: List<String>,
                                  color: Int, shouldShowUnderLine: Boolean, linkClickListener: OnLinkClickListener?) {

        val spannableString = SpannableString(content)
        for (value in links) {
            if (TextUtils.isEmpty(value)) {
                continue
            }

            val index = content.indexOf(value)
            if (index < 0) {
                continue
            }

            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    linkClickListener?.onClick(widget, value)
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = color
                    ds.isUnderlineText = shouldShowUnderLine
                }
            }

            spannableString.setSpan(clickableSpan, index, index + value.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        mTextView.text = spannableString
        mTextView.movementMethod = TextViewLinkMovementMethod().getInstance()
    }

    // <editor-fold defaultstate="collapsed" desc="for map links">

    private fun applyLink(textView: TextView, datas: HashMap<String, String>, buffer: String) {
        val spannableString = SpannableString(buffer)

        for ((key, value) in datas) {
            if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
                continue
            }
            val index = buffer.indexOf(key)

            if (index < 0) {
                continue
            }

            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = Color.GRAY
                    ds.isUnderlineText = false
                }

            }
            spannableString.setSpan(clickableSpan, index, index + key.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        textView.text = spannableString
        textView.movementMethod = TextViewLinkMovementMethod().getInstance()
    }

    // </editor-fold>

    interface OnLinkClickListener {
        /**
         * @param view    the view been clicked ,aka the TextView
         * @param content the content which is clicked
         */
        fun onClick(view: View, content: String)
    }
}
