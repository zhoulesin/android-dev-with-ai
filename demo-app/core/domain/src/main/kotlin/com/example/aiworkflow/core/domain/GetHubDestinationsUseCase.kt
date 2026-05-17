package com.example.aiworkflow.core.domain

import com.example.aiworkflow.core.model.HubDestination

class GetHubDestinationsUseCase {
    operator fun invoke(): List<HubDestination> =
        listOf(
            HubDestination(
                route = "playbook",
                title = "配置",
                description = "Rules / Skills / Prompts",
            ),
            HubDestination(
                route = "benchmark",
                title = "考卷",
                description = "Android AI 模型评测场景",
            ),
            HubDestination(
                route = "release",
                title = "发布",
                description = "发包前检查清单",
            ),
        )
}
