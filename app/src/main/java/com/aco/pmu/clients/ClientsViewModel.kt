package com.aco.pmu.clients

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.aco.pmu.appDatabase.ClientsEntity
import com.aco.pmu.clients.ClientsRepository

class ClientsViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: ClientsRepository =
        ClientsRepository(application)
    private var allClients: LiveData<List<ClientsEntity>> = repository.getAllClients()
    private var clientsCount: LiveData<Int> = repository.getClientsCount()

    fun insert(clientsEntity: ClientsEntity) {
        repository.insert(clientsEntity)
    }

    fun update(clientsEntity: ClientsEntity) {
        repository.update(clientsEntity)
    }

    fun delete(clientsEntity: ClientsEntity) {
        repository.delete(clientsEntity)
    }

    fun getAllClients(): LiveData<List<ClientsEntity>> {
        return allClients
    }

    fun getClientsCount(): LiveData<Int> {
        return clientsCount
    }
}
