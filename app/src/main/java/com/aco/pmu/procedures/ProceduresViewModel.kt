package com.aco.pmu.procedures

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.aco.pmu.appDatabase.ProceduresEntity

class ProceduresViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: ProceduresRepository =
        ProceduresRepository(application)
    private var allProcedures: LiveData<List<ProceduresEntity>> = repository.getAllProcedures()

    fun insert(proceduresEntity: ProceduresEntity) {
        repository.insert(proceduresEntity)
    }

    fun update(proceduresEntity: ProceduresEntity) {
        repository.update(proceduresEntity)
    }

    fun delete(proceduresEntity: ProceduresEntity) {
        repository.delete(proceduresEntity)
    }

    fun getAllProcedures(): LiveData<List<ProceduresEntity>> {
        return allProcedures
    }
}
