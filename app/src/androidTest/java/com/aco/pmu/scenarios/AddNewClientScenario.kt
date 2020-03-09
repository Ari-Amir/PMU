package com.aco.pmu.scenarios

import com.aco.pmu.AddNewClient.AddNewClientActivityScreen
import com.aco.pmu.AddNewClient.ClientsFragmentScreen
import com.aco.pmu.screens.MainActivityScreen
import com.kaspersky.kaspresso.testcases.api.scenario.Scenario
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext

class AddNewClientScenario : Scenario() {

    val mainActivityScreen = MainActivityScreen
    val clientsFragmentScreen =
        ClientsFragmentScreen
    val addNewClientActivityScreen =
        AddNewClientActivityScreen
    val clientsName = "Арыстан"
    val clientsSurname = "Амиров"
    val clientsPhone1 = "7011019712"
    val clientsPhone2 = "7017777777"
    val clientsComments = "Постоянный клиент"


    override val steps: TestContext<Unit>.() -> Unit = {

            step("1. Open ClientsFragment") {
                mainActivityScreen {
                    clientsButton.click()
                }
            }

            step("2. Open AddClientsActivity") {
                clientsFragmentScreen {
                    addNewClientButton.click()
                }
            }

            step("3. Fill clients information") {
                addNewClientActivityScreen {
                    clientsNameEditText.click()
                    clientsNameEditText.replaceText(clientsName)
                    clientsSurnameNameEditText.replaceText(clientsSurname)
                    clientsPhoneNumber1EditText.typeText(clientsPhone1)
                    clientsPhoneNumber2EditText.typeText(clientsPhone2)
                    clientsCommentsEditText.replaceText(clientsComments)
                    buttonDone.click()
                }
            }

            step("4. Check clients name, surname") {
                clientsFragmentScreen {
                    searchResultAtPosition(0).itemNameSurname.hasText("$clientsName $clientsSurname")
                }
            }
        }
    }

