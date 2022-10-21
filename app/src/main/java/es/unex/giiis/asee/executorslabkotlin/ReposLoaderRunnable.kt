package es.unex.giiis.asee.executorslabkotlin

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import es.unex.giiis.asee.executorslabkotlin.model.Repo
import kotlinx.coroutines.Runnable
import java.io.InputStream
import java.io.InputStreamReader

class ReposLoaderRunnable(
    private val inFile: InputStream,
    private val onReposLoadedListener: OnReposLoadedListener
) : Runnable {

    override fun run() {
        // Parse json file into JsonReader
        val reader = JsonReader(
            InputStreamReader(
                inFile
            )
        )

        // Parse JsonReader into list of Repo using Gson
        val repos: List<Repo> = Gson().fromJson(
            reader,
            object : TypeToken<List<Repo>>() {}.type
        )

        for (repo in repos) {
            try {
                Thread.sleep(500)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        // Callback with list of repos
        AppExecutors.instance?.mainThread()?.execute {
            onReposLoadedListener.onReposLoaded(repos)
        }
    }
}
