package ie.setu.surfmate.main

import android.app.Application
import ie.setu.surfmate.models.SurfmateStore


class SurfmateApp : Application() {
    lateinit var surfspotsStore: SurfmateStore

    override fun onCreate() {
        super.onCreate()
    }
}