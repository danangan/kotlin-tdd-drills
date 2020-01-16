import interfaces.ILogger
import interfaces.WebService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class StringCalcWithLoggerTestsWithMockk {
    inline fun <reified T:Any> fake(): T {
        return mockk<T>(relaxed = true, relaxUnitFun = true)
    }

    fun makeCalc(logger: ILogger = fake(), service: WebService = fake()) : StringCalcWithLogger {
        return StringCalcWithLogger(logger, service)
    }

    @Test
    fun `Add single number should log this number`() {
        val mockLog = fake<ILogger>()
        val sc = makeCalc(mockLog);

        sc.add("1")

        verify { mockLog.write("got 1") }
    }

    @Test
    fun `Add empty string should log default value`() {
        val mockLog = fake<ILogger>()
        val sc = makeCalc(mockLog);

        sc.add("")

        verify { mockLog.write("got 0") }
    }

    @Test
    fun `Call add function should notify web service when logger throw exception`() {
        val mockLog = fake<ILogger>()
        val mockWebService = fake<WebService>()

        every { mockLog.write(any()) } .throws(Exception("hell no"))

        val sc = makeCalc(mockLog, mockWebService);

        sc.add("1,2,3")

        verify { mockWebService.notifyError("hell no") }
    }

    @Test
    fun `Call add should throw exception when logger and web service throw exception`() {
        val mockLog = fake<ILogger>()
        val mockWebService = fake<WebService>()

        every { mockLog.write(any()) } .throws(Exception("hell no"))
        every { mockWebService.notifyError(any()) } .throws(Exception())
        val sc = makeCalc(mockLog, mockWebService);

        val result = assertThrows<Exception> {
            sc.add("1,2,3")
        }

        assertEquals("Webservice configuration issue", result.message);
    }
}