package com.engineer.linktextview.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.engineer.linktextview.Linker
import com.engineer.linktextview.internal.OnLinkClickListener
import com.engineer.linktextview.R
import kotlinx.android.synthetic.main.activity_example.*

class ExampleActivity : AppCompatActivity() {

    private lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
        mContext = this


        val datas = HashMap<String, String>()

        datas.put("「电影」", "https://topic/19550429")
        datas.put("「阅读」", "https://topic/19550564")
        datas.put("「自然科学」", "https://topic/19553298")
        datas.put("「移动互联网」", "https://topic/19550547")

        val buffer = StringBuilder()
        for ((key, _) in datas) {
            buffer.append(key)
        }


        val arrays = ArrayList<String>()
        arrays.add("「电影」")
        arrays.add("「阅读」")
        arrays.add("「自然科学」")
        arrays.add("「移动互联网」")

        val test = "111111111111" + buffer.toString() + "23e4234324323232423432432"

        val test1 = "aaaa"+arrays[0]+"222222222"+arrays[2]+"fdjkjkdajfdkl"+arrays[1]+arrays[3]

        Linker.Builder()
            .content(test)
            .textView(mulit_text_view)
            .links(arrays)
            .linkColor(Color.RED)
            .addOnLinkClickListener(object : OnLinkClickListener {
                override fun onClick(view: View, content: String) {
                    Toast.makeText(mContext, "value is $content", Toast.LENGTH_SHORT).show()
                }
            })
            .apply()

        Linker.Builder()
            .content(test1)
            .textView(mulit_text_view_1)
            .links(arrays)
            .linkColor(Color.RED)
            .addOnLinkClickListener(object : OnLinkClickListener {
                override fun onClick(view: View, content: String) {
                    Toast.makeText(mContext, "value is $content", Toast.LENGTH_SHORT).show()
                }
            })
            .apply()
    }

}
