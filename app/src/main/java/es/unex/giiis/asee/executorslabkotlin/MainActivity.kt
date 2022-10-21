package es.unex.giiis.asee.executorslabkotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import es.unex.giiis.asee.executorslabkotlin.model.Repo
import java.io.InputStreamReader

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
        // Parse json file into JsonReader
        val reader = JsonReader(
            InputStreamReader(
                resources.openRawResource(R.raw.rrecheve_github_repos)
            )
        )
        // Parse JsonReader into list of Repo using Gson
        val repos: List<Repo> = Gson().fromJson(
            reader,
            object : TypeToken<List<Repo>>() {}.type
        )

        mAdapter = MyAdapter(repos, this)
        recyclerView!!.adapter = mAdapter
    }

    override fun onListInteraction(url: String?) {
        val webpage = Uri.parse(url)
        val webIntent = Intent(Intent.ACTION_VIEW, webpage)
        startActivity(webIntent)
    }
}