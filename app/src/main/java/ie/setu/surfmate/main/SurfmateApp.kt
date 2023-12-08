package ie.setu.surfmate.main

import android.app.Application
import ie.setu.surfmate.models.SurfmateStore
import timber.log.Timber


class SurfmateApp : Application() {
    lateinit var surfspotsStore: SurfmateStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.i("Surfmate Application Started")
    }
}