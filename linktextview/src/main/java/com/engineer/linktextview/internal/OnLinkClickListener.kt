package com.engineer.linktextview.internal

import android.view.View

/**
 * Created on 2019/3/5.
 * @author rookie
 */
interface OnLinkClickListener {
    /**
     * @param view    the view been clicked ,aka the TextView
     * @param content the content which is clicked
     */
    fun onClick(view: View, content: String)
}