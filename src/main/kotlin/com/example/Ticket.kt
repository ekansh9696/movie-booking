package com.example
import java.time.LocalDate
data class Ticket(val seat: Int, val slot: ShowTiming, val movie: Movie,val date: LocalDate= LocalDate.now())

