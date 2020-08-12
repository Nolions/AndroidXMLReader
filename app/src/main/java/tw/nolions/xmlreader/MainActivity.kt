package tw.nolions.xmlreader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader


class MainActivity : AppCompatActivity() {
    private val ns: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Repository.baseAPI("https://alerts.ncdr.nat.gov.tw")
        CoroutineScope(Dispatchers.IO).launch {
            val result = Repository.suspensionClasses()
            withContext(Dispatchers.Main) {
                result.data?.let { parserRSSFeedEntry(it) }
            }
        }
    }

    private fun parserRSSFeedEntry(result: String): List<Entry> {
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true
        val xpp = factory.newPullParser()
        xpp.setInput(StringReader(result))
        xpp.nextTag()

        val entries = RSSFeedXMLReader().readFeed(xpp)
        println("entries: $entries")
        return entries
    }
}
