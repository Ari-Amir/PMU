import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.aco.pmu.MainActivity
import com.aco.pmu.scenarios.AddNewClientScenario
import com.aco.pmu.scenarios.AddNewPigmentScenario
import com.aco.pmu.scenarios.AddNewProcedureScenario
import com.aco.pmu.scenarios.AddNewRecordScenario
import com.kaspersky.kaspresso.testcases.api.testcase.DocLocScreenshotTestCase
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File


@RunWith(AndroidJUnit4::class)
class PMUTest : DocLocScreenshotTestCase(
    screenshotsDirectory = File("screenshots"),
    locales = "ru"
) {

    @JvmField
    @Rule
    val testRule = ActivityTestRule<MainActivity>(MainActivity::class.java)


    @Test
    fun addNewRecordTest() {
        run {
            step("1") {
                scenario(AddNewClientScenario())
            }
            step("2") {
                scenario(AddNewProcedureScenario())
            }
            step("3") {
                scenario(AddNewPigmentScenario())
            }
            step("4") {
                scenario(AddNewRecordScenario())
            }
        }
    }


    @After
    fun deleteDatabase() {
        InstrumentationRegistry.getInstrumentation().targetContext.deleteDatabase("PMUs_database")
    }
}





//        var newCLients = database?.dataBaseDao()?.getAllClients()
//        val extraClients = newCLients?.filter { prevClients.any { prev -> it.id != prev.id } }
//        extraClients?.forEach { database?.dataBaseDao()?.delete(it) }

