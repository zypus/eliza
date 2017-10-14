# ELIZA

"ELIZA is an early natural language processing computer program created from 1964 to 1966 at the MIT Artificial Intelligence Laboratory by Joseph Weizenbaum." [wikipedia](https://en.wikipedia.org/wiki/ELIZA)

This tiny project is an implementation of the ELIZA program is [Kotlin](https://kotlinlang.org/). Further the famous DOCTOR script is implemented in a custom Kotlin DSL. The DSL makes it very easy to create ELIZA scripts.

## Try it

    git clone https://github.com/zypus/eliza.git
    gradle installDist
    build/install/eliza/bin/eliza