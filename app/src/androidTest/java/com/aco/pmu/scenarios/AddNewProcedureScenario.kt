package com.aco.pmu.scenarios

import com.aco.pmu.screens.*
import com.aco.pmu.AddNewProcedure.AddNewProcedureActivityScreen
import com.aco.pmu.AddNewProcedure.ProceduresFragmentScreen
import com.kaspersky.kaspresso.testcases.api.scenario.Scenario
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext

class AddNewProcedureScenario : Scenario() {

    val mainActivityScreen = MainActivityScreen
    val moreFragmentScreen = MoreFragmentScreen
    val proceduresFragmentScreen =
        ProceduresFragmentScreen
    val addNewProcedureActivityScreen =
        AddNewProcedureActivityScreen
    val procedureName = "Перманентный макияж бровей"


    override val steps: TestContext<Unit>.() -> Unit = {

        step("1. Open MoreFragment") {
            mainActivityScreen {
                moreButton.click()
            }
        }

        step("2. Open ProceduresFragment") {
            moreFragmentScreen {
                procedureButton.click()
            }
        }

        step("3. Open AddProcedureActivity") {
            proceduresFragmentScreen {
                addNewProcedureButton.click()
            }
        }


        step("4. Fill procedure information") {
            addNewProcedureActivityScreen {
                procedureNameEditText.click()
                procedureNameEditText.replaceText(procedureName)
                buttonDone.click()
            }
        }

        step("5. Check procedure name") {
            proceduresFragmentScreen {
                searchResultAtPosition(0).itemProcedure.hasText("$procedureName")
            }
        }
    }
}
