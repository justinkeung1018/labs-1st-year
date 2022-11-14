package webserver

// write your web framework code here:

fun scheme(url: String): String = url.substringBefore("://")

fun host(url: String): String = url.split('/')[2]

fun path(url: String): String {
  val host = host(url)
  return url.substringAfter(host).substringBefore('?')
}

fun queryParams(url: String): List<Pair<String, String>> {
  val queryString = url.substringAfter('?')
  if (queryString == url)
    return emptyList()
  val pairs = queryString.split('&').map {string -> string.split('=')}
  return pairs.map {list -> Pair(list[0], list[1])}
}

// http handlers for a particular website...

typealias HttpHandler = (Request) -> Response

fun configureRoutes(map: List<Pair<String, HttpHandler>>): HttpHandler {
  return fun(request: Request): Response {
    val path = path(request.url)
    for (pair in map) {
      if (pair.first == path) {
        return pair.second(request)
      }
    }
    return Response(Status.NOT_FOUND, "")
  }
}

fun requireToken(token: String, wrapped: HttpHandler): HttpHandler {
  return fun(request: Request): Response {
    if (request.authToken != token) {
      return Response(Status.FORBIDDEN, "")
    }
    return wrapped(request)
  }
}

fun route(request: Request): Response {
  return when (path(request.url)) {
    "/say-hello" -> helloHandler(request)
    "/" -> homePageHandler(request)
    "/computing" -> homePageHandler(request)
    else -> Response(Status.NOT_FOUND, "")
  }
}

fun helloHandler(request: Request): Response {
  val queryParams = queryParams(request.url)
  var name = "World"
  var isShouting = false
  for (pair in queryParams) {
    when (pair.first) {
      "name" -> name = pair.second
      "style" -> isShouting = pair.second == "shouting"
    }
  }
  val body = "Hello, $name!"
  if (isShouting) {
    return Response(Status.OK, body.uppercase())
  }
  return Response(Status.OK, body)
}

fun homePageHandler(request: Request): Response {
  val body = when (path(request.url)) {
    "/" -> "This is Imperial."
    "/computing" -> "This is DoC."
    else -> return Response(Status.NOT_FOUND, "")
  }
  return Response(Status.OK, body)
}

fun restrictedPageHandler(request: Request): Response {
  val body = when (path(request.url)) {
    "/exam-marks" -> "This is very secret."
    else -> return Response(Status.NOT_FOUND, "")
  }
  return Response(Status.OK, body)
}
