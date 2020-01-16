import interfaces.ILogger
import interfaces.WebService
import java.lang.Exception

class StringCalcWithLogger(private val logger: ILogger, private val webService: WebService) {
    fun add(numbers: String): Int {
        try {
            if (numbers.isEmpty()){
                //TODO 1: write the result to a logger here
                logger.write("got 0")
                return 0
            }

            val result = numbers.split(',').sumBy { Integer.parseInt(it) }
            //TODO 2: write the result to a logger here
            logger.write("got ${result}")
            return result
        } catch (err: Exception) {
            try {
                err.message?.let { webService.notifyError(it) }
                return -1
            } catch (err: Exception) {
                throw Exception("Webservice configuration issue")
            }
        }
    }
}
