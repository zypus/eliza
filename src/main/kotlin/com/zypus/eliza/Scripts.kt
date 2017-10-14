package com.zypus.eliza

import com.zypus.eliza.dsl.script

/**
 * Object holding all predefined scripts.
 *
 * @author Zypus
 *
 * @created 10.10.17
 */
object Scripts {

	// The famous DOCTOR script
	val doctor = script {
		initial = "How do you do. Please tell me your problem."
		final = "Goodbye. Thank you for talking to me."

		onQuit("bye", "goodbye", "quit")

		pre(
				"dont" to "don't",
				"cant" to "can't",
				"wont" to "won't",
				"recollect" to "remember",
				"dreamt" to "dreamed",
				"dreams" to "dream",
				"maybe" to "perhaps",
				"how" to "what",
				"when" to "what",
				"certainly" to "yes",
				"machine" to "computer",
				"computers" to "computer",
				"were" to "was",
				"you're" to "you are",
				"i'm" to "i am",
				"same" to "alike"
		)

		post(
				"am" to "are",
				"your" to "my",
				"me" to "you",
				"myself" to "yourself",
				"yourself" to "myself",
				"i" to "you",
				"you" to "I",
				"my" to "your",
				"i'm" to "you are"
		)

		val belief = synonym("belief", "feel", "think", "believe", "wish")
		val family = synonym("family", "mother", "mom", "father", "dad", "sister", "brother", "wife", "children", "child")
		val desire = synonym("desire", "want", "need")
		val sad = synonym("sad", "unhappy", "depressed", "sick")
		val happy = synonym("happy", "elated", "glad", "better")
		val cannot = synonym("cannot", "can't")
		val everyone = synonym("everyone", "everybody", "nobody", "none")
		val be = synonym("be", "am", "is", "are", "was")

		key("xnone") {
			decompose("^(.*)$") {
				assemble("I'm not sure I understand you fully.")
				assemble("Please go on.")
				assemble("What does that suggest to you?")
				assemble("Do you feel strongly about discussing such things?")
			}
		}

		key("sorry") {
			decompose("^(.*)$") {
				assemble("Please don't apologise.")
				assemble("Apologies are not necessary.")
				assemble("I've told you that apologies are not required.")
			}
		}

		key("apologise") {
			decompose("^(.*)$") {
				goto("sorry")
			}
		}
		key("remember", weight = 5) {
			decompose("^(.*)i remember (.*)$") {
				assemble("Do you often think of \$2?")
				assemble("Does thinking of \$2 bring anything else to mind?")
				assemble("What else do you recollect?")
				assemble("Why do you recollect \$2 just now?")
				assemble("What in the present situation reminds you of \$2?")
				assemble("What is the connection between me and \$2?")
			}
			decompose("^(.*)do you remember (.*)$") {
				assemble("Did you think I would forget \$2?")
				assemble("Why do you think I should recall \$2 now?")
				assemble("What about \$2?")
				goto("what")
				assemble("You mentioned \$2?")
			}
		}
		key("if", weight = 3) {
			decompose("^(.*)if (.*)$") {
				assemble("Do you think its likely that \$2?")
				assemble("Do you wish that \$2?")
				assemble("What do you know about \$2?")
				assemble("Really, if \$2?")
			}
		}
		key("dreamed", weight = 4) {
			decompose("^(.*)i dreamed (.*)$") {
				assemble("Really, \$2?")
				assemble("Have you ever fantasized \$2 while you were awake?")
				assemble("Have you ever dreamed \$2 before?")
				goto("dream")
			}
		}
		key("dream", weight = 3) {
			decompose("^(.*)$") {
				assemble("What does that dream suggest to you?")
				assemble("Do you dream often?")
				assemble("What persons appear in your dreams?")
				assemble("Do you believe that dreams have something to do with your problems?")
			}
		}
		key("perhaps") {
			decompose("^(.*)$") {
				assemble("You don't seem quite certain.")
				assemble("Why the uncertain tone?")
				assemble("Can't you be more positive?")
				assemble("You aren't sure?")
				assemble("Don't you know?")
			}
		}
		key("name", weight = 15) {
			decompose("^(.*)$") {
				assemble("I am not interested in names.")
				assemble("I've told you before, I don't care about names -- please continue.")
			}
		}
		key("deutsch") {
			decompose("^(.*)$") {
				goto("xforeign")
				assemble("I told you before, I don't understand German.")
			}
		}
		key("francais") {
			decompose("^(.*)$") {
				goto("xforeign")
				assemble("I told you before, I don't understand French.")
			}
		}
		key("italiano") {
			decompose("^(.*)$") {
				goto("xforeign")
				assemble("I told you before, I don't understand Italian.")
			}
		}
		key("espanol") {
			decompose("^(.*)$") {
				goto("xforeign")
				assemble("I told you before, I don't understand Spanish.")
			}
		}
		key("xforeign") {
			decompose("^(.*)$") {
				assemble("I speak only English.")
			}
		}
		key("hello") {
			decompose("^(.*)$") {
				assemble("How do you do. Please state your problem.")
				assemble("Hi. What seems to be your problem?")
			}
		}
		key("computer", weight = 50) {
			decompose("^(.*)$") {
				assemble("Do computers worry you?")
				assemble("Why do you mention computers?")
				assemble("What do you think machines have to do with your problem?")
				assemble("Don't you think computers can help people?")
				assemble("What about machines worries you?")
				assemble("What do you think about machines?")
			}
		}
		key("am") {
			decompose("^(.*)am i (.*)$") {
				assemble("Do you believe you are \$2?")
				assemble("Would you want to be \$2?")
				assemble("Do you wish I would tell you you are \$2?")
				assemble("What would it mean if you were \$2?")
				goto("what")
			}
			decompose("^(.*)$") {
				assemble("Why do you say 'am'?")
				assemble("I don't understand that.")
			}
		}
		key("are") {
			decompose("^(.*)are you (.*)$") {
				assemble("Why are you interested in whether I am \$2 or not?")
				assemble("Would you prefer if I weren't \$2?")
				assemble("Perhaps I am \$2 in your fantasies.")
				assemble("Do you sometimes think I am \$2?")
				goto("what")
			}
			decompose("^(.*)are (.*)$") {
				assemble("Did you think they might not be \$2?")
				assemble("Would you like it if they were not \$2?")
				assemble("What if they were not \$2?")
				assemble("Possibly they are \$2.")
			}
		}
		key("your") {
			decompose("^(.*)your (.*)$") {
				assemble("Why are you concerned over my \$2?")
				assemble("What about your own \$2?")
				assemble("Are you worried about someone else's \$2?")
				assemble("Really, my \$2?")
			}
		}
		key("was", weight = 2) {
			decompose("^(.*)was i (.*)$") {
				assemble("What if you were \$2?")
				assemble("Do you think you were \$2?")
				assemble("Were you \$2?")
				assemble("What would it mean if you were \$2?")
				assemble("What does \$2 suggest to you?")
				goto("what")
			}
			decompose("^(.*)i was (.*)$") {
				assemble("Were you really?")
				assemble("Why do you tell me you were \$2 now?")
				assemble("Perhaps I already know you were \$2.")
			}
			decompose("^(.*)was you (.*)$") {
				assemble("Would you like to believe I was \$2?")
				assemble("What suggests that I was \$2?")
				assemble("What do you think?")
				assemble("Perhaps I was \$2.")
				assemble("What if I had been \$2?")
			}
		}
		key("i") {
			decompose("^(.*)i $desire (.*)$") {
				assemble("What would it mean to you if you got \$2?")
				assemble("Why do you want \$2?")
				assemble("Suppose you got \$2 soon?")
				assemble("What if you never got \$2?")
				assemble("What would getting \$2 mean to you?")
				assemble("What does wanting \$2 have to do with this discussion?")
			}
			decompose("^(.*)i am(.*) $sad (.*)$") {
				assemble("I am sorry to hear that you are \$3.")
				assemble("Do you think that coming here will help you not to be \$3?")
				assemble("I'm sure it's not pleasant to be \$3.")
				assemble("Can you explain what made you \$3?")
			}
			decompose("^(.*)i am(.*) $happy (.*)$") {
				assemble("How have I helped you to be \$3?")
				assemble("Has your treatment made you \$3?")
				assemble("What makes you \$3 just now?")
				assemble("Can you explain why you are suddenly \$3?")
			}
			decompose("^(.*)i was (.*)$") {
				goto("was")
			}
			decompose("^(.*)i $belief (.*) i (.*)$") {
				assemble("Do you really think so?")
				assemble("But you are not sure you \$3.")
				assemble("Do you really doubt you \$3?")
			}
			decompose("^(.*)i(.*) $belief (.*)you (.*)$") {
				goto("you")
			}
			decompose("^(.*)i am (.*)$") {
				assemble("Is it because you are \$2 that you came to me?")
				assemble("How long have you been \$2?")
				assemble("Do you believe it is normal to be \$2?")
				assemble("Do you enjoy being \$2?")
			}
			decompose("^(.*)i $cannot (.*)$") {
				assemble("How do you think that you can't \$2?")
				assemble("Have you tried?")
				assemble("Perhaps you could \$2 now.")
				assemble("Do you really want to be able to \$2?")
			}
			decompose("^(.*)i don't (.*)$") {
				assemble("Don't you really \$2?")
				assemble("Why don't you \$2?")
				assemble("Do you wish to be able to \$2?")
				assemble("Does that trouble you?")
			}
			decompose("^(.*)do i feel (.*)$") {
				assemble("Tell me more about such feelings.")
				assemble("Do you often feel \$2?")
				assemble("Do you enjoy feeling \$2?")
				assemble("Of what does feeling \$2 remind you?")
			}
			decompose("^(.*)i (.*) you (.*)$") {
				assemble("Perhaps in your fantasies we \$2 each other.")
				assemble("Do you wish to \$2 me?")
				assemble("You seem to need to \$2 me.")
				assemble("Do you \$2 anyone else?")
			}
			decompose("^(.*)$") {
				assemble("You say \$1?")
				assemble("Can you elaborate on that?")
				assemble("Do you say \$1 for some special reason?")
				assemble("That's quite interesting.")
			}
		}
		key("you") {
			decompose("^(.*)you remind me of (.*)$") {
				goto("alike")
			}
			decompose("^(.*)you are (.*)$") {
				assemble("What makes you think I am \$2?")
				assemble("Does it please you to believe I am \$2?")
				assemble("Do you sometimes wish you were \$2?")
				assemble("Perhaps you would like to be \$2.")
			}
			decompose("^(.*)you(.*) me (.*)$") {
				assemble("Why do you think I \$2 you?")
				assemble("You like to think I \$2 you -- don't you?")
				assemble("What makes you think I \$2 you?")
				assemble("Really, I \$2 you?")
				assemble("Do you wish to believe I \$2 you?")
				assemble("Suppose I did \$2 you -- what would that mean?")
				assemble("Does someone else believe I \$2 you?")
			}
			decompose("^(.*)you (.*)$") {
				assemble("We were discussing you -- not me.")
				assemble("Oh, I \$2?")
				assemble("You're not really talking about me -- are you?")
				assemble("What are your feelings now?")
			}
		}
		key("yes") {
			decompose("^(.*)$") {
				assemble("You seem to be quite positive.")
				assemble("You are sure.")
				assemble("I see.")
				assemble("I understand.")
			}
		}
		key("no") {
			decompose("^(.*)$") {
				assemble("Are you saying no just to be negative?")
				assemble("You are being a bit negative.")
				assemble("Why not?")
				assemble("Why 'no'?")
			}
		}
		key("my", weight = 2) {
			decompose("(.*) my (.*)", memorising = true) {
				assemble("Lets discuss further why your \$2.")
				assemble("Earlier you said your \$2.")
				assemble("But your \$2.")
				assemble("Does that have anything to do with the fact that your \$2?")
			}
			decompose("^(.*)my(.*) $family (.*)$") {
				assemble("Tell me more about your family.")
				assemble("Who else in your family \$3?")
				assemble("Your \$2?")
				assemble("What else comes to mind when you think of your \$2?")
			}
			decompose("^(.*)my (.*)$") {
				assemble("Your \$2?")
				assemble("Why do you say your \$2?")
				assemble("Does that suggest anything else which belongs to you?")
				assemble("Is it important that your \$2?")
			}
		}
		key("can") {
			decompose("^(.*)can you (.*)$") {
				assemble("You believe I can \$2 don't you?")
				goto("what")
				assemble("You want me to be able to \$2.")
				assemble("Perhaps you would like to be able to \$2 yourself.")
			}
			decompose("^(.*)can i (.*)$") {
				assemble("Whether or not you can \$2 depends on you more than me.")
				assemble("Do you want to be able to \$2?")
				assemble("Perhaps you don't want to \$2.")
				goto("what")
			}
		}
		key("what") {
			decompose("^(.*)$") {
				assemble("Why do you ask?")
				assemble("Does that question interest you?")
				assemble("What is it you really wanted to know?")
				assemble("Are such questions much on your mind?")
				assemble("What type would please you most?")
				assemble("What do you think?")
				assemble("What comes to mind when you ask that?")
				assemble("Have you asked such questions before?")
				assemble("Have you asked anyone else?")
			}
		}
		key("because") {
			decompose("^(.*)$") {
				assemble("Is that the real reason?")
				assemble("Don't any other reasons come to mind?")
				assemble("Does that reason seem to explain anything else?")
				assemble("What other reasons might there be?")
			}
		}
		key("why") {
			decompose("^(.*)why don't you (.*)$") {
				assemble("Do you believe I don't \$2?")
				assemble("Perhaps I will \$2 in good time.")
				assemble("Should you \$2 yourself?")
				assemble("You want me to \$2?")
				goto("what")
			}
			decompose("^(.*)why can't i (.*)$") {
				assemble("Do you think you should be able to \$2?")
				assemble("Do you want to be able to \$2?")
				assemble("Do you believe this will help you to \$2?")
				assemble("Have you any idea why you can't \$2?")
				goto("what")
			}
			decompose("^(.*)$") {
				goto("what")
			}
		}
		key("everyone", weight = 2) {
			decompose("^(.*)$everyone (.*)$") {
				assemble("Really, \$2?")
				assemble("Surely not \$2.")
				assemble("Can you think of anyone in particular?")
				assemble("Who, for example?")
				assemble("Are you thinking of a very special person?")
				assemble("Who, may I ask?")
				assemble("Someone special perhaps?")
				assemble("You have a particular person in mind, don't you?")
				assemble("Who do you think you're talking about?")
			}
		}
		key("everybody", weight = 2) {
			decompose("^(.*)$") {
				goto("everyone")
			}
		}
		key("nobody", weight = 2) {
			decompose("^(.*)$") {
				goto("everyone")
			}
		}
		key("noone", weight = 2) {
			decompose("^(.*)$") {
				goto("everyone")
			}
		}
		key("always", weight = 1) {
			decompose("^(.*)$") {
				assemble("Can you think of a specific example?")
				assemble("When?")
				assemble("What incident are you thinking of?")
				assemble("Really, always?")
			}
		}
		key("alike", weight = 10) {
			decompose("^(.*)$") {
				assemble("In what way?")
				assemble("What resemblance do you see?")
				assemble("What does that similarity suggest to you?")
				assemble("What other connections do you see?")
				assemble("What do you suppose that resemblance means?")
				assemble("What is the connection, do you suppose?")
				assemble("Could here really be some connection?")
				assemble("How?")
			}
		}
		key("like", weight = 10) {
			decompose("^(.*)$be (.*)like (.*)$") {
				goto("alike")
			}
		}

	}

}