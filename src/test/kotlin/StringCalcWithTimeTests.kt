import extractAndOverride.StringCalcWithTime
import io.kotlintest.data.forall
import io.kotlintest.tables.row
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.util.Calendar.*

class StringCalcWithTimeTests {
    @Test
    fun `Add function returns null on weekend`() {
        forall(row(SUNDAY),
                row(SATURDAY)) {
            input ->
            val calc = TestableStringCalcWithTime()

            calc.setDay(input)

            val result = calc.add("1,2,3")

            assertNull(result)
        }
    }

    @Test
    fun `Add function returns default value on weekday with empty string`() {
        forall(row(MONDAY),
                row(THURSDAY)) { input ->
            val calc = TestableStringCalcWithTime()

            calc.setDay(input)

            val result = calc.add("")

            assertEquals(0, result)
        }
    }

    @Test
    fun `Add function returns sum of a single number as that number`() {
        forall(row(MONDAY,"1", 1),
            row(THURSDAY,"2", 2)) { day, input, expected ->
            val calc = TestableStringCalcWithTime()

            calc.setDay(day)

            val result = calc.add(input)

            assertEquals(expected, result)
        }
    }
}

class TestableStringCalcWithTime : StringCalcWithTime() {
    private var currentDay: Int = 0

    override fun getDayOfWeek(): Int {
        return currentDay
    }

    fun setDay(day: Int) {
        currentDay = day
    }
}
