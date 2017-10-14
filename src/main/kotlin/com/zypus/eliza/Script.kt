package com.zypus.eliza

/**
 * Data structures and dsl to create ELIZA scripts.
 * TODO seperate dsl and final data structures
 *
 * @author Fabian Fraenz <f.fraenz@t-online.de>
 *
 * @created 10.10.17
 */

data class Script(
		var initial: String = "",
		var final: String = "",
		val quitList: MutableList<String> = arrayListOf(),
		val preMap: MutableMap<String, String> = hashMapOf(),
		val postMap: MutableMap<String, String> = hashMapOf(),
		val synonymMap: MutableMap<String, Synonym> = hashMapOf(),
		val keyMap: MutableMap<String, Key> = hashMapOf()
) {

	fun onQuit(vararg messages: String) = quitList.addAll(messages)

	fun pre(vararg transformations: Pair<String, String>) = preMap.putAll(transformations)

	fun post(vararg transformations: Pair<String, String>) = postMap.putAll(transformations)

	fun synon(group: String, vararg synonyms: String): Synonym {
		val synonym = Synonym(group)
		synonym.synonyms.add(group)
		synonym.synonyms.addAll(synonyms)
		synonymMap[group] = synonym
		return synonym
	}

	fun key(name: String, weight: Int = 1, block: Key.() -> Unit) {
		val k = Key(name, weight)
		k.block()
		keyMap[name] = k
	}
}

data class Synonym(val group: String, val synonyms: MutableList<String> = arrayListOf()) {
	override fun toString(): String {
		return synonyms.joinToString(separator = "|", prefix = "(?:", postfix = ")")
	}
}

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

	fun match(input: String): Decomposition? {
		return decompositions.firstOrNull { dec ->
			dec.pattern.matches(input)
		}
	}
}

data class Decomposition(val pattern: Regex, val isMemorising: Boolean = false, private val actions: MutableList<Action> = arrayListOf()) {

	var counter = 0

	fun assemble(string: String) {
		actions += Reassembly(string)
	}

	fun goto(target: String) {
		actions += Goto(target)
	}

	fun nextAction(): Action {
		val action = actions[counter]
		counter = (counter + 1) % actions.size
		return action
	}

}

sealed class Action

data class Reassembly(private val pattern: String) : Action() {
	fun assemble(decompositionPattern: Regex, input: String, postMap: MutableMap<String, String>): String {
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