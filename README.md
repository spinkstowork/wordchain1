SDP

I interviewed with a company called eMerlyn. They described this problem to me
as part of a pair programming excercise (1hr duration). There were 2 other coding
exercises during the hour, so we didnt get to this until half past the hour.
We discussed a solution in depth, but I didnt write any code. I didnt get the job,
but I thought that at least I would code up the example as a programming exercise.

Create a word chain given two words and a dictionary.

given: "cat", "dog"

and a dictionary of valid words, Calculate the progression
of the words from the start word to the end word while 
changing only a single letter of the current word at a time.

"cat" -> "cot" -> "dot" -> "dog"

Constraints ------------------------------------------------

Use only lowercase words.

Dictionary must be in text file format. Hyphens, spaces, and
single apostraphes will be filtered out.

This is a single threaded solution.

Default WCEngine.MAX_DEPTH is 200, you may wish to change it

The dictionary included contains > 4500 nouns

When a solution is found, a text file is dumped out containing heuristics
of the head keys and the terminal nodes discovered.

------------------------
Found this solution on github after I solved on my own: https://github.com/coder36/wordchains

My application will work using Mark's dictionary (about 45,000 terms).
