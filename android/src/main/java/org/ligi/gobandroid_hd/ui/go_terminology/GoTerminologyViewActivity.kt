package org.ligi.gobandroid_hd.ui.go_terminology

import android.app.Activity
import android.os.Bundle
import android.text.util.Linkify
import android.widget.TextView
import org.ligi.axt.listeners.ActivityFinishingOnClickListener
import org.ligi.gobandroid_hd.R
import java.util.*
import java.util.regex.Pattern

class GoTerminologyViewActivity : Activity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.empty)
        // NaDra setBehindContentView(R.layout.empty);

        val term = this.intent.data.toString().substringAfterLast("/")

        val dialog = GoTerminologyDialog(this, term)
        dialog.setPositiveButton(android.R.string.ok, ActivityFinishingOnClickListener(this))
        dialog.setOnCancelListener { finish() }
        dialog.show()

    }

    companion object {

        val Term2resMap: HashMap<String, Int> = object : HashMap<String, Int>() {
            init {
                put("joseki", R.string.goterm_joseki)
                put("miai", R.string.goterm_miai)
                put("shape", R.string.goterm_shape)
                put("tesuji", R.string.goterm_tesuji)
                // TODO add missing mojo
            }
        }

        fun linkifyTextView(myTextView: TextView) {

            Linkify.addLinks(myTextView, Linkify.ALL);

            val mentionFilter: Linkify.TransformFilter = Linkify.TransformFilter { matcher, url ->
                matcher.group(1).toLowerCase()
            }

            Term2resMap.keys.forEach {
                val wikiWordMatcher = Pattern.compile("[\\. ]($it)[\\. ]", Pattern.CASE_INSENSITIVE)
                val wikiViewURL = "goterm://org.ligi.gobandroid_hd.goterms/";
                Linkify.addLinks(myTextView, wikiWordMatcher, wikiViewURL, null, mentionFilter);
            }

        }
    }
}
