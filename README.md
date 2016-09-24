# NLPAssessment

## Assumptions
* Whitespace and special character/punctuation tokens are included in the output
* Proper noun matching is case sensitive
* If a series of tokens matches more than one named entity, we should map it to the longest named entity (e.g. "John" matches "John" and "John Feldhaus", so it should be mapped to "John Feldhaus" since it is longer)

## Limitations
* Doesn't work well with abbreviations such as "Mr.", "Inc.", "Dr.", etc... because it thinks it is the end of a sentence. This could be mitigated by adding a list of abbreviations to check against when determining if a '.' is the end of a sentence or not.
* May not recognized proper nouns that include a period that it interprets as a sentence ending (such as "Dr. Greg House"). This could be mitigated in a similar way as the above limitation.
* If a given proper noun is very short (such as "a") then it may slow down the runtime since it will trigger a potential match for many tokens.
* Is not smart enough to recognize proper nouns that may be slightly misspelled or deliberately modified (e.g. Typing "O r a c l e" instead of "Oracle")

## Alternate Approaches
* I am taking two passes through each file - the first pass does the basic tokenization and the second pass looks for token combinations 
that form a recognized proper noun (named entity) and combines them into one token. This could be done in one pass instead.  I chose to
do it in two passes mostly for code readability and due to time constraints.
