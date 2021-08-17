package com.example

import java.time.LocalDate

data class Show(
    val movie: Movie,
    val capacity: Int,
    val slot: ShowTiming,
    val showDate: LocalDate = LocalDate.now(),
    val booked: Int = 0
) {

    init {

        if (showDate > LocalDate.now().plusDays(7))
            throw error("Cannot Book Past 7 Days")
        else
            showDate

    }

    fun bookTicket(quantity: Int): List<Ticket> {
        validation(quantity)
        val ticketList = mutableListOf<Ticket>()
        for (i in 1..quantity) {
            ticketList.add(Ticket(booked + i, slot, movie,showDate))
        }
        return ticketList
    }

    private fun validation(quantity: Int) {
        if (booked == capacity)
            throw error("Theatre booked")
        if (booked + quantity > capacity)
            throw error("Not enough seats vacant")
    }

    fun updateShow(quantity: Int): Show {
        return Show(movie, capacity, slot, showDate, booked + quantity)
    }

    fun updateMovie(newMovie: Movie): Show {
        if (booked != 0)
            throw error("Cannot update : Tickets booked already")
        else
            return Show(newMovie, capacity, slot)
    }

    override fun equals(other: Any?): Boolean =
        this.hashCode() == other.hashCode()
//                other is Show &&
//                other.movie == movie &&
//                other.capacity == capacity &&
//                other.slot == slot

    override fun hashCode(): Int =
        movie.hashCode() + capacity.hashCode() + slot.hashCode() + showDate.hashCode()


}

enum class ShowTiming {
    Morning,
    Evening,
    Night;
}