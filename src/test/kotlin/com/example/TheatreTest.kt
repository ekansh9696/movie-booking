package com.example

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate


class TheatreTest : StringSpec({
    "should return 1 as the seat number and correct updated show details for 1 ticket booked for a Show Today" {

        val showList = setOf(
            Show(Movie("A"), 100, ShowTiming.Morning),
            Show(Movie("A"), 100, ShowTiming.Evening),
            Show(Movie("A"), 100, ShowTiming.Night)
        )
        val session1 = Theatre(showList)
        val (updatedSession1, ticket) = session1.booking( Show(Movie("A"), 100, ShowTiming.Morning))
        ticket shouldBe listOf( Ticket(1, ShowTiming.Morning, Movie("A")))
        updatedSession1.findShow(Show(Movie("A"), 100, ShowTiming.Morning)).capacity shouldBe 100
        updatedSession1.findShow(Show(Movie("A"), 100, ShowTiming.Morning)).booked shouldBe 1
    }

    "should return a correct ticket for nth ticket booked for a Show Today" {
        val showList = setOf(
            Show(Movie("A"), 100, ShowTiming.Morning),
            Show(Movie("A"), 100, ShowTiming.Evening),
            Show(Movie("A"), 100, ShowTiming.Night)
        )
        val session = Theatre(showList)
        val (newSession, tickets) = session.booking(Show(Movie("A"), 100, ShowTiming.Evening),6)
        tickets[5] shouldBe Ticket(6, ShowTiming.Evening, Movie("A"))
        newSession.findShow(Show(Movie("A"), 100, ShowTiming.Evening)).capacity shouldBe 100
        newSession.findShow(Show(Movie("A"), 100, ShowTiming.Evening)).booked shouldBe 6
    }

    "should return an exception when creating a show 8 days from now"{
        val exception = shouldThrow<java.lang.IllegalStateException> {
            Show(Movie("A"), 100, ShowTiming.Night, LocalDate.now().plusDays(8))
        }
        exception.message shouldBe "Cannot Book Past 7 Days"
    }

    "should return an exception if the seat number exceeds 100 tickets (quantity of tickets greater than remaining seats)" {
        val showList = setOf(
            Show(Movie("A"), 100, ShowTiming.Morning),
            Show(Movie("A"), 100, ShowTiming.Evening),
            Show(Movie("A"), 100, ShowTiming.Night)
        )
        val exception= shouldThrow<IllegalStateException> {
            val session = Theatre(showList)
            session.booking(Show(Movie("A"), 100, ShowTiming.Evening),101)
        }
        exception.message shouldBe "Not enough seats vacant"
    }

    "should return the correct show when a show is searched for"{
        val showList = setOf(
            Show(Movie("A"), 100, ShowTiming.Morning),
            Show(Movie("A"), 100, ShowTiming.Evening),
            Show(Movie("A"), 100, ShowTiming.Night)
        )
        val session = Theatre(showList)
        session.findShow(Show(Movie("A"), 100, ShowTiming.Morning)) shouldBe Show(Movie("A"), 100, ShowTiming.Morning)
    }

    "should return an exception when a show is searched for that do not exist"{
        val exception = shouldThrow<IllegalStateException> {
            val showList = setOf(
                Show(Movie("A"), 100, ShowTiming.Morning),
                Show(Movie("A"), 100, ShowTiming.Evening),
                Show(Movie("A"), 100, ShowTiming.Night)
            )
            val session = Theatre(showList)
            session.findShow(Show(Movie("B"), 100, ShowTiming.Morning))
        }
        exception.message shouldBe "Show doesn't exist"
    }

    "should return the correct set of shows when we searched shows for a certain movie"{
        val showList = setOf(
            Show(Movie("A"), 100, ShowTiming.Morning, LocalDate.now().plusDays(1)),
            Show(Movie("A"), 100, ShowTiming.Evening, LocalDate.now().plusDays(2)),
            Show(Movie("A"), 100, ShowTiming.Night, LocalDate.now().plusDays(2)),
            Show(Movie("A"), 100, ShowTiming.Night),
            Show(Movie("B"), 100, ShowTiming.Night)
        )
        val session = Theatre(showList)
        session.findShows(Movie("A")) shouldBe setOf(
            Show(Movie("A"), 100, ShowTiming.Morning, LocalDate.now().plusDays(1)),
            Show(Movie("A"), 100, ShowTiming.Evening, LocalDate.now().plusDays(2)),
            Show(Movie("A"), 100, ShowTiming.Night, LocalDate.now().plusDays(2)),
            Show(Movie("A"), 100, ShowTiming.Night),
        )
    }

    "should return list of Tickets for multiple tickets booked " {

        val showList = setOf(
            Show(Movie("A"), 100, ShowTiming.Morning),
            Show(Movie("A"), 100, ShowTiming.Evening),
            Show(Movie("A"), 100, ShowTiming.Night)
        )
        val session1 = Theatre(showList)
        val (updatedSession1, ticket) = session1.booking(Show(Movie("A"), 100, ShowTiming.Morning),2)
        ticket shouldBe listOf( Ticket(1, ShowTiming.Morning, Movie("A")),Ticket(2, ShowTiming.Morning, Movie("A")))
        updatedSession1.findShow(Show(Movie("A"), 100, ShowTiming.Morning)).capacity shouldBe 100
        updatedSession1.findShow(Show(Movie("A"), 100, ShowTiming.Morning)).booked shouldBe 2
    }

    "should return updated show list when updating a movie " {

        val showList = setOf(
            Show(Movie("A"), 100, ShowTiming.Morning),
            Show(Movie("A"), 100, ShowTiming.Evening),
            Show(Movie("A"), 100, ShowTiming.Night)
        )
        val session1 = Theatre(showList)
        val updatedSession=session1.updateMovieInShow(Show(Movie("A"), 100, ShowTiming.Morning),Movie("Jeena"))
        updatedSession.shows shouldBe setOf(
            Show(Movie("Jeena"), 100, ShowTiming.Morning),
            Show(Movie("A"), 100, ShowTiming.Evening),
            Show(Movie("A"), 100, ShowTiming.Night)
        )
    }

    "should return an exception when updating a movie on a show that do not exist" {
        val exception = shouldThrow<IllegalStateException> {
            val showList = setOf(
                Show(Movie("A"), 100, ShowTiming.Morning),
                Show(Movie("A"), 100, ShowTiming.Evening),
                Show(Movie("A"), 100, ShowTiming.Night)
            )
            val session1 = Theatre(showList)
            session1.updateMovieInShow(Show(Movie("clueless"), 100, ShowTiming.Morning), Movie("Jeena"))
        }
        exception.message shouldBe "Show doesn't exist"
    }

    "should return an empty set of shows when we searched shows for a certain movie for which no seats are left"{
        val showList = setOf(
            Show(Movie("A"), 100, ShowTiming.Morning),
            Show(Movie("A"), 100, ShowTiming.Evening),
            Show(Movie("B"), 100, ShowTiming.Night),
        )
        val session = Theatre(showList)
        val updatedSession1 = session.booking(Show(Movie("B"), 100, ShowTiming.Night),100).first
        updatedSession1.findShows(Movie("B")) shouldBe setOf()
    }
})