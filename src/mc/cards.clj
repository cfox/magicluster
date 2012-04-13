(ns mc.cards
  (:use mc.magic)
  (:use [clojure.contrib.duck-streams :only [read-lines]])
  (:import (mc.magic Card)))

(def cost-re #"[\(\)/ 0-9WwUuBbGgRrXp]+")

(defn cost-offset
  [text]
  (if (re-matches cost-re (second text)) 1 0))

(defn card-text-seq
  [filename]
  (filter #(every? not-empty %) (partition-by empty? (read-lines filename))))

(defn parse-name
  [text]
  (first text))

(defn parse-cost
  [text]
  (re-matches cost-re (second text)))

(defn parse-type
  [text]
  (nth text (+ 1 (cost-offset text))))

(defn creature?
  [text]
  (let [creature-re #".*Creature.*"]
    (re-matches creature-re (parse-type text))))

(defn creature-offset
  [text]
  (if (creature? text) 1 0))

(defn parse-pt
  [text]
  (if (creature? text)
    (nth text (+ 2 (cost-offset text)))))

(defn parse-text
  [text]
  (let [start (+ 2 (cost-offset text) (creature-offset text))
        lines (- (count text) start 1)]
    (take lines (drop start text))))

(defn parse-card
  [text]
  (Card.
   (parse-name text)
   (parse-cost text)
   (parse-type text)
   (parse-pt text)
   (parse-text text)
   ))
