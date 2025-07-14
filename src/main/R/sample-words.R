#!/usr/bin/Rscript
#
# This file is part of cryptator, https://github.com/arnaud-m/cryptator
#
# Copyright (c) 2021-2025, Université Côte d'Azur. All rights reserved.
#
# Licensed under the BSD 3-clause license.
# See LICENSE file in the project root for full license information.
#



ScanWords <- function(file) scan(file = file, what = character(), quiet = TRUE)


WriteWords <- function(letters, words) {
    ## Generate the filename
    len <- min(sapply(words, nchar))
    idx <- sample.int(999, 1)
    filename <- sprintf("L%02dN%02d-%s-%03d.txt", len, length(words), letters, idx)
    ## Write words
    sink(filename)
    cat(words, sep = '\n')
    sink()
    cat("Generate", filename,"with", length(words),"words\n")
}
################################
## Generate a random letters

## Define a named vector of the frequency of each latin alphabet letter
## https://fr.wikipedia.org/wiki/Fr%C3%A9quence_d%27apparition_des_lettres_en_fran%C3%A7ais
frequencies <- c(7.45, 1.14, 3.24, 3.67, 14.44, 1.11, 1.23, 1.11, 6.64, 0.34, 0.29, 4.96, 2.62, 6.39, 5.07, 2.49, 0.65, 6.07, 6.51, 5.92, 4.54, 1.11, 0.17, 0.38, 0.46, 0.15)
names(frequencies) <- letters

## Split vowels and consons
vowels <- c("a", "e", "i", "o", "u", "y")
freqV <- frequencies[ vowels ]
freqC <- frequencies[  ! letters %in% vowels]

## Define a helper function for drawing random letters
DrawLetters <- function(nletters, nvowels) {
    nvowels <- max(0, min(nletters, length(freqV), nvowels))
    nconsons <- max(0, min(length(freqC), nletters - nvowels))
    letters <- c(
        sample(names(freqV), size = nvowels, prob = freqV),
        sample(names(freqC), size = nconsons, prob = freqC)
    )
    paste(sort(letters), collapse = "")
}

################################
## Parse command line arguments

library(argparser, quietly=TRUE)

## Create a parser
p <- arg_parser("Generate random word lists")

## Add command line arguments
p <- add_argument(p, "dictionary", help="path of the dictionary text file")
p <- add_argument(p, "--number", help="number of tentatives", type="integer", default = 1)
p <- add_argument(p, "--letters", help="number of letters", type="integer", default = 10)
p <- add_argument(p, "--vowels", help="number of vowels", type="integer", default = 4)
p <- add_argument(p, "--words", help="number of words per list", type="integer", default = 50)
p <- add_argument(p, "--minlen", help="minimal word length", type="integer", default = 2)
p <- add_argument(p, "--maxlen", help="maximal word length", type="integer", default = Inf)

## Parse the command line arguments
argv <- parse_args(p)

################################
## Main script

## Scan and filter words
words <- ScanWords(argv$dictionary)
cat("Scan", length(words), "words in", argv$dictionary,"\n")

words <- subset(words, (argv$minlen <= nchar(words)) & (nchar(words) <= argv$maxlen))
cat("Find ", length(words), " words with length in [", argv$minlen, ", ", argv$maxlen, "]\n", sep = "")

## Try generating words lists
i <- 0 # instance
j <- 0 # tentatives

if(length(words) >= argv$words) {
    while(j < argv$number) {
        ## Draw random letters
        letters <- DrawLetters(argv$letters, argv$vowels)
        ## Filter words
        regexp <- sprintf("^[%s]+$", letters)
        lwords <- words[ grepl(regexp, words) ]
        ## Draw random words
        if( length(lwords) >= argv$words) {
            lwords <- sort(sample(lwords, size = argv$words))
            WriteWords(letters, lwords)
            i <- i + 1
        }
        j <- j + 1
    }
}
cat("Generate", i , "word list(s) after", j, "tentative(s)\n")
