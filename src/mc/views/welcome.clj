(ns mc.views.welcome
  (:require [mc.views.common :as common]
            [noir.content.pages :as pages])
  (:use mc.db
        noir.core
        hiccup.core
        hiccup.page-helpers))

(defpage "/welcome" []
         (common/layout
          [:p "Welcome to magicluster"]))

(defpartial tournament-link [{:keys [id name date]}]
  (let [formatter (java.text.SimpleDateFormat. "MM/dd")]
    [:a {:href (str "/tournament/" id)}
     (.format formatter date) "  " name]))

(defpartial tournament-summary-list-item [ts]
  [:li.tournamentSummary (tournament-link ts)])

(defpartial tournament-summary-list [tss]
  [:ul.tournamentSummaries
   (map tournament-summary-list-item tss)])

(defpage "/tournaments" []
  (let [tss (load-tournament-summaries)]
    (tournament-summary-list tss)))

(defpartial table-header [val]
  [:th val])

(defpartial card-list-item [card]
  [:li card])

(defpartial deck-cell-chunks [count ss]
  (let [cards (sort (map :card (filter #(= (:count %) count) ss)))]
    [:td [:ul (map card-list-item cards)]]))

(defpartial deck-block [ss]
  (let [counts (reverse (sort (set (map :count ss))))]
    [:table
     [:tr (map table-header counts)]
     [:tr (map deck-cell-chunks counts (repeat ss))]
     ]))

(defpartial deck-detail [d]
  [:div (:player d)]
  [:div.main (deck-block (:main d))]
  [:div.sideboard (deck-block (:sideboard d))])

(defpartial tournament-detail [t]
  [:div (tournament-link t)]
  [:ul.decks (map deck-detail (:decks t))]
  [:pre (str t)])

(defpage "/tournament/:id" {tournament-id :id}
  (let [t (load-tournament tournament-id)]
    (tournament-detail t)))
