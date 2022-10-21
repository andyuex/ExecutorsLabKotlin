package es.unex.giiis.asee.executorslabkotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.unex.giiis.asee.executorslabkotlin.model.Repo


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

        AppExecutors.instance?.networkIO()?.execute {
            ReposNetworkLoaderRunnable(object : OnReposLoadedListener {
                override fun onReposLoaded(repos: List<Repo>) {
                    runOnUiThread {
                        mAdapter!!.swap(repos)
                    }
                }
            }).run()
        }

        recyclerView!!.adapter = mAdapter
    }

    override fun onListInteraction(url: String?) {
        val webpage = Uri.parse(url)
        val webIntent = Intent(Intent.ACTION_VIEW, webpage)
        startActivity(webIntent)
    }
}
