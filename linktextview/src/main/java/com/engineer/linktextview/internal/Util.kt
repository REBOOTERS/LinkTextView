package com.engineer.linktextview.internal

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 *
 * @author: zhuyongging
 * @since: 2019-06-09
 */
object Util {

    fun mappingLink(content: String, links: List<String>): ArrayList<Pair<Int, Int>> {
        var pattern: Pattern?
        var matcher: Matcher?
        var list: ArrayList<Pair<Int, Int>> = ArrayList()

        for (value in links) {
            if (value == "") {
                continue
            }

            pattern = Pattern.compile(value)
            matcher = pattern.matcher(content)
            while (matcher.find()) {
                val start = matcher.start()
                val end = matcher.end()
                val pair = Pair(start, end)
                list.add(pair)
            }
        }
        return list
    }


    fun mappingStrLink(content: String, link: String): Pair<Int, Int> {
        var pattern: Pattern?
        var matcher: Matcher?
        var pair: Pair<Int, Int> = Pair(0, 0)



        pattern = Pattern.compile(link)
        matcher = pattern.matcher(content)
        while (matcher.find()) {
            val start = matcher.start()
            val end = matcher.end()
            pair = Pair(start, end)
        }

        return pair
    }
}