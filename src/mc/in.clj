(ns mc.in
  (:require [clojure.data.json :as json]
            [net.cgrand.enlive-html :as html])
  (:use mc.util
        mc.magic)
  (:import (mc.magic Tournament Deck Slot)))

(def tournament-listing-url
     "http://www.wizards.com/handlers/XMLListService.ashx?dir=mtgo&type=XMLFileInfo&start=7")

(def tournament-base-url
     "http://www.wizards.com/Magic/Digital/MagicOnlineTourn.aspx?x=mtg/digital/magiconline/tourn/")

(def deck-selector
  [[:div.deck]])

(defn fetch-listings
  "Fetch the current tournament listing from the What's Happening page."
  []
  (json/read-json (fetch-url tournament-listing-url)))

(defn extract-deck
  [markup]
  "deck")

(defn extract-tournament
  [listing]
  (let [url (str tournament-base-url (:Hyperlink listing))
        doc (html/html-resource (java.net.URL. url))
        decks (map extract-deck (html/select doc deck-selector))]
    (Tournament.
     (:Hyperlink listing)
     (:Name listing)
     (:Date listing)
     decks)))
