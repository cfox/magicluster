(ns mc.magic)

(defrecord Tournament
    [id                                 ;id from mtgo
     name                               ;e.g. Standard Daily
     date                               ;a java.util.Date
     decks                              ;Decks
     results                            ;Results
     ])

(defrecord Deck
    [player                             ;canonical player name
     main                               ;Slots
     sideboard                          ;Slots
     ])

(defrecord Slot
    [card                               ;canonical card name
     count                              ;integer between 1 and 4 inclusive
     ])

(defrecord Result
    [player                             ;canonical player name
     placing                            ;e.g. 1 for first place
     points                             ;e.g. 12 for 3-1
     ])
