package tw.nolions.xmlreader

import org.xmlpull.v1.XmlPullParser

/**
 * RSS Feed XML Reader
 *
 */
class RSSFeedXMLReader {
    private val ns: String? = null

    /**
     * 讀取XML Feed
     *
     * @param parser
     * @return List<Entry>
     */
    fun readFeed(parser: XmlPullParser): List<Entry> {
        val entries = mutableListOf<Entry>()

        parser.require(XmlPullParser.START_TAG, ns, "feed")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            // Starts by looking for the entry tag
            if (parser.name == "entry") {
                entries.add(readEntry(parser))
            } else {
                skip(parser)
            }
        }
        return entries
    }

    /**
     * 讀取Entry元素
     *
     * @param parser
     * @return Entry
     */
    private fun readEntry(parser: XmlPullParser): Entry {
        parser.require(XmlPullParser.START_TAG, ns, "entry")
        var summary: String? = null
        var updated: String? = null
        var id: String? = null
        var category: String? = null
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "id" -> id = readElement(parser, "id")
                "summary" -> summary = readElement(parser, "summary")
                "updated" -> updated = readElement(parser, "updated")
                "category" -> category = readElement(parser, "category", "term")
                else -> skip(parser)
            }
        }
        return Entry(id, summary, updated, category)
    }

    /**
     * 讀取元素
     *
     * @param element
     * @param parser
     * @return String
     */
    private fun readElement(
        parser: XmlPullParser,
        element: String,
        attribute: String? = null
    ): String {
        var value: String? = null
        if (attribute == null) {
            parser.require(XmlPullParser.START_TAG, ns, element)
            value = readText(parser)
            parser.require(XmlPullParser.END_TAG, ns, element)
        } else {
            parser.require(XmlPullParser.START_TAG, ns, element)
            value = readAttribute(attribute, parser)
            parser.nextTag()
        }

        return value
    }

    /**
     * 取得元素值
     *
     * @param parser
     * @return String
     */
    private fun readText(parser: XmlPullParser): String {
        var result = ""

        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }


    /**
     * 取得元素屬性質
     *
     * @param attribute
     * @param parser
     * @return String
     */
    private fun readAttribute(attribute: String, parser: XmlPullParser): String {
        val value = parser.getAttributeValue(null, attribute)
        return value
    }


    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}