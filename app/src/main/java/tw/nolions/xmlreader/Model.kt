package tw.nolions.xmlreader

data class Author(var name: String)

data class Base(val feed: Feed?)

data class Category(val term: String?)

data class Entry(
    val id: String?,
    val summary: String?,
    val updated: String?,
    val category: String?
)

data class Feed(
    val entry: List<Entry>,
    val author: Author,
    val link: Link,
    val id: String,
    val title: String?,
    val updated: String?
)

data class Link(val rel: String, val href: String)

data class Summary(val type: String, val content: String)