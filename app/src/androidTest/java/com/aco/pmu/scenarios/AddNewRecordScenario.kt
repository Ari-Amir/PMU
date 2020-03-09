package com.aco.pmu.scenarios

import androidx.test.espresso.action.ViewActions.click
import com.aco.pmu.screens.*
import com.aco.pmu.AddNewRecord.*
import com.kaspersky.kaspresso.testcases.api.scenario.Scenario
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext


class AddNewRecordScenario : Scenario() {

    val mainActivityScreen = MainActivityScreen
    val recordsFragmentScreen =
        RecordsFragmentScreen
    val addNewRecordsActivityScreen =
        AddNewRecordActivityScreen
    val datePickerDialogFragment =
        DatePickerDialogFragment
    val timePickerDialogFragment =
        TimePickerDialogFragment
    val clientSelectFragmentScreen =
        ClientSelectFragmentScreen
    val procedureSelectFragmentScreen =
        ProcedureSelectFragmentScreen
    val pigmentSelectFragmentScreen =
        PigmentSelectFragmentScreen
    val spinnerItemSelectFragmentScreen =
        SpinnerItemSelectFragmentScreen

    val price = "15000"
    val comments = "bla bla bla"


        override val steps: TestContext<Unit>.() -> Unit = {


            step("1. Open RecordsFragment") {
                mainActivityScreen {
                    recordsButton.click()
                }
            }

            step("2. Open AddRecordActivity") {
                recordsFragmentScreen {
                    addNewRecordButton.click()
                }
            }

            step("3. Fill records information") {
                addNewRecordsActivityScreen {
                    dateSelectTextView.click()
                    datePickerDialogFragment.datePickerDialog.datePicker.setDate(2020,2,23)
                    datePickerDialogFragment.datePickerDialog.okButton.click()

                    timeSelectTextView.click()
                    timePickerDialogFragment.timePickerDialog.timePicker.setTime(13, 30)
                    timePickerDialogFragment.timePickerDialog.okButton.click()

                    nameSurnameSelectTextView.click()
                    clientSelectFragmentScreen.searchResultAtPosition(0).click()

                    procedureSelectTextView.click()
                    procedureSelectFragmentScreen.searchResultAtPosition(0).click()

                    doneSwitch.click()

                    pigmentsSelectTextView1.click()
                    pigmentSelectFragmentScreen.searchResultAtPosition(0).click()

                    spinner1.click()
                    spinnerItemSelectFragmentScreen.searchItemAtPosition(4).perform(click())

                    pigmentsSelectTextView2.click()
                    pigmentSelectFragmentScreen.searchResultAtPosition(0).click()

                    spinner2.click()
                    spinnerItemSelectFragmentScreen.searchItemAtPosition(10).perform(click())

                    pigmentsSelectTextView3.click()
                    pigmentSelectFragmentScreen.searchResultAtPosition(0).click()

                    spinner3.click()
                    spinnerItemSelectFragmentScreen.searchItemAtPosition(15).perform(click())

                    priceEditText.replaceText(price)

                    commentsEditText.replaceText(comments)

                    Thread.sleep(5000)

                    buttonDone.click()

                }
            }

            step("4. Check record information") {

                }
            }
        }


