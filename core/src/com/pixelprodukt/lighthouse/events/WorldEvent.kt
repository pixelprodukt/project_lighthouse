package com.pixelprodukt.lighthouse.events

import com.badlogic.gdx.Gdx
import com.pixelprodukt.lighthouse.*
import com.pixelprodukt.lighthouse.gameobjects.Character
import com.pixelprodukt.lighthouse.map.GameMap
import kotlinx.coroutines.*

class WorldEvent(val map: GameMap, val behaviour: Behaviour) {

    fun initialize(): CompletableJob {
        val parentJob: CompletableJob = Job()
        val handler = CoroutineExceptionHandler { _, exception -> println("Caught $exception") }
        val coroutineScope = CoroutineScope(Dispatchers.IO + parentJob + handler)

        coroutineScope.launch {
            if (behaviour.type == BehaviourType.WALK) walk(parentJob)
            if (behaviour.type == BehaviourType.IDLE) idle(parentJob)
        }

        return parentJob
    }

    private fun idle(job: CompletableJob) {
        val character = map.gameObjects.find { obj -> obj.id == behaviour.characterId } as Character
        character.startBehaviour(
            UpdateState(null, map),
            Behaviour(BehaviourType.IDLE, behaviour.direction, behaviour.time, retry = true)
        )

        val completeHandler: EventListener = object : EventListener {
            override fun update(objectId: String) {
                if (objectId == behaviour.characterId) {
                    EventHandler.removeEventListener("personIdleComplete", this)
                    job.complete()
                }
            }
        }
        EventHandler.addEventListener("personIdleComplete", completeHandler)
    }

    private fun walk(job: CompletableJob) {
        val character = map.gameObjects.find { obj -> obj.id == behaviour.characterId } as Character
        character.startBehaviour(
            UpdateState(null, map),
            Behaviour(BehaviourType.WALK, behaviour.direction, retry = true)
        )

        val completeHandler: EventListener = object : EventListener {
            override fun update(objectId: String) {
                if (objectId == behaviour.characterId) {
                    EventHandler.removeEventListener("personWalkingComplete", this)
                    job.complete()
                    Gdx.app.log("WorldEvent, completeHandler", "Job Completed")
                }
            }
        }
        Gdx.app.log("WorldEvent, walk", "addEventListener")
        EventHandler.addEventListener("personWalkingComplete", completeHandler)
    }
}