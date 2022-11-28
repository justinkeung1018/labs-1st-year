package websearch

import org.jsoup.Jsoup
import org.junit.Test
import kotlin.test.assertEquals

class SearchEngineTest {

  private val docHomePageHtml =
    """<html>
         <head>
           <title>Department of Computing</title>
         </head>
           <body>
             <p>Welcome to the Department of Computing at <a href="https://www.imperial.ac.uk">Imperial College London</a>.</p>
           </body>
        </html>"""

  private val imperialHomePageHtml =
    """<html>
         <head>
           <title>Imperial College London</title>
         </head>
           <body>
             <p>Imperial people share ideas, expertise and technology to find answers to the big scientific questions and tackle global challenges</p>
             <p>See the latest news about our research on the <a href="https://www.bbc.co.uk/news">BBC</a></p>
           </body>
        </html>"""

  private val bbcNewsPageHtml =
    """<html>
         <head>
           <title>BBC News</title>
         </head>
           <body>
             <p>Here is all the latest news about science and other things.</p>
           </body>
        </html>"""

  private val docHomePage = WebPage(Jsoup.parse(docHomePageHtml))
  private val imperialHomePage = WebPage(Jsoup.parse(imperialHomePageHtml))
  private val bbcNews = WebPage(Jsoup.parse(bbcNewsPageHtml))

  private val downloadedPages = mapOf(
    URL("https://www.imperial.ac.uk/computing") to docHomePage,
    URL("https://www.imperial.ac.uk") to imperialHomePage,
    URL("https://www.bbc.co.uk/news") to bbcNews
  )

  @Test
  fun `can index downloaded pages`() {
    val searchEngine = SearchEngine(downloadedPages)
    searchEngine.compileIndex()
    var summary = searchEngine.searchFor("news")
    assertEquals("news", summary.query)
    assertEquals(2, summary.results.size)
    assertResultsMatch("https://www.bbc.co.uk/news", 2, summary.results[0])
    assertResultsMatch("https://www.imperial.ac.uk", 1, summary.results[1])

    // Query with some letters capitalised
    summary = searchEngine.searchFor("NeWs")
    assertEquals("NeWs", summary.query)
    assertEquals(2, summary.results.size)
    assertResultsMatch("https://www.bbc.co.uk/news", 2, summary.results[0])
    assertResultsMatch("https://www.imperial.ac.uk", 1, summary.results[1])

    // Query with extra whitespace around it
    summary = searchEngine.searchFor("      computing  ")
    assertEquals("computing", summary.query)
    assertEquals(1, summary.results.size)
    assertResultsMatch("https://www.imperial.ac.uk/computing", 2, summary.results[0])
  }

  @Test
  fun `prints search results`() {
    val searchEngine = SearchEngine(downloadedPages)
    searchEngine.compileIndex()
    var summary = searchEngine.searchFor("news")
    assertEquals(
      """
                Results for "news":
                  https://www.bbc.co.uk/news - 2 references
                  https://www.imperial.ac.uk - 1 references
      """.trimIndent(),
      summary.toString()
    )

    // Search miss
    summary = searchEngine.searchFor("whatever")
    assertEquals(
      """
                No results for "whatever"
      """.trimIndent(),
      summary.toString()
    )
  }

  private fun assertResultsMatch(expectedUrl: String, numRefs: Int, searchResult: SearchResult) {
    assertEquals(expectedUrl, searchResult.url.toString())
    assertEquals(numRefs, searchResult.numRefs)
  }
}
