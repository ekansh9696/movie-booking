package com.example

data class Theatre(val shows: Set<Show>) {
    fun booking(show: Show, quantity: Int = 1): Pair<Theatre, List<Ticket>> {
        val ticket = show.bookTicket(quantity)
        return Pair(Theatre(updateShows(show, quantity)), ticket)
    }
    private fun updateShows(show: Show, quantity: Int): Set<Show> {
        val updatedList = shows.toMutableSet()
        updatedList.remove(show)
        updatedList.add(show.updateShow(quantity))
        return updatedList
    }

    fun updateMovieInShow(show: Show, movie: Movie): Theatre {
        validation(show)
        val updatedList = shows.toMutableSet()
        updatedList.remove(show)
        updatedList.add(show.updateMovie(movie))
        return Theatre(updatedList)
    }

    fun findShow(show: Show): Show {
        validation(show)
        return shows.find { it == show  }!!
    }

    fun findShows(movie: Movie): Set<Show> {
        return shows.filter { it.movie == movie && it.booked < it.capacity}.toSet()
    }

    private fun validation(show: Show) {
        if (show !in shows)
            throw error("Show doesn't exist")
    }
}