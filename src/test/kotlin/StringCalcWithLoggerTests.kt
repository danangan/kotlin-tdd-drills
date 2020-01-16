import interfaces.ILogger
import interfaces.WebService
import io.kotlintest.data.forall
import io.kotlintest.tables.row
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.Exception

class StringCalcWithLoggerTests {
    private fun makeCalc(logger: ILogger = FakeLogger(), webService: WebService = FakeWebService("Webservice configuration issue")): StringCalcWithLogger {
        return StringCalcWithLogger(logger, webService)
    }

    @Test
    fun `Add empty string should log default value`() {
        val logger = FakeLogger()
        val calc = makeCalc(logger)

        calc.add("")

        assertEquals("got 0", logger.written);
    }


    @Test
    fun `Add number should log its own value`() {
        forall(
            row("1", "got 1"),
            row("2", "got 2")
        ) { input, expected ->
            val logger = FakeLogger()
            val calc = makeCalc(logger)

            calc.add(input)

            assertEquals(expected, logger.written)
        }
    }

    @Test
    fun `Add several numbers should log their sum`() {
        forall(
            row("1,1", "got 2"),
            row("1,1,2", "got 4")
        ) { input, expected ->
            val logger = FakeLogger()

            val calc = makeCalc(logger)

            calc.add(input)

            assertEquals(expected, logger.written)
        }
    }

    @Test
    fun `Add notify the web service on exception`() {
        val fakeLogger = FakeLogger("dude, its wrong")
        val fakeWebService = FakeWebService("Webservice configuration issue")

        val calc = makeCalc(fakeLogger, fakeWebService)

        calc.add("1,2")

        assertEquals("dude, its wrong", fakeWebService.notified)
    }

    @Test
    fun `Add throw exception when logger and web service throw exception`() {
        val fakeLogger = FakeLogger("dude, it's wrong")
        val fakeWebService = FakeWebService("Webservice configuration issue")

        val calc = makeCalc(fakeLogger, fakeWebService)



    }
}

class FakeWebService(s: String) : WebService {
    lateinit var notified: String

    override fun notifyError(text: String) {
        notified = text
    }
}


class FakeLogger(val exception: String? = null) : ILogger {
    lateinit var written: String

    override fun write(str: String) {
        if(exception != null){
            throw Exception(exception);
        }

        written = str
    }
}
