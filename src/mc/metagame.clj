(ns mc.metagame
  (:require [clojure.set :as set])
  (:use (mc magic cluster))
  (:import (mc.magic Tournament Deck Slot Result)))

(defn deck-cardset
  "Return a set of all cards contained in main deck."
  [deck]
  (set (map :card (:main deck))))

(defn tournament-cardset
  "Return a set of all cards played in main decks in a tournament."
  [tournament]
  (reduce set/union (map deck-cardset (:decks tournament))))

(defn metagame-dimensions
  "Return a sorted collection of all distinct cards played in the tournaments."
  [tournaments]
  (sort (vec (reduce set/union (map tournament-cardset tournaments)))))

(defn vectorize-deck
  "Given dimensions, convert the main deck into a vector."
  [dimensions deck]
  (let [cardmap (zipmap (map :card (:main deck)) (map :count (:main deck)))]
    (vec (map #(get cardmap % 0) dimensions))))

(defn vectorize-metagame
  "Return dimensions and vectorized decks."
  [tournaments]
  (let [dimensions (metagame-dimensions tournaments)
        decks (reduce conj (map :decks tournaments))
        deck-vectors (map (partial vectorize-deck dimensions) decks)]
    [dimensions deck-vectors]))

(defn unvectorize-deck
  "Return a collection of slots based on dimensions and a vectorized deck."
  [dimensions deck-vector]
  (let [indexed-vector (map vector (iterate inc 0) deck-vector)
        slot-builder (fn [[dim count]] (Slot. (nth dimensions dim) count))
        slots (map slot-builder indexed-vector)]
    (filter #(> (:count %) 0) slots)))

(defn decks-to-beat
  [tournaments]
  (let [[dimensions deck-vectors] (vectorize-metagame tournaments)
        medoids (keys (best-k-medoids deck-vectors 5 0.5 10))]
    (map (partial unvectorize-deck dimensions) medoids)))
