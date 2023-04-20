package com.example.lab1

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.File

@WebServlet(name = "helloServlet", value = ["/hello"])
class HelloServlet : HttpServlet() {
    private lateinit var repository: File

    override fun init() {
        repository = File(servletContext.getRealPath("") + File.separator + "database.txt")
    }

    public override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        response.contentType = "text/html;charset=UTF-8"

        val key = request.getParameter("key")
        val value = request.getParameter("value").toIntOrNull() ?: 1
        val test = request.getParameter("test")?.toBoolean() ?: false

        val requestUrl = request.requestURL.toString()
        val httpMethod = request.method
        val clientIp = request.remoteAddr
        val userAgent = request.getHeader("User-Agent")
        val params = request.parameterMap.map { (key, value) -> "$key=${value.joinToString(",")}" }.joinToString("&")

        val logMessage = "HTTP method: $httpMethod, IP address: $clientIp, User-Agent: $userAgent, Params: $params"
        servletContext.log(logMessage)

        if (test) {
            response.writer.use { out ->
                out.println("<html><body><h1>Confirmation Message</h1></body></html>")
            }
        } else {
            repository.appendText("$key\t${"x".repeat(value)}\n")

            response.writer.use { out ->
                out.println("<html><body><ul>")
                repository.forEachLine { line ->
                    out.println("<li>$line</li>")
                }
                out.println("</ul></body></html>")
            }
        }

    }

    override fun destroy() {
        repository.delete()
    }
}