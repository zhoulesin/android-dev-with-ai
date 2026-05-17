package com.example.aiworkflow.core.domain

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ToggleChecklistItemUseCaseTest {
    private val fakeRepo = FakeChecklistRepository()
    private val toggleUseCase = ToggleChecklistItemUseCase(fakeRepo)
    private val observeUseCase = ObserveReleaseChecklistUseCase(fakeRepo)
    private val resetUseCase = ResetReleaseChecklistUseCase(fakeRepo)

    @Test
    fun `toggle unchecked item to checked`() {
        runTest {
            toggleUseCase("a1")
            observeUseCase().test {
                val sections = awaitItem()
                val item = sections.first().items.first { it.id == "a1" }
                assertTrue(item.checked)
            }
        }
    }

    @Test
    fun `toggle checked item to unchecked`() {
        runTest {
            toggleUseCase("a2")
            observeUseCase().test {
                val sections = awaitItem()
                val item = sections.first().items.first { it.id == "a2" }
                assertFalse(item.checked)
            }
        }
    }

    @Test
    fun `reset clears all checked states`() {
        runTest {
            toggleUseCase("a1")
            resetUseCase()
            observeUseCase().test {
                val sections = awaitItem()
                val allUnchecked = sections.flatMap { it.items }.none { it.checked }
                assertTrue(allUnchecked)
            }
        }
    }

    @Test
    fun `toggle unknown id does not crash`() {
        runTest {
            toggleUseCase("non-existent")
            observeUseCase().test {
                val sections = awaitItem()
                assertEquals(2, sections.first().items.size)
            }
        }
    }
}
