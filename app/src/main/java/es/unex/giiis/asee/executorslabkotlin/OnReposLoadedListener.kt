package es.unex.giiis.asee.executorslabkotlin

import es.unex.giiis.asee.executorslabkotlin.model.Repo


interface OnReposLoadedListener {
    fun onReposLoaded(repos: List<Repo>)
}
