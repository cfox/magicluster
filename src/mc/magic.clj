(ns mc.magic)

(defrecord Tournament
    [id
     name
     date
     decks])

(defrecord Deck
    [main
     sideboard])

(defrecord Slot
    [card
     count])
