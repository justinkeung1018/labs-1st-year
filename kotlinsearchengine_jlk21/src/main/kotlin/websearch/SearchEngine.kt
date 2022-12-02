package websearch

class SearchEngine(private val pages: Map<URL, WebPage>) {
  private var index: Map<String, List<SearchResult>> = emptyMap()

  fun compileIndex() {
    val pairs = mutableListOf<Pair<String, URL>>()
    for (page in pages.entries) {
      val url = page.key
      val webpage = page.value
      val words = webpage.extractWords()
      for (word in words) {
        pairs.add(Pair(word, url))
      }
    }
    val map = pairs.groupBy(keySelector = { it.first }, valueTransform = { it.second })
    index = map.mapValues { entry -> rank(entry.value).sortedByDescending { it.numRefs } }
  }

  private fun rank(urls: List<URL>): List<SearchResult> {
    val searchResults = mutableMapOf<URL, Int>()
    for (url in urls) {
      searchResults[url] = searchResults.getOrDefault(url, 0) + 1
    }
    return searchResults.map { SearchResult(it.key, it.value) }
  }

  fun searchFor(query: String): SearchResultsSummary {
    val trimmed = query.trim { it.isWhitespace() }
    val results = index.getOrDefault(trimmed.lowercase(), emptyList())
    return SearchResultsSummary(trimmed, results)
  }
}

class SearchResult(val url: URL, val numRefs: Int) {
  override fun toString(): String = "$url - $numRefs references"
}

class SearchResultsSummary(val query: String, val results: List<SearchResult>) {
  override fun toString(): String {
    if (results.isEmpty()) {
      return "No results for \"$query\""
    }
    val summary = mutableListOf("Results for \"$query\":")
    for (result in results) {
      summary.add("  $result")
    }
    return summary.joinToString("\n")
  }
}
