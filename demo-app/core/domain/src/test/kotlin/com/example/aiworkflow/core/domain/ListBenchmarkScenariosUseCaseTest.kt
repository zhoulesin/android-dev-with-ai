package com.example.aiworkflow.core.domain

import com.example.aiworkflow.core.common.Result
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ListBenchmarkScenariosUseCaseTest {
    private val fakeRepo = FakeBenchmarkRepository()
    private val useCase = ListBenchmarkScenariosUseCase(fakeRepo)

    @Test
    fun `returns list of scenarios`() {
        runTest {
            val result = useCase()
            assertTrue(result is Result.Success)
            val scenarios = (result as Result.Success).data
            assertEquals(2, scenarios.size)
        }
    }

    @Test
    fun `scenario ids are unique`() {
        runTest {
            val result = useCase()
            val scenarios = (result as Result.Success).data
            val ids = scenarios.map { it.id }
            assertEquals(ids.size, ids.toSet().size)
        }
    }
}
