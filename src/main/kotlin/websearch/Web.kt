package websearch

import org.jsoup.Jsoup.connect
import org.jsoup.nodes.Document

class URL(private val url: String) {
  fun download(): WebPage = WebPage(connect(url).get())

  override fun toString(): String = url

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null) return false
    if (other !is URL) return false
    return url == other.url
  }

  override fun hashCode(): Int {
    return url.hashCode()
  }
}

class WebPage(private val page: Document) {
  fun extractWords(): List<String> = page.text().lowercase().split("[\\s,.]+".toRegex()).filter { it.isNotEmpty() }

  fun extractLinks(): List<URL> {
    val tags = page.getElementsByTag("a")
    return tags.mapNotNull { tag -> tag?.attr("href") }.map { link -> URL(link) }
  }
}
