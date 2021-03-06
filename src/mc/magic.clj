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

(defrecord Card
    [name                               ;canonical card name
     cost                               ;e.g. 2B
     type                               ;e.g. Sorcery or Creature -- Elf
     pt                                 ;e.g. 2/1
     text                               ;sequence of lines
     printings				;map of set abbreviations to rarity
     ])

(defrecord Set
    [abbr				;set abbreviation
     name				;set name
     ])
