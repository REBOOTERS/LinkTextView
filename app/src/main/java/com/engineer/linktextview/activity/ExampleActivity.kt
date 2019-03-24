package com.engineer.linktextview.activity

import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.engineer.linktextview.Linker
import com.engineer.linktextview.R
import com.engineer.linktextview.internal.OnLinkClickListener
import kotlinx.android.synthetic.main.activity_example.*

class ExampleActivity : AppCompatActivity() {

    private lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
        mContext = this

        //<editor-fold desc="android">
        // the content should be set for TextView
        val android = getString(R.string.text_android_baike)
        // Array to define all clickable words
        val androidRules = arrayOf("Linux", "操作系统", "移动设备", "智能手机", "平板电脑", "开放手机联盟")
        Linker.Builder()
            .content(android)
            .textView(text_view_1)
            .links(androidRules)
            .linkColor(ContextCompat.getColor(this, R.color.link))
            .addOnLinkClickListener(onLinkClickListener)
            .apply()
        //</editor-fold>

        //<editor-fold desc="star">
        val star = getString(R.string.text_star_baike)
        val starRules = listOf("黎明", "张学友", "刘德华", "郭富城")
        Linker.Builder()
            .content(star)
            .textView(text_view_2)
            .links(starRules)
            .linkColor(ContextCompat.getColor(this, R.color.link))
            .addOnLinkClickListener(onLinkClickListener)
            .apply()
        //</editor-fold>

        //<editor-fold desc="english">
        val english = getString(R.string.text_english)
        val englishRules = listOf("Manchester City", "Asia")
        Linker.Builder()
            .content(english)
            .textView(text_view_3)
            .links(englishRules)
            .linkColor(ContextCompat.getColor(this, R.color.red))
            .shouldShowUnderLine(true)
            .addOnLinkClickListener(onLinkClickListener)
            .apply()
        //</editor-fold>

        //<editor-fold desc="actors">
        val actors = getString(R.string.text_actors)
        val actorsRules = listOf(" 刘仁娜", "李栋旭", "吴政世", "沈亨倬", "吴义植", "张基龙", "黄灿盛", "金希珍")
        Linker.Builder()
            .content(actors)
            .textView(text_view_4)
            .links(actorsRules)
            .linkColor(ContextCompat.getColor(this, R.color.link))
            .shouldShowUnderLine(false)
            .addOnLinkClickListener(onLinkClickListener)
            .apply()
        //</editor-fold>


        //<editor-fold desc="actors with different colors">


        val actorsRules1 = listOf(
            Pair("刘仁娜", ContextCompat.getColor(this, R.color.red)),
            Pair("李栋旭", ContextCompat.getColor(this, R.color.colorPrimary)),
            Pair("吴政世", ContextCompat.getColor(this, R.color.colorAccent)),
            Pair("沈亨倬", ContextCompat.getColor(this, R.color.colorPrimaryDark)),
            Pair("吴义植", ContextCompat.getColor(this, R.color.green)),
            Pair("张基龙", ContextCompat.getColor(this, R.color.blue)),
            Pair("黄灿盛", ContextCompat.getColor(this, R.color.yellow)),
            Pair("金希珍", ContextCompat.getColor(this, R.color.orange))
        )

        Linker.Builder()
            .content(actors)
            .textView(text_view_4_1)
            .colorLinks(actorsRules1)
            .shouldShowUnderLine(false)
            .addOnLinkClickListener(onLinkClickListener)
            .apply()
        //</editor-fold>

        //<editor-fold desc="email">
        val email = getString(R.string.text_email)
        val emailRules = "movie@douban.com"
        Linker.Builder()
            .content(email)
            .textView(text_view_5)
            .links(emailRules)
            .linkColor(ContextCompat.getColor(this, R.color.gray))
            .addOnLinkClickListener(onLinkClickListener)
            .apply()


        Linker.Builder()
            .content(email)
            .textView(text_view_6)
            .links(emailRules)
            .setLinkMovementMethod(CustomLinkMovementMethod().getInstance())
            .linkColor(ContextCompat.getColor(this, R.color.gray))
            .addOnLinkClickListener(onLinkClickListener)
            .apply()
        //</editor-fold>
    }


    private val onLinkClickListener = object : OnLinkClickListener {
        override fun onClick(view: View, content: String) {
            Toast.makeText(mContext, "clicked link is : $content", Toast.LENGTH_SHORT).show()
        }
    }

}
