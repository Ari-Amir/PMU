package com.aco.pmu.scenarios

import com.aco.pmu.screens.*
import com.aco.pmu.AddNewPigment.AddNewPigmentActivityScreen
import com.aco.pmu.AddNewPigment.PigmentsFragmentScreen
import com.kaspersky.kaspresso.testcases.api.scenario.Scenario
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext

class AddNewPigmentScenario : Scenario() {

    val mainActivityScreen = MainActivityScreen
    val moreFragmentScreen = MoreFragmentScreen
    val pigmentsFragmentScreen =
        PigmentsFragmentScreen
    val addNewPigmentActivityScreen =
        AddNewPigmentActivityScreen
    val pigmentName = "Perma Blend Espresso"


    override val steps: TestContext<Unit>.() -> Unit = {

        step("1. Open MoreFragment") {
            mainActivityScreen {
                moreButton.click()
            }
        }

        step("2. Open PigmentFragment") {
            moreFragmentScreen {
                pigmentsButton.click()
            }
        }

        step("3. Open AddPigmentActivity") {
            pigmentsFragmentScreen {
                addNewPigmentButton.click()
            }
        }


        step("4. Fill pigment information") {
            addNewPigmentActivityScreen {
                pigmentNameEditText.click()
                pigmentNameEditText.replaceText(pigmentName)
                buttonDone.click()
            }
        }

        step("5. Check pigment name") {
            pigmentsFragmentScreen {
                searchResultAtPosition(0).itemPigment.hasText("$pigmentName")
            }
        }
    }
}
