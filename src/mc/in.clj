(ns mc.in
  (:require [clojure.data.json :as json]
            [clojure.string :as string])
  (:use (mc util magic))
  (:import (mc.magic Tournament Deck Slot)))

(def tournament-listing-url
     "http://www.wizards.com/handlers/XMLListService.ashx?dir=mtgo&type=XMLFileInfo&start=7")

(def tournament-base-url
     "http://www.wizards.com/Magic/Digital/MagicOnlineTourn.aspx?x=mtg/digital/magiconline/tourn/")

(defn fetch-listings
  "Fetch the current tournament listing from the What's Happening page."
  []
  (json/read-json (fetch-url tournament-listing-url)))

(defn extract-player
  "Extracts a player name from some deck markup."
  [markup]
  (let [player-re #"<heading>([^<]+)</heading>"]
    (if-let [matches (re-seq player-re markup)]
      (first (string/split (second (first matches)) #" "))
      "unknown")))

(defn extract-slots
  "Extracts some card slots from some deck markup."
  [markup]
  (let [card-re #"(\d+).\s*<a class=\"nodec\"[^>]*>([\w ]+)<"
        matches (re-seq card-re markup)
        slot-builder
        (fn [match] (Slot. (last match) (Integer/valueOf (second match))))]
    (map slot-builder matches)))

(defn extract-deck
  "Extracts a deck from some deck markup."
  [markup]
  (let [sideboard-delimiter #"<i>Sideboard</i>"
        [main sideboard] (string/split markup sideboard-delimiter)]
    (Deck.
     (extract-player markup)
     (extract-slots main)
     (extract-slots sideboard))))

(defn extract-tournament
  "Given a tournament listing from the What's Happeng page, extracts one."
  [listing]
  (let [url (str tournament-base-url (:Hyperlink listing))
        doc (fetch-url url)
        deck-delimiter #"<div class=\"deck\">"
        decks (map extract-deck (rest (string/split doc deck-delimiter)))]
    (Tournament.
     (:Hyperlink listing)
     (:Name listing)
     (fix-date (:Date listing))
     decks)))

(defn extract-one-tournament
  []
  (let [listings (fetch-listings)]
    (extract-tournament (first listings))))
