package com.example.aiworkflow.core.domain

import com.example.aiworkflow.core.common.Result
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetHubDestinationsUseCaseTest {
    private val useCase = GetHubDestinationsUseCase()

    @Test
    fun `returns three destinations`() {
        val destinations = useCase()
        assertEquals(3, destinations.size)
    }

    @Test
    fun `destinations have expected routes`() {
        val destinations = useCase()
        val routes = destinations.map { it.route }.toSet()
        assertEquals(setOf("playbook", "benchmark", "release"), routes)
    }

    @Test
    fun `destination routes are unique`() {
        val destinations = useCase()
        val routes = destinations.map { it.route }
        assertEquals(routes.size, routes.toSet().size)
    }
}

class GetBenchmarkDetailUseCaseTest {
    private val fakeRepo = FakeBenchmarkRepository()
    private val useCase = GetBenchmarkDetailUseCase(fakeRepo)

    @Test
    fun `returns detail for valid id`() {
        runTest {
            val result = useCase("s01")
            val detail = (result as Result.Success).data
            assertEquals("s01", detail.scenario.id)
            assertEquals("readme content", detail.readmeMarkdown)
            assertEquals("prompt content", detail.promptMarkdown)
        }
    }

    @Test
    fun `returns error for unknown id`() {
        runTest {
            val result = useCase("unknown")
            assert(result is Result.Error)
        }
    }
}
