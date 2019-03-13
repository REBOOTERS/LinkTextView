package com.engineer.linktextview.activity

import android.graphics.Color
import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.widget.TextView

/**
 *
 * @author: zhuyongging
 * @since: 2019-03-13
 */
class CustomLinkMovementMethod : LinkMovementMethod() {
    override fun onTouchEvent(widget: TextView?, buffer: Spannable?, event: MotionEvent?): Boolean {
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
                        widget.setTextColor(Color.BLACK)
                    } else if (action == MotionEvent.ACTION_DOWN) {
                        Selection.setSelection(buffer, buffer.getSpanStart(links[0]), buffer.getSpanEnd(links[0]))
                        widget.setTextColor(Color.RED)
                    }
                    return true
                } else {
                    Selection.removeSelection(buffer)
                }
            } else {// 没有命中，消耗事件不处理
                return true
            }
        }
        return super.onTouchEvent(widget, buffer, event)
    }

    fun getInstance(): CustomLinkMovementMethod {

        if (sInstance == null) {
            sInstance = CustomLinkMovementMethod()
        }

        return sInstance as CustomLinkMovementMethod
    }

    private var sInstance: CustomLinkMovementMethod? = null
}