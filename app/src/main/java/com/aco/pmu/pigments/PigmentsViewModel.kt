package com.aco.pmu.pigments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.aco.pmu.appDatabase.PigmentsEntity

class PigmentsViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: PigmentsRepository =
        PigmentsRepository(application)
    private var allPigments: LiveData<List<PigmentsEntity>> = repository.getAllPigments()

    fun insert(pigmentsEntity: PigmentsEntity) {
        repository.insert(pigmentsEntity)
    }

    fun update(pigmentsEntity: PigmentsEntity) {
        repository.update(pigmentsEntity)
    }

    fun delete(pigmentsEntity: PigmentsEntity) {
        repository.delete(pigmentsEntity)
    }

    fun getAlPigments(): LiveData<List<PigmentsEntity>> {
        return allPigments
    }
}
