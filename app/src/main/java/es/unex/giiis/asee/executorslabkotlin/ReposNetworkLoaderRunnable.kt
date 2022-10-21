package es.unex.giiis.asee.executorslabkotlin

import es.unex.giiis.asee.executorslabkotlin.model.Repo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ReposNetworkLoaderRunnable(
    private val onReposLoadedListener: OnReposLoadedListener
): Runnable {

    override fun run() {
        // Retrofit instantiation and asynchronous call
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(GitHubService::class.java)
        // Call listener with obtained repos
        try {
            val repos = service.listRepos("rrecheve").execute().body()
            AppExecutors.instance?.mainThread()?.execute {
                onReposLoadedListener.onReposLoaded(repos!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        object : retrofit2.Callback<List<Repo>> {
            override fun onResponse(call: retrofit2.Call<List<Repo>>, response: retrofit2.Response<List<Repo>>) {
                val repos = response.body()
                AppExecutors.instance?.mainThread()?.execute {
                    onReposLoadedListener.onReposLoaded(repos!!)
                }
            }

            override fun onFailure(call: retrofit2.Call<List<Repo>>, t: Throwable) {
                t.printStackTrace()
            }
        }

        // Llamada al Listener con los datos obtenidos
    }
}
