package com.zypus.eliza.dsl

/**
 * DSL to easily create ELIZA scripts.
 *
 * @author Zypus
 *
 * @created 14.10.17
 */

@DslMarker
annotation class ElizaMarker

@ElizaMarker
data class Script(
		var initial: String = "",
		var final: String = "",
		val quits: MutableList<String> = arrayListOf(),
		val preTransformations: MutableMap<String, String> = hashMapOf(),
		val postTransformations: MutableMap<String, String> = hashMapOf(),
		val synonyms: MutableMap<String, Synonym> = hashMapOf(),
		val keys: MutableMap<String, Key> = hashMapOf()
) {

	fun onQuit(vararg messages: String) = quits.addAll(messages)

	fun pre(vararg transformations: Pair<String, String>) = preTransformations.putAll(transformations)

	fun post(vararg transformations: Pair<String, String>) = postTransformations.putAll(transformations)

	fun synonym(group: String, vararg synonyms: String): Synonym {
		val synonym = Synonym(group)
		synonym.synonyms.add(group)
		synonym.synonyms.addAll(synonyms)
		this.synonyms[group] = synonym
		return synonym
	}

	fun key(name: String, weight: Int = 1, block: Key.() -> Unit) {
		val k = Key(name, weight)
		k.block()
		keys[name] = k
	}
}

@ElizaMarker
data class Synonym(val group: String, val synonyms: MutableList<String> = arrayListOf()) {
	override fun toString(): String {
		return synonyms.joinToString(separator = "|", prefix = "(?:", postfix = ")")
	}
}

@ElizaMarker
data class Key(val name: String, val weight: Int, private val decompositions: MutableList<Decomposition> = arrayListOf(), val index: Int = nextIndex) {

	companion object {
		private var nextIndex_ = 0

		val nextIndex: Int
			get() {
				val index = nextIndex_
				nextIndex_++
				return index
			}
	}

	fun decompose(pattern: Regex, memorising: Boolean = false, block: Decomposition.() -> Unit) {
		val dec = Decomposition(pattern, memorising)
		dec.block()
		decompositions += dec
	}

	fun decompose(string: String, memorising: Boolean = false, block: Decomposition.() -> Unit) = decompose(string.toRegex(), memorising, block)

	internal fun match(input: String): Decomposition? {
		return decompositions.firstOrNull { dec ->
			dec.pattern.matches(input)
		}
	}
}

@ElizaMarker
data class Decomposition(val pattern: Regex, val isMemorising: Boolean = false, private val actions: MutableList<Action> = arrayListOf()) {

	private var counter = 0

	fun assemble(string: String) {
		actions += Reassembly(string)
	}

	fun goto(target: String) {
		actions += Goto(target)
	}

	internal fun nextAction(): Action {
		val action = actions[counter]
		counter = (counter + 1) % actions.size
		return action
	}

}

@ElizaMarker
sealed class Action

data class Reassembly(private val pattern: String) : Action() {
	internal fun assemble(decompositionPattern: Regex, input: String, postMap: MutableMap<String, String>): String {
		return input.replace(decompositionPattern) { result ->
			pattern.replace("\\$\\d".toRegex()) { placeholder ->
				val index = placeholder.value.drop(1).toInt()
				result.groupValues[index].split(" ").joinToString(separator = " ") {
					postMap.getOrDefault(it, it)
				}
			}
		}
	}
}

data class Goto(val target: String) : Action()

fun script(block: Script.() -> Unit): Script {
	val s = Script()
	s.block()
	return s
}