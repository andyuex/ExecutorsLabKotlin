package es.unex.giiis.asee.executorslabkotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.unex.giiis.asee.executorslabkotlin.model.Repo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), MyAdapter.OnListInteractionListener {
    private var recyclerView: RecyclerView? = null
    private var mAdapter: MyAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById<View>(R.id.repolist) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        mAdapter = MyAdapter(emptyList(), this)

        // Create a very simple REST adapter which points to the API.
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(GitHubService::class.java)
        service.listRepos("rrecheve").enqueue(object : retrofit2.Callback<List<Repo>> {
            override fun onResponse(call: retrofit2.Call<List<Repo>>, response: retrofit2.Response<List<Repo>>) {
                val repos = response.body()
                runOnUiThread {
                    mAdapter!!.swap(repos!!)
                }
            }

            override fun onFailure(call: retrofit2.Call<List<Repo>>, t: Throwable) {
                t.printStackTrace()
            }
        })

        recyclerView!!.adapter = mAdapter
    }

    override fun onListInteraction(url: String?) {
        val webpage = Uri.parse(url)
        val webIntent = Intent(Intent.ACTION_VIEW, webpage)
        startActivity(webIntent)
    }
}
