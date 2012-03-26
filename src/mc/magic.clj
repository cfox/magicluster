(ns mc.magic)

(defrecord Tournament
    [id
     name
     date
     decks])

(defrecord Deck
    [player
     main
     sideboard])

(defrecord Slot
    [card
     count])
