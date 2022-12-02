package websearch

class WebCrawler(private val url: URL, private val maxPages: Int = 10) {
  private val data: MutableMap<URL, WebPage> = mutableMapOf()
  private val errors: MutableSet<URL> = mutableSetOf()

  fun run() {
    data.clear()
    errors.clear()
    crawl(url)
  }

  private fun crawl(url: URL) {
    if (data.size >= maxPages) {
      return
    }
    if (errors.contains(url)) {
      return
    }
    if (data.containsKey(url)) {
      return
    }
    try {
      val webPage = url.download()
      data[url] = webPage
      for (link in webPage.extractLinks()) {
        crawl(link)
      }
    } catch (e: Exception) {
      errors.add(url)
    }
  }

  fun dump(): Map<URL, WebPage> = data
}

fun main(args: Array<String>) {
  if (args.isNotEmpty()) {
    for (arg in args) {
      val webCrawler = WebCrawler(URL(arg))
      webCrawler.run()
      println("Results of crawling from $arg:")
      for (url in webCrawler.dump().keys) {
        println("  $url")
      }
    }
  }
}
