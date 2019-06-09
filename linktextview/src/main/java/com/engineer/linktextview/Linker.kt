package com.engineer.linktextview

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.engineer.linktextview.internal.OnLinkClickListener
import com.engineer.linktextview.internal.TextViewLinkMovementMethod
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author: rookie
 * @since: 2019-03-02
 */
object Linker {


    class Builder {

        private var mLinkMovementMethod: LinkMovementMethod? = null
        private lateinit var mTextView: TextView
        private lateinit var mContent: String
        private var mLinks: List<String> = ArrayList()
        private var mColor: Int = Color.BLACK
        private var mShouldShowUnderLine: Boolean = false
        private lateinit var mLinkClickListener: OnLinkClickListener
        private var mColorLinks: List<Pair<String, Int>> = ArrayList()

        fun textView(textView: TextView): Builder {
            this.mTextView = textView
            return this
        }

        fun content(content: String): Builder {
            this.mContent = content
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
            this.mLinks = links
            return this
        }

        fun colorLinks(links: List<Pair<String, Int>>): Builder {
            this.mColorLinks = links
            return this
        }


        fun linkColor(color: Int): Builder {
            this.mColor = color
            return this
        }

        fun shouldShowUnderLine(shouldShowUnderLine: Boolean): Builder {
            this.mShouldShowUnderLine = shouldShowUnderLine
            return this
        }

        fun addOnLinkClickListener(listener: OnLinkClickListener): Builder {
            this.mLinkClickListener = listener
            return this
        }

        fun setLinkMovementMethod(method: LinkMovementMethod): Builder {
            this.mLinkMovementMethod = method
            return this
        }

        fun apply() {
            applylink(
                mTextView, mContent, mLinks, mColor,
                mShouldShowUnderLine, mLinkClickListener, mLinkMovementMethod, mColorLinks
            )
        }
    }

    fun applylink(
        mTextView: TextView?,
        content: String,
        links: List<String>?,
        color: Int,
        shouldShowUnderLine: Boolean,
        linkClickListener: OnLinkClickListener,
        mLinkMovementMethod: LinkMovementMethod?,
        mColorLinks: List<Pair<String, Int>>?
    ) {
        if (mTextView == null) {
            throw IllegalStateException("the TextView must not null")
        }

        if ((links == null || links.isEmpty()) && (mColorLinks == null || mColorLinks.isEmpty())) {
            mTextView.text = content
            return
        }

        if (mColorLinks != null && mColorLinks.isNotEmpty()) {
            applyLinkInternal(
                mTextView, content, mColorLinks,
                shouldShowUnderLine, linkClickListener, mLinkMovementMethod
            )
            return
        }

        if (links != null && links.isNotEmpty()) {
            applyLinkInternal(
                mTextView, content, links, color,
                shouldShowUnderLine, linkClickListener, mLinkMovementMethod
            )
        }


    }

    private fun mappingLink(content: String, links: List<String>): ArrayList<Pair<Int, Int>> {
        var pattern: Pattern?
        var matcher: Matcher?
        var list: ArrayList<Pair<Int, Int>> = ArrayList()

        for (value in links) {
            if (value == "") {
                continue
            }

            pattern = Pattern.compile(value)
            matcher = pattern.matcher(content)
            val start = matcher.start()
            val end = matcher.end()
            val pair = Pair(start, end)
            list.add(pair)
        }
        return list
    }

    private fun applyLinkInternal(
        mTextView: TextView, content: String, links: List<String>,
        color: Int, shouldShowUnderLine: Boolean,
        linkClickListener: OnLinkClickListener?,
        mLinkMovementMethod: LinkMovementMethod?
    ) {
        val spannableString = SpannableString(content)

        var pattern: Pattern?
        var matcher: Matcher?
        var clickableSpan: ClickableSpan?

        for (value in links) {
            if (TextUtils.isEmpty(value)) {
                continue
            }

            pattern = Pattern.compile(value)
            matcher = pattern.matcher(content)
            while (matcher.find()) {
                clickableSpan = object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        linkClickListener?.onClick(widget, value)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = color
                        ds.isUnderlineText = shouldShowUnderLine
                    }
                }

                spannableString.setSpan(clickableSpan, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        mTextView.text = spannableString

        if (mLinkMovementMethod != null) {
            mTextView.movementMethod = mLinkMovementMethod
        } else {
            mTextView.movementMethod = TextViewLinkMovementMethod().getInstance()
        }
    }

    private fun applyLinkInternal(
        mTextView: TextView, content: String, links: List<Pair<String, Int>>,
        shouldShowUnderLine: Boolean,
        linkClickListener: OnLinkClickListener?,
        mLinkMovementMethod: LinkMovementMethod?
    ) {

        val spannableString = SpannableString(content)

        var pattern: Pattern?
        var matcher: Matcher?
        var clickableSpan: ClickableSpan?


        for (value in links) {
            if (TextUtils.isEmpty(value.first)) {
                continue
            }

            pattern = Pattern.compile(value.first)
            matcher = pattern.matcher(content)
            while (matcher.find()) {
                clickableSpan = object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        linkClickListener?.onClick(widget, value.first)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = value.second
                        ds.isUnderlineText = shouldShowUnderLine
                    }
                }

                spannableString.setSpan(clickableSpan, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }

        mTextView.text = spannableString

        if (mLinkMovementMethod != null) {
            mTextView.movementMethod = mLinkMovementMethod
        } else {
            mTextView.movementMethod = TextViewLinkMovementMethod().getInstance()

        }
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
}
